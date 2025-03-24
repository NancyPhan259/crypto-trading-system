package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.entity.Asset;
import com.aquariux.crypto_trading_system.respository.spec.AssetRepository;
import com.aquariux.crypto_trading_system.service.contract.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.aquariux.crypto_trading_system.util.validation.CryptoPairValidator.VALID_CRYPTO_QUOTES;

@Service
public class AssestServiceImpl implements AssetService {

    private AssetRepository assetRepository;


    @Autowired
    public AssestServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }


    @Override
    public Collection<Asset> getAssets(UUID userId) {
        Map<String, Asset> assets = assetRepository.getAssets(userId, new LinkedList<>(VALID_CRYPTO_QUOTES));
        return assets.values();
    }
}
