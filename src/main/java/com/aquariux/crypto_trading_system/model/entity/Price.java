package com.aquariux.crypto_trading_system.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import jakarta.persistence.*;

@Entity
public class Price implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cryptoPair;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private ZonedDateTime timestamp;

    public Price() {}

    public Price(String cryptoPair, BigDecimal bidPrice, BigDecimal askPrice, ZonedDateTime timestamp) {
        this.cryptoPair = cryptoPair.toUpperCase();
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getCryptoPair() {
        return cryptoPair;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setCryptoPair(String cryptoPair) {
        this.cryptoPair = cryptoPair;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
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