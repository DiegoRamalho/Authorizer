package org.authorizer.builder;

import org.authorizer.core.entity.Transaction;
import org.authorizer.core.usecase.dto.AuthorizeTransactionCommand;

import static java.util.Objects.nonNull;

public class AuthorizeTransactionCommandBuilder {

    public static AuthorizeTransactionCommand from(Transaction transaction) {
        var builder = AuthorizeTransactionCommand.builder();
        if (nonNull(transaction)) {
            builder
                    .amount(transaction.getAmount())
                    .merchant(transaction.getMerchant())
                    .time(transaction.getTime());
        }
        return builder.build();
    }
}
