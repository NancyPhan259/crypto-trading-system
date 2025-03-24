package com.aquariux.crypto_trading_system.entity;

import java.math.BigDecimal;
import java.util.UUID;

public class Asset {
    private final UUID userId;
    private final String cryptoType;
    private final BigDecimal balance;
    private final int version;

    public Asset(UUID userId, String cryptoType, BigDecimal balance, int version) {
        this.userId = userId;
        this.cryptoType = cryptoType;
        this.balance = balance;
        this.version = version;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getCryptoType() {
        return cryptoType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public int getVersion() {
        return version;
    }
}
