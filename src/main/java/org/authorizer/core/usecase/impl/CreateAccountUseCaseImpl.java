package org.authorizer.core.usecase.impl;

import org.authorizer.core.entity.Account;
import org.authorizer.core.gateway.AccountGateway;
import org.authorizer.core.usecase.CreateAccountUseCase;
import org.authorizer.core.usecase.dto.AccountDTO;
import org.authorizer.core.usecase.dto.CreateAccountCommand;
import org.authorizer.core.usecase.dto.OperationResult;
import org.authorizer.core.usecase.rule.CreateAccountRule;
import org.authorizer.core.usecase.rule.AccountValidateRuleExecutor;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateAccountUseCaseImpl implements CreateAccountUseCase {

    private final AccountGateway accountGateway;
    private final List<CreateAccountRule> rules;
    private final AccountValidateRuleExecutor accountValidateRuleExecutor;

    @Override
    public OperationResult execute(CreateAccountCommand command) {
        Account newAccount = new Account(command.getActiveCard(), command.getAvailableLimit());
        Account currentState = accountGateway.findByIdOrFirst(null).orElse(null);
        var validationResult = accountValidateRuleExecutor.validate(rules, newAccount, currentState);
        if (validationResult.isValid()) {
            // Operations that have violations should not be saved in the internal state of the application.
            currentState = accountGateway.save(newAccount);
        }
        return OperationResult.of(AccountDTO.from(currentState), validationResult.getViolations());
    }
}
