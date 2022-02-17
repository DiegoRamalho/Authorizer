package org.authorizer.core.usecase.rule;

import org.authorizer.core.entity.Account;
import org.authorizer.core.usecase.dto.ValidationResult;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class AccountValidateRuleExecutor {

    /**
     * Check the validation rules against the modifications in the current account state
     *
     * @param rules               Chain of account validate rules
     * @param modification        Modifications to check
     * @param currentAccountState Current Account State
     * @param <T>                 The modification class
     * @param <R>                 An rule the validate the account with modification of type T
     * @return A ValidationResult with:
     * valid = true if the modifications are valid, else
     * valid = false and a list of violations
     */
    public <T, R extends AccountValidateRule<T>> ValidationResult validate(List<R> rules, T modification, Account currentAccountState) {
        List<String> violations = new LinkedList<>();
        for (R rule : rules) {
            var validationResult = rule.validate(modification, currentAccountState);
            violations.addAll(validationResult.getViolations());
            // if it's not valid and must break the chain execution in this case
            if (!validationResult.isValid() && rule.breakWithError()) {
                break;
            }
        }
        return ValidationResult.of(violations);
    }
}
