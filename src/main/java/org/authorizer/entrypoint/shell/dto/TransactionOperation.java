package org.authorizer.entrypoint.shell.dto;

import org.authorizer.core.usecase.dto.AuthorizeTransactionCommand;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.util.ObjectUtils.isEmpty;

@Data
@NoArgsConstructor
public class TransactionOperation implements CommandOperation {
    private String accountId;
    private String merchant;
    private BigDecimal amount;
    private LocalDateTime time;

    public AuthorizeTransactionCommand toCommand() {
        return AuthorizeTransactionCommand
                .builder()
                .accountId(isEmpty(this.accountId) ? null : UUID.fromString(this.accountId))
                .merchant(this.merchant)
                .amount(this.amount)
                .time(this.time)
                .build();
    }
}
