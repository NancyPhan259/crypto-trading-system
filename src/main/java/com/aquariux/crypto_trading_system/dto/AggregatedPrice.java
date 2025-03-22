package com.aquariux.crypto_trading_system.dto;

import java.math.BigDecimal;

public class AggregatedPrice {
    private final String symbol;
    private final BigDecimal bestBid;
    private final BigDecimal bestAsk;

    public AggregatedPrice(String symbol, BigDecimal bestBid, BigDecimal bestAsk) {
        this.symbol = symbol;
        this.bestBid = bestBid;
        this.bestAsk = bestAsk;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getBestBid() {
        return bestBid;
    }

    public BigDecimal getBestAsk() {
        return bestAsk;
    }
}
