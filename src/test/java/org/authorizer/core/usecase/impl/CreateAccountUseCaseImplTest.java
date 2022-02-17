package org.authorizer.core.usecase.impl;

import org.authorizer.BaseMockitoTest;
import org.authorizer.builder.CreateAccountCommandBuilder;
import org.authorizer.core.entity.Account;
import org.authorizer.core.gateway.AccountGateway;
import org.authorizer.core.usecase.dto.ValidationResult;
import org.authorizer.core.usecase.rule.CreateAccountRule;
import org.authorizer.core.usecase.rule.AccountValidateRuleExecutor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tags({@Tag("account"), @Tag("use-case")})
class CreateAccountUseCaseImplTest extends BaseMockitoTest {

    @Mock
    private AccountGateway accountGateway;
    @Mock
    private List<CreateAccountRule> rules;
    @Mock
    private AccountValidateRuleExecutor accountValidateRuleExecutor;
    @InjectMocks
    private CreateAccountUseCaseImpl target;

    @ParameterizedTest(name = "Account {0}.")
    @MethodSource("org.authorizer.builder.AccountBuilder#genericAccounts")
    public void shouldCreateWhenAccountIsValid(Account account) {
        // Given
        doReturn(Optional.of(account)).when(accountGateway).findByIdOrFirst(null);
        doReturn(account).when(accountGateway).save(any(Account.class));
        doReturn(ValidationResult.ok()).when(accountValidateRuleExecutor).validate(eq(rules), any(Account.class), eq(account));

        // When
        var result = target.execute(CreateAccountCommandBuilder.from(account));

        // Then
        assertNotNull(result);

        // should save the account
        verify(accountGateway).save(any(Account.class));

        // shouldn't have violations
        assertTrue(result.getViolations().isEmpty());

        // should return the current account
        assertEquals(account.getActiveCard(), result.getAccount().getActiveCard());
        assertEquals(account.getAvailableLimit(), result.getAccount().getAvailableLimit());
    }

    @ParameterizedTest(name = "Account {0}.")
    @MethodSource("org.authorizer.builder.AccountBuilder#genericAccounts")
    void shouldNotCreateWhenAccountIsInvalid(Account account) {
        // Given
        doReturn(Optional.of(account)).when(accountGateway).findByIdOrFirst(null);

        var error = "Error";
        doReturn(ValidationResult.error(error)).when(accountValidateRuleExecutor).validate(eq(rules), any(Account.class), eq(account));

        // When
        var result = target.execute(CreateAccountCommandBuilder.from(account));

        // Then
        assertNotNull(result);

        // shouldn't save the account
        verify(accountGateway, never()).save(any(Account.class));

        // should have violations
        assertFalse(result.getViolations().isEmpty());
        assertThat(result.getViolations(), containsInAnyOrder(error));

        // should return the current account
        assertEquals(account.getActiveCard(), result.getAccount().getActiveCard());
        assertEquals(account.getAvailableLimit(), result.getAccount().getAvailableLimit());
    }
}
