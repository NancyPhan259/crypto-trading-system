package com.aquariux.crypto_trading_system.dto.api.trade;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class TradeHistoryResponse {

    private UUID id;
    private String userId;
    private String cryptoPair;
    private String tradeType;
    private BigDecimal tradeAmount;
    private BigDecimal tradePrice;
    private ZonedDateTime tradeTimestamp;

    public TradeHistoryResponse() {

    }

    public TradeHistoryResponse(UUID id, String userId, String cryptoPair, String tradeType, BigDecimal tradeAmount, BigDecimal tradePrice, ZonedDateTime tradeTimestamp) {
        this.id = id;
        this.userId = userId;
        this.cryptoPair = cryptoPair;
        this.tradeType = tradeType;
        this.tradeAmount = tradeAmount;
        this.tradePrice = tradePrice;
        this.tradeTimestamp = tradeTimestamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCryptoPair() {
        return cryptoPair;
    }

    public void setCryptoPair(String cryptoPair) {
        this.cryptoPair = cryptoPair;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(BigDecimal tradePrice) {
        this.tradePrice = tradePrice;
    }

    public ZonedDateTime getTradeTimestamp() {
        return tradeTimestamp;
    }

    public void setTradeTimestamp(ZonedDateTime tradeTimestamp) {
        this.tradeTimestamp = tradeTimestamp;
    }
}

