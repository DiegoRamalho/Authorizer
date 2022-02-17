package org.authorizer.core.usecase.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
public class CreateAccountCommand implements Command {
    private final Boolean activeCard;
    private final BigDecimal availableLimit;
}
