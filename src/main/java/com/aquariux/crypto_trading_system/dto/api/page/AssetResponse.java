package com.aquariux.crypto_trading_system.dto.api.page;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class AssetResponse {

    private UUID userId;

    private List<AssetBalance> assetBalances;

    public AssetResponse() {

    }

    public AssetResponse(UUID userId, List<AssetBalance> assetBalances) {
        this.userId = userId;
        this.assetBalances = assetBalances;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<AssetBalance> getAssetBalances() {
        return assetBalances;
    }

    public void setAssetBalances(List<AssetBalance> assetBalances) {
        this.assetBalances = assetBalances;
    }

    public static class AssetBalance {
        private String cryptoType;
        private BigDecimal balance;

        public AssetBalance() {

        }

        public AssetBalance(String cryptoType, BigDecimal balance) {
            this.cryptoType = cryptoType;
            this.balance = balance;
        }

        public String getCryptoType() {
            return cryptoType;
        }

        public void setCryptoType(String cryptoType) {
            this.cryptoType = cryptoType;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
    }

}
