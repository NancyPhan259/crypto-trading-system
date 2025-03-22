package com.aquariux.crypto_trading_system.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class Price implements Serializable {
    private String id;
    private String pair;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private ZonedDateTime timestamp;

    public Price(String cryptoPair, BigDecimal bidPrice, BigDecimal askPrice, ZonedDateTime timestamp) {
        this.pair = cryptoPair.toUpperCase();
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getCryptoPair() {
        return pair;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Price[" +
                "cryptoPair='" + pair + '\'' +
                ", bidPrice=" + bidPrice +
                ", askPrice=" + askPrice +
                ']';
    }
}
