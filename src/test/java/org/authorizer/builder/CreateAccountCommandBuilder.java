package org.authorizer.builder;

import org.authorizer.core.entity.Account;
import org.authorizer.core.usecase.dto.CreateAccountCommand;

import static java.util.Objects.nonNull;


public class CreateAccountCommandBuilder {

    public static CreateAccountCommand from(Account account) {
        var builder = CreateAccountCommand.builder();
        if (nonNull(account)) {
            builder
                    .activeCard(account.getActiveCard())
                    .availableLimit(account.getAvailableLimit());
        }
        return builder.build();
    }
}
