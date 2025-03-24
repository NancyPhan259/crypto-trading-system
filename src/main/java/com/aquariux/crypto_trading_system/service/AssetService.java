package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.model.entity.Asset;
import java.math.BigDecimal;
import java.util.List;

public interface AssetService {
    BigDecimal getUserAssetBalance(String userId, String symbol);
    void updateAsset(String userId, String symbol, BigDecimal amountChange);
    List<Asset> getAllAssetsForUser(String userId);
}