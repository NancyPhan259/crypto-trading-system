package com.aquariux.crypto_trading_system.respository;

import com.aquariux.crypto_trading_system.model.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByUserIdAndSymbol(String userId, String symbol);
    List<Asset> findAllByUserId(String userId);
}
