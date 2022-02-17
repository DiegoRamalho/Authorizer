package org.authorizer.entrypoint.shell.dto;

import org.authorizer.core.usecase.dto.Command;

public interface CommandOperation {
    Command toCommand();
}
