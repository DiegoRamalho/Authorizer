package org.authorizer.core.usecase.rule;

import org.authorizer.BaseMockitoTest;
import org.authorizer.core.entity.Account;
import org.authorizer.core.usecase.dto.ValidationResult;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static org.junit.jupiter.api.Assertions.*;

@Tags({@Tag("account"), @Tag("rule-executor")})
class AccountValidateRuleExecutorTest extends BaseMockitoTest {

    private static final String ERRO_OBJ_NULL_KEY = "ERRO-OBJ-NULL-KEY";
    private static final String ERRO_FIELD_1_NULL_KEY = "ERRO-FIELD1-NULL-KEY";
    private static final String ERRO_FIELD_2_NULL_KEY = "ERRO-FIELD2-NULL-KEY";
    public static final long DEFAULT_FIELD_1_VALUE = 1L;
    public static final String DEFAULT_FIELD_2_VALUE = "2";
    private final List<AccountValidateRule<MockObject>> rules = Arrays.asList(new ObjectNotNullRule(), new AllFieldsNotNullRule());

    @InjectMocks
    private AccountValidateRuleExecutor target;

    @Test
    void shouldReturnOkWhenObjectIsValid() {
        // Given
        Account currentAccountState = null;
        // When
        var result = target.validate(rules, valid(), currentAccountState);
        // Then
        assertNotNull(result);
        assertTrue(result.isValid());
    }

    @ParameterizedTest(name = "Object {0} - Result: {1}")
    @MethodSource("possibleErrors")
    void shouldReturnErrorWhenObjectIsInvalid(MockObject object, List<String> violationsKeys) {
        // Given
        Account currentAccountState = null;
        // When
        var result = target.validate(rules, object, currentAccountState);
        // Then
        assertNotNull(result);
        assertFalse(result.isValid());
        assertTrue(result.getViolations().containsAll(violationsKeys));
    }

    private static Stream<Arguments> possibleErrors() {
        return Stream.of(
                Arguments.of(withNoFields(), Arrays.asList(ERRO_FIELD_1_NULL_KEY, ERRO_FIELD_2_NULL_KEY)),
                Arguments.of(withField1Null(), Collections.singletonList(ERRO_FIELD_1_NULL_KEY)),
                Arguments.of(withField2Null(), Collections.singletonList(ERRO_FIELD_2_NULL_KEY)),
                Arguments.of(null, Collections.singletonList(ERRO_OBJ_NULL_KEY))
        );
    }

    private static MockObject valid() {
        return new MockObject(DEFAULT_FIELD_1_VALUE, DEFAULT_FIELD_2_VALUE);
    }

    private static MockObject withNoFields() {
        return new MockObject(null, null);
    }

    private static MockObject withField2Null() {
        return new MockObject(DEFAULT_FIELD_1_VALUE, null);
    }

    private static MockObject withField1Null() {
        return new MockObject(null, DEFAULT_FIELD_2_VALUE);
    }

    @AllArgsConstructor
    @ToString
    private static class MockObject {
        private final Long field1;
        private final String field2;
    }

    private static class ObjectNotNullRule implements AccountValidateRule<MockObject> {

        @Override
        public ValidationResult validate(MockObject obj, Account currentAccountState) {
            List<String> errors = new LinkedList<>();
            if (isNull(obj)) {
                errors.add(ERRO_OBJ_NULL_KEY);
            }
            return ValidationResult.of(errors);
        }

        @Override
        public int getOrder() {
            return 0;
        }

        @Override
        public boolean breakWithError() {
            return true;
        }
    }

    private static class AllFieldsNotNullRule implements AccountValidateRule<MockObject> {
        @Override
        public ValidationResult validate(MockObject obj, Account currentAccountState) {
            List<String> errors = new LinkedList<>();
            if (isNull(obj.field1)) {
                errors.add(ERRO_FIELD_1_NULL_KEY);
            }
            if (isNull(obj.field2)) {
                errors.add(ERRO_FIELD_2_NULL_KEY);
            }
            return ValidationResult.of(errors);
        }

        @Override
        public int getOrder() {
            return 1;
        }
    }
}
