package org.authorizer.core.usecase.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@ToString
public class AuthorizeTransactionCommand implements Command {
    private final UUID accountId;
    private final String merchant;
    private final BigDecimal amount;
    private final LocalDateTime time;
}
