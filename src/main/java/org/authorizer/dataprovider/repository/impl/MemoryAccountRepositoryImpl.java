package org.authorizer.dataprovider.repository.impl;

import org.authorizer.core.entity.Account;
import org.authorizer.dataprovider.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemoryAccountRepositoryImpl implements AccountRepository {

    /**
     * O programa não deve depender de nenhum banco de dados externo, e o estado interno da aplicação deve
     * ser gerenciado em memória explicitamente por alguma estrutura que achar adequada. O estado da
     * aplicação deve estar vazio sempre que a aplicação for inicializada.
     */
    private final Map<UUID, Account> REGISTERS = new LinkedHashMap<>();

    @Override
    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(REGISTERS.get(id));
    }

    @Override
    public Optional<Account> findFirst() {
        return REGISTERS.values().stream().findFirst();
    }

    @Override
    public Account save(Account account) {
        REGISTERS.put(account.getId(), account);
        return account;
    }

    @Override
    public void deleteAll() {
        REGISTERS.clear();
    }
}
