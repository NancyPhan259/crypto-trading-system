package com.aquariux.crypto_trading_system.dto;


import com.aquariux.crypto_trading_system.model.TradeSide;

import java.math.BigDecimal;

public class TradeRequest {
    private String userId;
    private String cryptoPair;
    private BigDecimal quantity;
    private TradeSide side;

    public String getUserId() {
        return userId;
    }

    public String getCryptoPair() {
        return cryptoPair;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public TradeSide getSide() {
        return side;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCryptoPair(String cryptoPair) {
        this.cryptoPair = cryptoPair;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setSide(TradeSide side) {
        this.side = side;
    }
}
