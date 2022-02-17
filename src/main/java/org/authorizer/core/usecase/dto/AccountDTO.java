package org.authorizer.core.usecase.dto;

import org.authorizer.core.entity.Account;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

import static java.util.Objects.nonNull;

@Getter
@Builder
public class AccountDTO {
    private final Boolean activeCard;
    private final BigDecimal availableLimit;

    public static AccountDTO from(Account account) {
        var builder = AccountDTO.builder();
        if (nonNull(account)) {
            builder.activeCard(account.getActiveCard())
                    .availableLimit(account.getAvailableLimit());
        }
        return builder.build();
    }
}
