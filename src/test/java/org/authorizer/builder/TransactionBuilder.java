package org.authorizer.builder;

import org.authorizer.core.entity.Transaction;
import org.junit.jupiter.params.provider.Arguments;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionBuilder {

    public static final String MERCHANT_1 = "M1";
    public static final String MERCHANT_2 = "M2";

    // 2021-01-01 01:00:00
    public static final LocalDateTime DEFAULT_TIME = LocalDateTime.of(2021, 1, 1, 1, 0, 0);
    // 2021-01-01 01:01:00
    public static final LocalDateTime NEXT_ONE_MINUTE_TIME = DEFAULT_TIME.plusMinutes(1);
    // 2021-01-01 01:02:00
    public static final LocalDateTime NEXT_TWO_MINUTE_TIME = DEFAULT_TIME.plusMinutes(2);

    public static Transaction with(String merchant, BigDecimal amount, LocalDateTime time) {
        return new Transaction(merchant, amount, time);
    }

    public static Transaction fromMerchant1With(BigDecimal amount) {
        return fromMerchant1With(amount, DEFAULT_TIME);
    }

    public static Transaction fromMerchant1With(BigDecimal amount, LocalDateTime time) {
        return with(MERCHANT_1, amount, time);
    }

    public static Transaction fromMerchant2With(BigDecimal amount, LocalDateTime time) {
        return with(MERCHANT_2, amount, time);
    }

    public static List<Transaction> transactionsFromAnyMerchantWithin2MinutesInterval() {
        return Arrays.asList(
                fromMerchant1With(BigDecimal.ONE),
                fromMerchant2With(BigDecimal.ONE, NEXT_ONE_MINUTE_TIME),
                fromMerchant1With(BigDecimal.ZERO, NEXT_TWO_MINUTE_TIME)
        );
    }

    public static List<Transaction> similarTransactionsFromM1Within2MinutesInterval() {
        return Arrays.asList(
                fromMerchant1With(BigDecimal.TEN, DEFAULT_TIME),
                fromMerchant2With(BigDecimal.TEN, NEXT_ONE_MINUTE_TIME)
        );
    }

    public static Stream<Arguments> genericTransactions() {
        List<Arguments> result = new LinkedList<>();
        result.addAll(transactionsFromAnyMerchantWithin2MinutesInterval().stream().map(Arguments::of).collect(Collectors.toList()));
        result.addAll(similarTransactionsFromM1Within2MinutesInterval().stream().map(Arguments::of).collect(Collectors.toList()));
        return result.stream();
    }
}
