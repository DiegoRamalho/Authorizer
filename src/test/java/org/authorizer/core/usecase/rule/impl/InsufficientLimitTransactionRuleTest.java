package org.authorizer.core.usecase.rule.impl;

import org.authorizer.BaseMockitoTest;
import org.authorizer.core.config.ApplicationCoreProperties;
import org.authorizer.core.entity.Account;
import org.authorizer.core.entity.Transaction;
import org.authorizer.builder.AccountBuilder;
import org.authorizer.builder.ApplicationPropertiesBuilder;
import org.authorizer.builder.TransactionBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.authorizer.builder.TransactionBuilder.fromMerchant1With;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tags({@Tag("account"), @Tag("transaction"), @Tag("rule")})
class InsufficientLimitTransactionRuleTest extends BaseMockitoTest {

    @Mock
    private ApplicationCoreProperties properties;
    @InjectMocks
    private InsufficientLimitTransactionRule target;

    private final String key = "ERROR-KEY";

    @BeforeEach
    public void init() {
        when(properties.getError()).thenReturn(ApplicationPropertiesBuilder.insufficientLimitKey(key));
    }

    @Test
    void shouldReturnErrorWhenTransactionAmountIsGreaterThanCardLimit() {
        // Given
        Account currentAccountState = AccountBuilder.activeCardWithLimitOfTen();
        Transaction modification = TransactionBuilder.fromMerchant1With(currentAccountState.getAvailableLimit().add(BigDecimal.ONE));

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertFalse(result.isValid());
        assertThat(result.getViolations(), containsInAnyOrder(key));
    }


    @Test
    void shouldReturnOkWhenTransactionAmountIsEqualsToCardLimit() {
        // Given
        Account currentAccountState = AccountBuilder.activeCardWithLimitOfTen();
        Transaction modification = TransactionBuilder.fromMerchant1With(currentAccountState.getAvailableLimit());

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertTrue(result.isValid());
        assertTrue(result.getViolations().isEmpty());
    }


    @Test
    void shouldReturnOkWhenTransactionAmountIsLessThanCardLimit() {
        // Given
        Account currentAccountState = AccountBuilder.activeCardWithLimitOfTen();
        Transaction modification = TransactionBuilder.fromMerchant1With(currentAccountState.getAvailableLimit().subtract(BigDecimal.ONE));

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertTrue(result.isValid());
        assertTrue(result.getViolations().isEmpty());
    }
}
