package org.authorizer.entrypoint.shell.impl;

import org.authorizer.BaseIntegrationTest;
import org.authorizer.core.gateway.AccountGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShellAuthorizeControllerImplIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ShellAuthorizeControllerImpl target;
    @Autowired
    private AccountGateway repository;

    private static final String SHELL_INTEGRATION_TESTS_FOLDER = "shell";

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void systemInputAndOutputRestore() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @AfterEach
    public void databaseRestore() {
        repository.deleteAll();
    }

    @Override
    @ParameterizedTest(name = "{0}.")
    @MethodSource("allTestCases")
    public void verifyThat(String testName, String inputFile, String outputFile) throws Exception {
        // Given
        System.setIn(new ByteArrayInputStream(Files.readAllBytes(Paths.get(inputFile))));
        String expected = Files.readString(Paths.get(outputFile));
        // When
        target.processOperationsFromStdInToStdOut();
        // Then
        assertEquals(removeSpaces(expected), removeSpaces(testOut.toString()));
    }

    private static Stream<Arguments> allTestCases() {
        return allTestCasesFrom(SHELL_INTEGRATION_TESTS_FOLDER);
    }

    private String removeSpaces(String str) {
        return str.replaceAll("[ \n\r]", "");
    }
}
