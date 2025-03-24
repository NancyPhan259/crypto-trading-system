package com.aquariux.crypto_trading_system.entity;

import com.aquariux.crypto_trading_system.util.validation.Sortable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Trade {
    private final UUID id;

    private final String userId;

    @Sortable
    private final String cryptoPair;

    @Sortable
    private final String tradeType;

    @Sortable
    private final BigDecimal tradeAmount;

    @Sortable
    private final BigDecimal tradePrice;

    @Sortable
    private final ZonedDateTime tradeTimestamp;
}
