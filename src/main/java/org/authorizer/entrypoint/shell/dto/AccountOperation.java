package org.authorizer.entrypoint.shell.dto;

import org.authorizer.core.usecase.dto.AccountDTO;
import org.authorizer.core.usecase.dto.CreateAccountCommand;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class AccountOperation implements CommandOperation {
    @JsonProperty("active-card")
    private Boolean activeCard;
    @JsonProperty("available-limit")
    private BigDecimal availableLimit;

    public static AccountOperation from(AccountDTO account) {
        return new AccountOperation(account.getActiveCard(), account.getAvailableLimit());
    }

    public CreateAccountCommand toCommand() {
        return CreateAccountCommand
                .builder()
                .availableLimit(this.getAvailableLimit())
                .activeCard(this.getActiveCard())
                .build();
    }
}
