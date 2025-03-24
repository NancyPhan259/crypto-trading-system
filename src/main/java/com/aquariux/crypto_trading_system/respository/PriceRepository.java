package com.aquariux.crypto_trading_system.respository;

import com.aquariux.crypto_trading_system.model.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long>, CustomPriceRepository {
    Optional<Price> findTopByCryptoPairOrderByTimestampDesc(String cryptoPair);
    Optional<Price> findByCryptoPair(String cryptoPair);
}