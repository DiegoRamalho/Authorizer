package org.authorizer.entrypoint.shell.impl;

import org.authorizer.core.handler.OperationCommandHandler;
import org.authorizer.entrypoint.shell.AuthorizeController;
import org.authorizer.entrypoint.shell.dto.OperationRequest;
import org.authorizer.entrypoint.shell.dto.OperationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log
public class ShellAuthorizeControllerImpl implements AuthorizeController {

    private final ObjectMapper objectMapper;
    private final OperationCommandHandler commandUseCaseHandler;

    @Override
    public void processOperationsFromStdInToStdOut() {
        List<OperationResponse> operations = processOperationsFrom(System.in);
        saveOperationsAt(System.out, operations);
    }

    private List<OperationResponse> processOperationsFrom(InputStream inputStream) {
        List<OperationResponse> operations = new LinkedList<>();
        try (var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                operations.addAll(processLine(line));
            }
        } catch (Exception exception) {
            log.log(Level.SEVERE, "Unexpected error processing operations from inputStream", exception);
        }
        return operations;
    }

    private List<OperationResponse> processLine(String line) {
        List<OperationResponse> operations = new LinkedList<>();
        try {
            OperationRequest request = objectMapper.readValue(line, OperationRequest.class);
            // get all commands inside the line and call the commandUseCaseHandler to execute it.
            operations.addAll(request.commands().stream()
                    .map(it -> OperationResponse.from(commandUseCaseHandler.execute(it)))
                    .collect(Collectors.toList()));
        } catch (Exception exception) {
            log.log(Level.SEVERE, String.format("Unexpected error processing line: \"%s\"", line), exception);
        }
        return operations;
    }

    private void saveOperationsAt(OutputStream outputStream, List<OperationResponse> operations) {
        try (var writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            writer.write(getOperationsFormatted(operations));
        } catch (Exception exception) {
            log.log(Level.SEVERE, "Unexpected error saving operations at outputStream", exception);
        }
    }

    /**
     * Gets a string where each operation is within a line.
     *
     * @param operations Operations to format
     * @return All operations formatted as json
     */
    private String getOperationsFormatted(List<OperationResponse> operations) {
        StringJoiner operationJoiner = new StringJoiner(System.lineSeparator());
        for (OperationResponse operation : operations) {
            try {
                operationJoiner.add(objectMapper.writeValueAsString(operation));
            } catch (Exception exception) {
                log.log(Level.SEVERE, String.format("Unexpected error formatting operation: \"%s\"", operation), exception);
            }
        }
        return operationJoiner.toString();
    }
}
