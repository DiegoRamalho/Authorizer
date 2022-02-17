package org.authorizer.core.usecase.rule;

import org.authorizer.core.entity.Transaction;

/**
 * Interface that identifies all account validation rules for the transaction authorization use case.
 * <p>
 * In this interface we can create constants to define the order of the validation item.
 * All items not identified here, must use the default order (LOWEST_PRECEDENCE){@link #getOrder()}
 */
public interface AuthorizeTransactionRule extends AccountValidateRule<Transaction> {
    int ACCOUNT_NOT_INITIALIZED_TRANSACTION_RULE_ORDER = HIGHEST_PRECEDENCE;
    int INSUFFICIENT_LIMIT_TRANSACTION_RULE_ORDER = ACCOUNT_NOT_INITIALIZED_TRANSACTION_RULE_ORDER + 1;
    int HIGH_FREQUENCY_WITHIN_PERIOD_TRANSACTION_RULE_ORDER = INSUFFICIENT_LIMIT_TRANSACTION_RULE_ORDER + 1;
    int DUPLICATE_WITHIN_PERIOD_TRANSACTION_RULE_ORDER = HIGH_FREQUENCY_WITHIN_PERIOD_TRANSACTION_RULE_ORDER + 1;
}
