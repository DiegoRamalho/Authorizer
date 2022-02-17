package org.authorizer.core.usecase.rule.impl;

import org.authorizer.core.config.ApplicationCoreProperties;
import org.authorizer.core.entity.Account;
import org.authorizer.core.entity.Transaction;
import org.authorizer.core.usecase.dto.ValidationResult;
import org.authorizer.core.usecase.rule.AuthorizeTransactionRule;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

/**
 * RULE: No transactions should be made without the account being initialized.
 */
@Component
@RequiredArgsConstructor
public class InsufficientLimitTransactionRule implements AuthorizeTransactionRule {

    private final ApplicationCoreProperties properties;

    @Override
    public ValidationResult validate(Transaction modification, Account currentAccountState) {
        boolean valid = currentAccountState.getAvailableLimit().compareTo(modification.getAmount()) >= 0;
        return ValidationResult.validOrWithError(valid, properties.getError().getInsufficientLimitKey());
    }

    @Override
    public int getOrder() {
        return INSUFFICIENT_LIMIT_TRANSACTION_RULE_ORDER;
    }
}
