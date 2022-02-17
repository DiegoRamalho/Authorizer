package org.authorizer.dataprovider.repository;

import org.authorizer.core.entity.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findById(UUID id);
    Optional<Account> findFirst();
    Account save(Account account);
    void deleteAll();
}
