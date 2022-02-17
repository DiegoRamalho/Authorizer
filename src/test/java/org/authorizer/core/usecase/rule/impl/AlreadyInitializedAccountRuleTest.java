package org.authorizer.core.usecase.rule.impl;

import org.authorizer.BaseMockitoTest;
import org.authorizer.core.config.ApplicationCoreProperties;
import org.authorizer.core.entity.Account;
import org.authorizer.builder.ApplicationPropertiesBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tags({@Tag("account"), @Tag("rule")})
class AlreadyInitializedAccountRuleTest extends BaseMockitoTest {

    @Mock
    private ApplicationCoreProperties properties;
    @InjectMocks
    private AlreadyInitializedAccountRule target;

    private final String key = "ERROR-KEY";

    @BeforeEach
    public void init() {
        when(properties.getError()).thenReturn(ApplicationPropertiesBuilder.accountAlreadyInitializedKey(key));
    }

    @ParameterizedTest(name = "Account {0}.")
    @MethodSource("org.authorizer.builder.AccountBuilder#genericAccounts")
    @NullSource
    void shouldReturnOkWhenAccountIsNotInitialized(Account modification) {
        // Given
        Account currentAccountState = null;

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertTrue(result.isValid());
        assertTrue(result.getViolations().isEmpty());
    }

    @ParameterizedTest(name = "Account {0}.")
    @MethodSource("org.authorizer.builder.AccountBuilder#genericAccounts")
    void shouldReturnErrorWhenAccountIsAlreadyInitialized(Account modification) {
        // Given
        Account currentAccountState = modification;

        // When
        var result = target.validate(modification, currentAccountState);

        // Then
        assertNotNull(result);
        assertFalse(result.isValid());
        assertThat(result.getViolations(), containsInAnyOrder(key));
    }
}
