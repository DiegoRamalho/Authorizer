package org.authorizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Base class for integration tests that uses a file as input and compare the result with the content inside an output file.
 */
@SpringBootTest(classes = AuthorizerApplication.class, properties = "spring.profiles.active=test")
@Tag("integration-tests")
public abstract class BaseIntegrationTest {

    @Autowired
    protected ObjectMapper mapper;

    protected static final String INTEGRATION_TESTS_FOLDER = "integration-tests";
    protected static final String INTEGRATION_TESTS_INPUT_FOLDER = "input";
    protected static final String INTEGRATION_TESTS_OUTPUT_FOLDER = "output";

    protected static Stream<Arguments> allTestCasesFrom(String testFolder) {
        List<Arguments> testCases = new LinkedList<>();
        File integrationTestDir = new File(BaseIntegrationTest.class.getClassLoader().getResource(INTEGRATION_TESTS_FOLDER).getFile() + File.separator + testFolder);
        for (File testCaseDir : integrationTestDir.listFiles()) {
            String testCase = testCaseDir.getName();
            var inputPath = testCaseDir.getAbsolutePath() + File.separator + INTEGRATION_TESTS_INPUT_FOLDER + File.separator;
            var outputPath = testCaseDir.getAbsolutePath() + File.separator + INTEGRATION_TESTS_OUTPUT_FOLDER + File.separator;
            for (File inputFileTest : new File(inputPath).listFiles()) {
                String testName = inputFileTest.getName();
                testCases.add(Arguments.of(getIntegrationTestNameFmt(testCase, testName), inputPath + testName, outputPath + testName));
            }
        }
        return testCases.stream();
    }

    protected static String getIntegrationTestNameFmt(String testCase, String testName) {
        return "should " + testCase + " " + testName;
    }

    public abstract void verifyThat(String testName, String inputFile, String outputFile) throws Exception;
}
