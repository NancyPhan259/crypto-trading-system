package com.aquariux.crypto_trading_system.respository.spec;

import com.aquariux.crypto_trading_system.entity.Asset;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface AssetRepository {
    Asset getAsset(UUID userId, String cryptoType);

    Map<String, Asset> getAssets(UUID userId, List<String> coins);

    @Transactional
    void updateAssets(Collection<Asset> assets);
}
