package com.aquariux.crypto_trading_system.respository;

import com.aquariux.crypto_trading_system.model.entity.AssetHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssetHistoryRepository extends JpaRepository<AssetHistory, String> {
    List<AssetHistory> findByUserIdOrderByTimestampDesc(String userId);
}
