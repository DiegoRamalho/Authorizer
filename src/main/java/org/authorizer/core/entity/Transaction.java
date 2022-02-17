package org.authorizer.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class Transaction {
    private final String merchant;
    private final BigDecimal amount;
    private final LocalDateTime time;
}
