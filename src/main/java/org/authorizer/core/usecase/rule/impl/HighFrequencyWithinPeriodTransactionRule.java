package org.authorizer.core.usecase.rule.impl;

import org.authorizer.core.config.ApplicationCoreProperties;
import org.authorizer.core.entity.Account;
import org.authorizer.core.entity.Transaction;
import org.authorizer.core.usecase.dto.ValidationResult;
import org.authorizer.core.usecase.rule.AuthorizeTransactionRule;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

/**
 * RULE: There should be no more than X transactions from any merchant in a Y minute interval.
 */
@Component
@RequiredArgsConstructor
public class HighFrequencyWithinPeriodTransactionRule implements AuthorizeTransactionRule {

    private final ApplicationCoreProperties properties;

    @Override
    public ValidationResult validate(Transaction modification, Account currentAccountState) {
        var transactionLimit = properties.getTransaction().getAnyMerchantFrequencyLimit();
        var transactionsWithinPeriod = currentAccountState.getTransactionsAfter(modification.getTime().minusMinutes(transactionLimit.getMinutes()))
                .count();
        boolean valid = transactionsWithinPeriod < transactionLimit.getAmount();
        return ValidationResult.validOrWithError(valid, properties.getError().getHighFrequencySmallIntervalKey());
    }

    @Override
    public int getOrder() {
        return HIGH_FREQUENCY_WITHIN_PERIOD_TRANSACTION_RULE_ORDER;
    }
}
