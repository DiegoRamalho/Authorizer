package org.authorizer.core.usecase.rule;

import org.authorizer.core.entity.Account;
import org.authorizer.core.usecase.dto.ValidationResult;
import org.springframework.core.Ordered;

/**
 * Interface for the account validation rules within a ordered chain.
 * <p>
 * Each rule can be checked using current account status and account modifications.
 * </p>
 * <p>
 * The order in the chain is getting in the {@link #getOrder}.
 * By default all items has the same order, so it will result in arbitrary sort positions for the affected objects.
 * </p>
 * <p>
 * If the validation rule must be valid to continue the validations in the chain, just modify the {@link #breakWithError} method to return true.
 * </p>
 * <p>
 * The entire chain runs in class {@link AccountValidateRuleExecutor}
 * </p>
 *
 * @param <T> Class containing account modifications.
 */
public interface AccountValidateRule<T> extends Ordered {

    /**
     * Checks if the modification in the current account state are valid.
     *
     * @param modification        Modifications to check
     * @param currentAccountState Current Account State
     * @return A ValidationResult with:
     * valid = true if the modifications are valid, else
     * valid = false and a list of violations
     */
    ValidationResult validate(T modification, Account currentAccountState);

    /**
     * Get the item order.
     * By default all tens has the same order (LOWEST_PRECEDENCE), so it will result in arbitrary sort positions for the affected objects.
     * <p>
     * Higher values are interpreted as lower priority.
     * As a consequence, the object with the lowest value has the highest priority.
     * </p>
     *
     * @return int - Item order
     */
    @Override
    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    /**
     * Decide if an item must be break the validations in the chain.
     * By default it is false, which means validations must continue even the rule is not valid.
     *
     * @return boolean - must this item break the validations in the chain if it is not valid?
     */
    default boolean breakWithError() {
        return false;
    }
}
