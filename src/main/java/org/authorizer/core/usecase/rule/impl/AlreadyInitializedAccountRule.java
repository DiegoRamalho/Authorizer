package org.authorizer.core.usecase.rule.impl;

import org.authorizer.core.config.ApplicationCoreProperties;
import org.authorizer.core.entity.Account;
import org.authorizer.core.usecase.dto.ValidationResult;
import org.authorizer.core.usecase.rule.CreateAccountRule;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

/**
 * RULE: Once created, the account must not be updated or recreated.
 */
@Component
@RequiredArgsConstructor
public class AlreadyInitializedAccountRule implements CreateAccountRule {

    private final ApplicationCoreProperties properties;

    @Override
    public ValidationResult validate(Account modification, Account currentAccountState) {
        var valid = isNull(currentAccountState);
        return ValidationResult.validOrWithError(valid, properties.getError().getAccountAlreadyInitializedKey());
    }
}
