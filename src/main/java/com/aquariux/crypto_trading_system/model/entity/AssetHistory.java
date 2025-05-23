package com.aquariux.crypto_trading_system.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "asset_history")
public class AssetHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String userId;

    private String symbol;

    private BigDecimal balance;

    private ZonedDateTime timestamp;

    public AssetHistory() {
    }

    public AssetHistory(String userId, String symbol, BigDecimal balance, ZonedDateTime timestamp) {
        this.userId = userId;
        this.symbol = symbol;
        this.balance = balance;
        this.timestamp = timestamp;
    }
}
