package org.authorizer.entrypoint.shell.dto;

import org.authorizer.core.usecase.dto.Command;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.nonNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperationRequest {
    private AccountOperation account;
    private TransactionOperation transaction;

    public List<Command> commands() {
        List<Command> result = new LinkedList<>();
        if (nonNull(account)) result.add(account.toCommand());
        if (nonNull(transaction)) result.add(transaction.toCommand());
        return result;
    }
}
