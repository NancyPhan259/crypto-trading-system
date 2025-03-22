package com.aquariux.crypto_trading_system.dto;

import java.math.BigDecimal;

public class BinancePrice {
    private String symbol;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;

    public BinancePrice(String symbol, BigDecimal bidPrice, BigDecimal askPrice) {
        this.symbol = symbol;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }
}
