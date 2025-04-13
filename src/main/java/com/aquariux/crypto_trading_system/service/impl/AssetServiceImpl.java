package com.aquariux.crypto_trading_system.service.impl;

import com.aquariux.crypto_trading_system.model.entity.Asset;
import com.aquariux.crypto_trading_system.respository.AssetRepository;
import com.aquariux.crypto_trading_system.service.AssetService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public BigDecimal getUserAssetBalance(String userId, String symbol) {
        return assetRepository.findByUserIdAndSymbol(userId, symbol)
                .map(Asset::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public void updateAsset(String userId, String symbol, BigDecimal amountChange) {
        Asset asset = assetRepository.findByUserIdAndSymbol(userId, symbol)
                .orElse(new Asset(userId, symbol, BigDecimal.ZERO));
        asset.setBalance(asset.getBalance().add(amountChange));
        assetRepository.save(asset);
    }

    @Override
    public List<Asset> getAllAssetsForUser(String userId) {
        return assetRepository.findAllByUserId(userId);
    }
}

