package org.authorizer.core.usecase.dto;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class ValidationResult {
    private final boolean valid;
    private final List<String> violations;

    private ValidationResult(boolean valid, List<String> violations) {
        this.valid = valid;
        this.violations = violations;
    }

    public static ValidationResult of(List<String> violations) {
        return new ValidationResult(violations.isEmpty(), violations);
    }

    public static ValidationResult validOrWithError(boolean valid, String... errors) {
        return valid ? ValidationResult.ok() : ValidationResult.error(errors);
    }

    public static ValidationResult ok() {
        return new ValidationResult(true, Collections.emptyList());
    }

    public static ValidationResult error(String... violation) {
        return errors(Arrays.asList(violation));
    }

    public static ValidationResult errors(List<String> violations) {
        return new ValidationResult(false, violations);
    }
}
