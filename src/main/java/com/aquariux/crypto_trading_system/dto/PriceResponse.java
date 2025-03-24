package com.aquariux.crypto_trading_system.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for exposing price data to clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceResponse {
    private String cryptoPair;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private ZonedDateTime timestamp;
}

