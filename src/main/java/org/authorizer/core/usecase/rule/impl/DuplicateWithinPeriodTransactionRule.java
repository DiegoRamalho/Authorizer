package org.authorizer.core.usecase.rule.impl;

import org.authorizer.core.config.ApplicationCoreProperties;
import org.authorizer.core.entity.Account;
import org.authorizer.core.entity.Transaction;
import org.authorizer.core.usecase.dto.ValidationResult;
import org.authorizer.core.usecase.rule.AuthorizeTransactionRule;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * RULE: There must be no more than X similar transaction (same value and merchant) within Y minutes.
 */
@Component
@RequiredArgsConstructor
public class DuplicateWithinPeriodTransactionRule implements AuthorizeTransactionRule {

    private final ApplicationCoreProperties properties;

    @Override
    public ValidationResult validate(Transaction modification, Account currentAccountState) {
        var transactionLimit = properties.getTransaction().getSameTransactionFrequencyLimit();
        var transactionsWithinPeriod = currentAccountState.getTransactionsAfter(modification.getTime().minusMinutes(transactionLimit.getMinutes()))
                .filter(it -> Objects.equals(it.getMerchant(), modification.getMerchant()) &&
                        Objects.equals(it.getAmount(), modification.getAmount()))
                .count();
        boolean valid = transactionsWithinPeriod < transactionLimit.getAmount();
        return ValidationResult.validOrWithError(valid, properties.getError().getDuplicateTransactionKey());
    }

    @Override
    public int getOrder() {
        return DUPLICATE_WITHIN_PERIOD_TRANSACTION_RULE_ORDER;
    }
}
