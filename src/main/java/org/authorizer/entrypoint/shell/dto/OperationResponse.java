package org.authorizer.entrypoint.shell.dto;

import org.authorizer.core.usecase.dto.OperationResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperationResponse {
    private AccountOperation account;
    private List<String> violations;

    public static OperationResponse from(OperationResult operationResult) {
        return new OperationResponse(AccountOperation.from(operationResult.getAccount()), operationResult.getViolations());
    }
}
