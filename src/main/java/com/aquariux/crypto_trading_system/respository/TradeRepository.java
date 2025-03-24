package com.aquariux.crypto_trading_system.respository;

import com.aquariux.crypto_trading_system.model.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByUserIdOrderByTimestampDesc(String userId);
}
