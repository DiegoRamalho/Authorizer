package org.authorizer.core.usecase.rule.impl;

import org.authorizer.core.config.ApplicationCoreProperties;
import org.authorizer.core.entity.Account;
import org.authorizer.core.entity.Transaction;
import org.authorizer.core.usecase.dto.ValidationResult;
import org.authorizer.core.usecase.rule.AuthorizeTransactionRule;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

/**
 * RULE: No transactions should be accepted unless the account has been initialized.
 */
@Component
@RequiredArgsConstructor
public class AccountNotInitializedTransactionRule implements AuthorizeTransactionRule {

    private final ApplicationCoreProperties properties;

    @Override
    public ValidationResult validate(Transaction modification, Account currentAccountState) {
        final var valid = nonNull(currentAccountState);
        return ValidationResult.validOrWithError(valid, properties.getError().getAccountNotInitializedKey());
    }

    @Override
    public int getOrder() {
        return ACCOUNT_NOT_INITIALIZED_TRANSACTION_RULE_ORDER;
    }

    @Override
    public boolean breakWithError() {
        return true;
    }
}
