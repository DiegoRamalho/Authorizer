package org.authorizer.core.handler;

import org.authorizer.core.usecase.dto.Command;
import org.authorizer.core.usecase.dto.OperationResult;

/**
 * Interface to handle operational commands.
 * It executes the command within its related use case.
 */
public interface OperationCommandHandler {
    /**
     * Executes the command within its related use case.
     *
     * @param command Command to be executed
     * @return OperationResult with:
     * account: The current account state
     * violations: a list of all violations, if any.
     */
    OperationResult execute(Command command);
}
