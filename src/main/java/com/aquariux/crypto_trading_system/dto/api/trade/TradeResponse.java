package com.aquariux.crypto_trading_system.dto.api.trade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class TradeResponse {
    @Getter
    private UUID tradeId;

    @Getter
    private String userId;

    @Setter
    @Getter
    private String cryptoPair;

    @Setter
    @Getter
    private String tradeType;

    @Setter
    @Getter
    private BigDecimal tradeAmount;

    @Setter
    @Getter
    private BigDecimal tradePrice;

    @Setter
    @Getter
    private ZonedDateTime tradeTimestamp;

    @Setter
    @Getter
    private String message;

}
