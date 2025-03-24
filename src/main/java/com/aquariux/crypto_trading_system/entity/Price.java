package com.aquariux.crypto_trading_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Price implements Serializable {
    private int id;
    @Getter
    private String cryptoPair;
    @Getter
    private  BigDecimal bidPrice;
    @Getter
    private  BigDecimal askPrice;
    @Getter
    private  ZonedDateTime timestamp;

    public Price(String cryptoPair, BigDecimal bidPrice, BigDecimal askPrice, ZonedDateTime timestamp) {
        this.cryptoPair = cryptoPair.toUpperCase();
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Price[" +
                "cryptoPair='" + cryptoPair + '\'' +
                ", bidPrice=" + bidPrice +
                ", askPrice=" + askPrice +
                ']';
    }
}
