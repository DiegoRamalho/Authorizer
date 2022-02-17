package org.authorizer.core.usecase.rule.impl;

import org.authorizer.BaseMockitoTest;
import org.authorizer.core.config.ApplicationCoreProperties;
import org.authorizer.core.entity.Account;
import org.authorizer.core.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.authorizer.builder.AccountBuilder.activeCardWithLimitOfTen;
import static org.authorizer.builder.AccountBuilder.inactiveCardWithLimitOfTen;
import static org.authorizer.builder.ApplicationPropertiesBuilder.cardNotActiveKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tags({@Tag("account"), @Tag("transaction"), @Tag("rule")})
class AccountCardNotActiveTransactionRuleTest extends BaseMockitoTest {

    @Mock
    private ApplicationCoreProperties properties;
    @InjectMocks
    private AccountCardNotActiveTransactionRule target;

    private final String key = "ERROR-KEY";

    @BeforeEach
    public void init() {
        when(properties.getError()).thenReturn(cardNotActiveKey(key));
    }

    @ParameterizedTest(name = "Transaction {0}.")
    @MethodSource("org.authorizer.builder.TransactionBuilder#genericTransactions")
    @NullSource
    void shouldReturnErrorWhenAccountCardIsInactive(Transaction modification) {
        // Given
        Account currentAccountState = inactiveCardWithLimitOfTen();

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertFalse(result.isValid());
        assertThat(result.getViolations(), containsInAnyOrder(key));
    }

    @ParameterizedTest(name = "Transaction {0}.")
    @MethodSource("org.authorizer.builder.TransactionBuilder#genericTransactions")
    void shouldReturnOkWhenAccountCardIsActive(Transaction modification) {
        // Given
        Account currentAccountState = activeCardWithLimitOfTen();

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertTrue(result.isValid());
        assertTrue(result.getViolations().isEmpty());
    }

}
