package org.authorizer.core.entity;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@ToString
public class Account implements Entity<UUID> {
    private final UUID id;
    private Boolean activeCard;
    private BigDecimal availableLimit;
    private final List<Transaction> transactions;

    public Account(Boolean activeCard, BigDecimal availableLimit) {
        this.id = UUID.randomUUID();
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
        transactions = new LinkedList<>();
    }

    /**
     * Subtract the transaction amount and add it into the transactions list.
     *
     * @param transaction Transaction to add.
     * @return The current state of the account.
     */
    public Account addTransaction(Transaction transaction) {
        availableLimit = availableLimit.subtract(transaction.getAmount());
        transactions.add(transaction);
        return this;
    }

    /**
     * Gets all transactions that were saved after the specified date-time.
     *
     * @param time The specified date-time
     * @return All transactions that were saved after the specified date-time.
     */
    public Stream<Transaction> getTransactionsAfter(LocalDateTime time) {
        return transactions.stream().filter(it -> it.getTime().isAfter(time));
    }
}
