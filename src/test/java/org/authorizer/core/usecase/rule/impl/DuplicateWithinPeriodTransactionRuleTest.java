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
import java.time.LocalDateTime;

import static org.authorizer.builder.TransactionBuilder.fromMerchant1With;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tags({@Tag("account"), @Tag("transaction"), @Tag("rule")})
class DuplicateWithinPeriodTransactionRuleTest extends BaseMockitoTest {

    @Mock
    private ApplicationCoreProperties properties;
    @InjectMocks
    private DuplicateWithinPeriodTransactionRule target;

    private final String key = "ERROR-KEY";
    private final Long amountLimit = 2L;
    private final Long minutesLimit = 4L;
    // Limit inferior
    private final LocalDateTime firstTransactionTime = TransactionBuilder.DEFAULT_TIME.plusSeconds(1);
    // limit superior
    private final LocalDateTime limitTransactionTime = TransactionBuilder.DEFAULT_TIME.plusMinutes(minutesLimit);

    @BeforeEach
    public void init() {
        when(properties.getError()).thenReturn(ApplicationPropertiesBuilder.duplicateTransactionKey(key));
        when(properties.getTransaction()).thenReturn(ApplicationPropertiesBuilder.sameTransactionFrequencyLimit(amountLimit, minutesLimit));
    }

    @Test
    void shouldReturnErrorWhenTransactionAmountWithinPeriodWithSameMerchantAndValueIsExceeded() {
        // Given
        Account currentAccountState = AccountBuilder.activeAccountWithTransactionsFromMerchant1At(firstTransactionTime, limitTransactionTime);

        Transaction modification = TransactionBuilder.fromMerchant1With(BigDecimal.ONE, limitTransactionTime);

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertFalse(result.isValid());
        assertThat(result.getViolations(), containsInAnyOrder(key));
    }

    @Test
    void shouldReturnOkWhenTransactionAmountWithinPeriodWithSameMerchantAndValueIsAllowed() {
        // Given
        Account currentAccountState = AccountBuilder.activeAccountWithTransactionsFromMerchant2At(limitTransactionTime);

        Transaction modification = TransactionBuilder.fromMerchant1With(BigDecimal.ONE, limitTransactionTime);

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertTrue(result.isValid());
        assertTrue(result.getViolations().isEmpty());
    }

    @Test
    void shouldReturnOkWhenTransactionAmountWithinPeriodWithSameMerchantHasDifferentValueIsAllowed() {
        // Given
        Account currentAccountState = AccountBuilder.activeAccountWithTransactionsFromMerchant2At(limitTransactionTime);

        Transaction modification = TransactionBuilder.fromMerchant1With(BigDecimal.TEN, limitTransactionTime);

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
        Account currentAccountState = AccountBuilder.activeAccountWithTransactionsFromMerchant1At(firstTransactionTime, limitTransactionTime);

        Transaction modification = TransactionBuilder.fromMerchant1With(BigDecimal.ONE, limitTransactionTime.plusMinutes(minutesLimit));

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertTrue(result.isValid());
        assertTrue(result.getViolations().isEmpty());
    }
}
