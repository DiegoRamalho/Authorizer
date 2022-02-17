package org.authorizer.core.usecase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OperationResult {
    private final AccountDTO account;
    private final List<String> violations;

    public static OperationResult of(AccountDTO account, List<String> violations) {
        return new OperationResult(account, violations);
    }
}
