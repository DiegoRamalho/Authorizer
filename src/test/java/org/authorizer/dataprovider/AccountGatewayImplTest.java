package org.authorizer.dataprovider;

import org.authorizer.BaseMockitoTest;
import org.authorizer.core.entity.Account;
import org.authorizer.dataprovider.repository.AccountRepository;
import org.authorizer.builder.AccountBuilder;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tags({@Tag("account"), @Tag("gateway")})
class AccountGatewayImplTest extends BaseMockitoTest {

    @Mock
    private AccountRepository repository;
    @InjectMocks
    private AccountGatewayImpl target;


    @Test
    void shouldReturnNothingWhenAccountIdIsNullAndDatabaseIsEmpty() {
        // Given
        when(repository.findFirst()).thenReturn(Optional.empty());
        // When
        var result = target.findByIdOrFirst(null);
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnNothingWhenAccountIdIsNotNullAndDatabaseIsEmpty() {
        // Given
        Account account = AccountBuilder.activeCardWithNoLimit();
        when(repository.findFirst()).thenReturn(Optional.empty());
        // When
        var result = target.findByIdOrFirst(account.getId());
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAccountWhenAccountIdIsNullAndDatabaseIsNotEmpty() {
        // Given
        Account account = AccountBuilder.activeCardWithNoLimit();
        when(repository.findFirst()).thenReturn(Optional.of(account));
        // When
        var result = target.findByIdOrFirst(null);
        // Then
        assertNotNull(result);
        assertTrue(result.isPresent());

        assertNotNull(result.get().getId());
        assertEquals(account.getAvailableLimit(), result.get().getAvailableLimit());
        assertEquals(account.getActiveCard(), result.get().getActiveCard());
    }

    @Test
    void shouldReturnAccountWhenAccountIdIsPresent() {
        // Given
        Account account = AccountBuilder.inactiveCardWithNoLimit();
        when(repository.findById(account.getId())).thenReturn(Optional.of(account));
        // When
        var result = target.findByIdOrFirst(account.getId());
        // Then
        assertNotNull(result);
        assertTrue(result.isPresent());

        assertNotNull(result.get().getId());
        assertEquals(account.getAvailableLimit(), result.get().getAvailableLimit());
        assertEquals(account.getActiveCard(), result.get().getActiveCard());
    }

    @Test
    void shouldSaveAccount() {
        // Given
        Account account = AccountBuilder.activeCardWithNoLimit();
        when(repository.save(account)).thenReturn(account);
        // When
        var result = target.save(account);
        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(account.getAvailableLimit(), result.getAvailableLimit());
        assertEquals(account.getActiveCard(), result.getActiveCard());
    }
}
