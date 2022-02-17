package org.authorizer.core.usecase.rule;

import org.authorizer.core.entity.Account;

/**
 * Interface that identifies all account validation rules for the create account use case.
 */
public interface CreateAccountRule extends AccountValidateRule<Account> {
}
