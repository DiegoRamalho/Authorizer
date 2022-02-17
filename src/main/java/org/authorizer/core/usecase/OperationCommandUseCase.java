package org.authorizer.core.usecase;

import org.authorizer.core.usecase.dto.Command;
import org.authorizer.core.usecase.dto.OperationResult;

public interface OperationCommandUseCase<T extends Command> {
    OperationResult execute(T command);
}
