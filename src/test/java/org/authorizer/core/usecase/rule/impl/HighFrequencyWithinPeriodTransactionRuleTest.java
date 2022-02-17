package org.authorizer.core.usecase.rule.impl;

import org.authorizer.BaseMockitoTest;
import org.authorizer.builder.TransactionBuilder;
import org.authorizer.core.config.ApplicationCoreProperties;
import org.authorizer.core.entity.Account;
import org.authorizer.core.entity.Transaction;
import org.authorizer.builder.AccountBuilder;
import org.authorizer.builder.ApplicationPropertiesBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tags({@Tag("account"), @Tag("transaction"), @Tag("rule")})
class HighFrequencyWithinPeriodTransactionRuleTest extends BaseMockitoTest {

    @Mock
    private ApplicationCoreProperties properties;
    @InjectMocks
    private HighFrequencyWithinPeriodTransactionRule target;

    private final String key = "ERROR-KEY";
    private final Long amountLimit = 2L;
    private final Long minutesLimit = 5L;
    // Limit inferior
    private final LocalDateTime firstTransactionTime = TransactionBuilder.DEFAULT_TIME.plusSeconds(1);
    // limit superior
    private final LocalDateTime limitTransactionTime = TransactionBuilder.DEFAULT_TIME.plusMinutes(minutesLimit);

    @BeforeEach
    public void init() {
        when(properties.getError()).thenReturn(ApplicationPropertiesBuilder.highFrequencySmallIntervalKey(key));
        when(properties.getTransaction()).thenReturn(ApplicationPropertiesBuilder.anyMerchantFrequencyLimit(amountLimit, minutesLimit));
    }

    @Test
    void shouldReturnErrorWhenTransactionAmountWithinPeriodIsExceeded() {
        // Given
        Account currentAccountState = AccountBuilder.activeAccountWithTransactionsFromRandomMerchantAt(firstTransactionTime, limitTransactionTime);

        Transaction modification = TransactionBuilder.fromMerchant1With(BigDecimal.ONE, limitTransactionTime);

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertFalse(result.isValid());
        assertThat(result.getViolations(), containsInAnyOrder(key));
    }

    @Test
    void shouldReturnOkWhenTransactionAmountWithinPeriodIsAllowed() {
        // Given
        Account currentAccountState = AccountBuilder.activeAccountWithTransactionsFromRandomMerchantAt(limitTransactionTime);

        Transaction modification = TransactionBuilder.fromMerchant1With(BigDecimal.ONE, limitTransactionTime);

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertTrue(result.isValid());
        assertTrue(result.getViolations().isEmpty());
    }

    @Test
    void shouldReturnOKWhenTransactionIsAfterPeriod() {
        // Given
        Account currentAccountState = AccountBuilder.activeAccountWithTransactionsFromRandomMerchantAt(firstTransactionTime, limitTransactionTime);

        Transaction modification = TransactionBuilder.fromMerchant1With(BigDecimal.ONE, limitTransactionTime.plusMinutes(minutesLimit));

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertTrue(result.isValid());
        assertTrue(result.getViolations().isEmpty());
    }
}
