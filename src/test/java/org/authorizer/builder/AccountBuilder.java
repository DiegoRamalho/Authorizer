package org.authorizer.builder;

import org.authorizer.core.entity.Account;
import org.junit.jupiter.params.provider.Arguments;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.authorizer.builder.TransactionBuilder.fromMerchant1With;
import static org.authorizer.builder.TransactionBuilder.fromMerchant2With;

public class AccountBuilder {

    public static Account with(Boolean activeCard, BigDecimal availableLimit) {
        return new Account(activeCard, availableLimit);
    }

    public static Account inactiveCardWithNoLimit() {
        return inactiveCardWith(BigDecimal.ZERO);
    }

    public static Account inactiveCardWith(BigDecimal value) {
        return with(false, value);
    }

    public static Account activeCardWithNoLimit() {
        return activeCardWith(BigDecimal.ZERO);
    }

    public static Account activeCardWith(BigDecimal value) {
        return with(true, value);
    }

    public static Account activeCardWithLimitOfTen() {
        return activeCardWith(BigDecimal.TEN);
    }

    public static Account inactiveCardWithLimitOfTen() {
        return inactiveCardWith(BigDecimal.TEN);
    }

    public static Stream<Arguments> genericAccounts() {
        return Stream.of(
                Arguments.of(inactiveCardWithNoLimit()),
                Arguments.of(inactiveCardWithLimitOfTen()),
                Arguments.of(activeCardWithLimitOfTen()),
                Arguments.of(activeCardWithNoLimit())
        );
    }

    public static Account activeAccountWithTransactionsFromMerchant1At(LocalDateTime... times) {
        Account account = activeCardWithLimitOfTen();
        for (LocalDateTime time : times) {
            account.addTransaction(fromMerchant1With(BigDecimal.ONE, time));
        }
        return account;
    }

    public static Account activeAccountWithTransactionsFromMerchant2At(LocalDateTime... times) {
        Account account = activeCardWithLimitOfTen();
        for (LocalDateTime time : times) {
            account.addTransaction(fromMerchant1With(BigDecimal.ONE, time));
        }
        return account;
    }

    public static Account activeAccountWithTransactionsFromRandomMerchantAt(LocalDateTime... times) {
        Account account = activeCardWithLimitOfTen();
        for (int index = 0; index < times.length; index++) {
            if (index % 2 == 0) account.addTransaction(fromMerchant1With(BigDecimal.ONE, times[index]));
            else account.addTransaction(fromMerchant2With(BigDecimal.ONE, times[index]));
        }
        return account;
    }
}
