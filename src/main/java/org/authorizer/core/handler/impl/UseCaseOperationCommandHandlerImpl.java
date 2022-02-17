package org.authorizer.core.handler.impl;

import org.authorizer.core.usecase.OperationCommandUseCase;
import org.authorizer.core.handler.OperationCommandHandler;
import org.authorizer.core.usecase.dto.Command;
import org.authorizer.core.usecase.dto.OperationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
@Log
public class UseCaseOperationCommandHandlerImpl implements OperationCommandHandler {

    private final ApplicationContext context;

    private final Map<Class<?>, OperationCommandUseCase<Command>> operationCommandUseCases = new LinkedHashMap<>();

    @PostConstruct
    public void load() {
        operationCommandUseCases.clear();
        context.getBeansOfType(OperationCommandUseCase.class).values()
                .forEach(it -> {
                    Class<?> clazz = (Class<?>) ((ParameterizedType) ((Class<?>) it.getClass().getGenericInterfaces()[0]).getGenericInterfaces()[0]).getActualTypeArguments()[0];
                    operationCommandUseCases.put(clazz, it);
                });
    }

    @Override
    public OperationResult execute(Command command) {
        OperationResult result;
        try {
            log.log(Level.INFO, () -> String.format("Executing command %s", command));
            result = operationCommandUseCases.get(command.getClass()).execute(command);
            log.log(Level.INFO, () -> String.format("Command %s executed with success", command));
        } catch (Exception ex) {
            log.log(Level.SEVERE, String.format("Unexpected error executing command %s", command), ex);
            throw new RuntimeException(getMessage(ex), ex);
        }
        return result;
    }

    private String getMessage(Exception ex) {
        Throwable cause = ex;
        int level = 0;
        while (cause.getCause() != cause && nonNull(cause.getCause()) && ++level < 10) {
            cause = cause.getCause();
        }
        final String message = cause.getMessage();
        return ObjectUtils.isEmpty(message) ? cause.toString() : message;
    }
}
