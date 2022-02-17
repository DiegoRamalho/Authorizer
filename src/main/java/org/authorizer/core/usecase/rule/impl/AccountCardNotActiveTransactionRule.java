package org.authorizer.core.usecase.rule.impl;

import org.authorizer.core.config.ApplicationCoreProperties;
import org.authorizer.core.entity.Account;
import org.authorizer.core.entity.Transaction;
import org.authorizer.core.usecase.dto.ValidationResult;
import org.authorizer.core.usecase.rule.AuthorizeTransactionRule;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

/**
 * RULE: The current account state must have a active card.
 */
@Component
@RequiredArgsConstructor
public class AccountCardNotActiveTransactionRule implements AuthorizeTransactionRule {

    private final ApplicationCoreProperties properties;

    @Override
    public ValidationResult validate(Transaction modification, Account currentAccountState) {
        boolean valid = currentAccountState.getActiveCard();
        return ValidationResult.validOrWithError(valid, properties.getError().getAccountCardNotActiveKey());
    }
}
