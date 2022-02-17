package org.authorizer.dataprovider.repository.impl;


import org.authorizer.BaseMockitoTest;
import org.authorizer.core.entity.Account;
import org.authorizer.builder.AccountBuilder;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;

@Tags({@Tag("account"), @Tag("repository")})
class MemoryAccountRepositoryImplTest extends BaseMockitoTest {

    @InjectMocks
    private MemoryAccountRepositoryImpl target;

    @Test
    void shouldReturnNothingWhenDatabaseIsEmpty() {
        // When
        var result = target.findFirst();
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnFirstAccountWhenDatabaseIsNotEmpty() {
        // Given
        Account first = target.save(AccountBuilder.activeCardWithNoLimit());
        Account second = target.save(AccountBuilder.inactiveCardWithNoLimit());
        // When
        var result = target.findFirst();
        // Then
        assertNotNull(result);
        assertTrue(result.isPresent());

        assertNotNull(result.get().getId());
        assertEquals(first.getAvailableLimit(), result.get().getAvailableLimit());
        assertEquals(first.getActiveCard(), result.get().getActiveCard());
    }

    @Test
    void shouldReturnAccountByIdWhenDatabaseIsNotEmpty() {
        // Given
        Account first = target.save(AccountBuilder.activeCardWithNoLimit());
        Account second = target.save(AccountBuilder.inactiveCardWithNoLimit());
        // When
        var result = target.findById(second.getId());
        // Then
        assertNotNull(result);
        assertTrue(result.isPresent());

        assertNotNull(result.get().getId());
        assertEquals(second.getAvailableLimit(), result.get().getAvailableLimit());
        assertEquals(second.getActiveCard(), result.get().getActiveCard());
    }

    @Test
    void shouldSaveAccount() {
        // Given
        Account account = AccountBuilder.activeCardWithNoLimit();
        // When
        var result = target.save(account);
        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(account.getAvailableLimit(), result.getAvailableLimit());
        assertEquals(account.getActiveCard(), result.getActiveCard());
    }
}
