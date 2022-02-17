package org.authorizer.core.usecase.impl;

import org.authorizer.core.entity.Account;
import org.authorizer.core.entity.Transaction;
import org.authorizer.core.gateway.AccountGateway;
import org.authorizer.core.usecase.AuthorizeTransactionUseCase;
import org.authorizer.core.usecase.dto.AccountDTO;
import org.authorizer.core.usecase.dto.AuthorizeTransactionCommand;
import org.authorizer.core.usecase.dto.OperationResult;
import org.authorizer.core.usecase.rule.AccountValidateRuleExecutor;
import org.authorizer.core.usecase.rule.AuthorizeTransactionRule;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorizeTransactionUseCaseImpl implements AuthorizeTransactionUseCase {

    private final AccountGateway accountGateway;
    private final List<AuthorizeTransactionRule> rules;
    private final AccountValidateRuleExecutor accountValidateRuleExecutor;

    @Override
    public OperationResult execute(AuthorizeTransactionCommand command) {
        Transaction newTransaction = new Transaction(command.getMerchant(), command.getAmount(), command.getTime());
        Account currentState = accountGateway.findByIdOrFirst(command.getAccountId()).orElse(null);
        var validationResult = accountValidateRuleExecutor.validate(rules, newTransaction, currentState);
        // Operations that have violations should not be saved in the internal state of the application.
        if (validationResult.isValid()) {
            // Adds the new transaction and update the current state.
            currentState = accountGateway.save(currentState.addTransaction(newTransaction));
        }
        return OperationResult.of(AccountDTO.from(currentState), validationResult.getViolations());
    }
}
