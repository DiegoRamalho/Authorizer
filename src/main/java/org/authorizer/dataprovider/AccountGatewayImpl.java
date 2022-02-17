package org.authorizer.dataprovider;

import org.authorizer.core.entity.Account;
import org.authorizer.core.gateway.AccountGateway;
import org.authorizer.dataprovider.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class AccountGatewayImpl implements AccountGateway {

    private final AccountRepository accountRepository;

    /**
     * Find an account by id or get the first one registered
     *
     * @param accountId The AccountId
     * @return Optional<Account>
     */
    @Override
    public Optional<Account> findByIdOrFirst(UUID accountId) {
        if (nonNull(accountId)) {
            return accountRepository.findById(accountId);
        }
        return accountRepository.findFirst();
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void deleteAll() {
        accountRepository.deleteAll();
    }
}
