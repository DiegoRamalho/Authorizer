package org.authorizer.core.gateway;

import org.authorizer.core.entity.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountGateway {
    /**
     * Find an account by id or get the first one registered
     *
     * @param accountId The AccountId
     * @return Optional<Account>
     */
    Optional<Account> findByIdOrFirst(UUID accountId);

    Account save(Account account);

    void deleteAll();
}
