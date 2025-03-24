package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.model.entity.Price;

import java.util.Optional;

public interface PriceCacheService {
    void cachePrice(String cryptoPair, Price price);
    Optional<Price> getCachedPrice(String cryptoPair);
}