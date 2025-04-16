package com.aquariux.crypto_trading_system.service.impl;

import com.aquariux.crypto_trading_system.model.entity.Asset;
import com.aquariux.crypto_trading_system.respository.AssetRepository;
import com.aquariux.crypto_trading_system.service.AssetService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<Asset> assets =  assetRepository.findAllByUserId(userId);

         return assets.stream()
        .collect(Collectors.groupingBy(
            Asset::getSymbol,
            Collectors.reducing(
                (a1, a2) -> {
                    a1.setBalance(a1.getBalance().add(a2.getBalance()));
                    return a1;
                }
            )
        ))
        .values().stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
    }
}

