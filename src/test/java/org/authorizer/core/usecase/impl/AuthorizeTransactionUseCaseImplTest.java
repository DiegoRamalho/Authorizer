package org.authorizer.core.usecase.impl;

import org.authorizer.BaseMockitoTest;
import org.authorizer.builder.AuthorizeTransactionCommandBuilder;
import org.authorizer.core.entity.Account;
import org.authorizer.core.entity.Transaction;
import org.authorizer.core.gateway.AccountGateway;
import org.authorizer.core.usecase.dto.ValidationResult;
import org.authorizer.core.usecase.rule.AuthorizeTransactionRule;
import org.authorizer.core.usecase.rule.AccountValidateRuleExecutor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.authorizer.builder.AccountBuilder.activeCardWithLimitOfTen;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Tags({@Tag("account"), @Tag("transaction"), @Tag("use-case")})
class AuthorizeTransactionUseCaseImplTest extends BaseMockitoTest {

    @Mock
    private AccountGateway accountGateway;
    @Mock
    private List<AuthorizeTransactionRule> rules;
    @Mock
    private AccountValidateRuleExecutor accountValidateRuleExecutor;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @InjectMocks
    private AuthorizeTransactionUseCaseImpl target;

    @ParameterizedTest(name = "Transaction {0}.")
    @MethodSource("org.authorizer.builder.TransactionBuilder#genericTransactions")
    public void shouldAddAndSaveWhenTransactionIsValid(Transaction transaction) {
        // Given
        Account currentAccountState = activeCardWithLimitOfTen();
        doReturn(Optional.of(currentAccountState)).when(accountGateway).findByIdOrFirst(null);
        doReturn(currentAccountState).when(accountGateway).save(any(Account.class));
        doReturn(ValidationResult.ok()).when(accountValidateRuleExecutor).validate(eq(rules), any(Transaction.class), eq(currentAccountState));

        BigDecimal expectedLimit = currentAccountState.getAvailableLimit().subtract(transaction.getAmount());
        int expectedTransactionsSize = currentAccountState.getTransactions().size() + 1;
        // When
        var result = target.execute(AuthorizeTransactionCommandBuilder.from(transaction));

        // Then
        assertNotNull(result);

        // should save the account
        verify(accountGateway).save(accountCaptor.capture());

        // shouldn't have violations
        assertTrue(result.getViolations().isEmpty());

        // should return the current account
        assertEquals(currentAccountState.getActiveCard(), result.getAccount().getActiveCard());
        assertEquals(expectedLimit, result.getAccount().getAvailableLimit());

        Account newAccountState = accountCaptor.getValue();
        assertEquals(expectedTransactionsSize, newAccountState.getTransactions().size());
    }

    @ParameterizedTest(name = "Transaction {0}.")
    @MethodSource("org.authorizer.builder.TransactionBuilder#genericTransactions")
    void shouldNotSaveWhenTransactionIsInvalid(Transaction transaction) {
        // Given
        Account currentAccountState = activeCardWithLimitOfTen();
        doReturn(Optional.of(currentAccountState)).when(accountGateway).findByIdOrFirst(null);

        var error = "Error";
        doReturn(ValidationResult.error(error)).when(accountValidateRuleExecutor).validate(eq(rules), any(Transaction.class), eq(currentAccountState));

        // When
        var result = target.execute(AuthorizeTransactionCommandBuilder.from(transaction));

        // Then
        assertNotNull(result);

        // shouldn't save the account
        verify(accountGateway, never()).save(any(Account.class));

        // should have violations
        assertFalse(result.getViolations().isEmpty());
        assertThat(result.getViolations(), containsInAnyOrder(error));

        // should return the current account
        assertEquals(currentAccountState.getActiveCard(), result.getAccount().getActiveCard());
        assertEquals(currentAccountState.getAvailableLimit(), result.getAccount().getAvailableLimit());
    }
}
