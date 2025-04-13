package com.aquariux.crypto_trading_system.service.impl;

import com.aquariux.crypto_trading_system.model.entity.Price;
import com.aquariux.crypto_trading_system.respository.PriceRepository;
import com.aquariux.crypto_trading_system.service.PriceCacheService;
import com.aquariux.crypto_trading_system.service.PriceService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final PriceCacheService priceCacheService;

    public PriceServiceImpl(PriceRepository priceRepository, PriceCacheService priceCacheService) {
        this.priceRepository = priceRepository;
        this.priceCacheService = priceCacheService;
    }

    @Override
    public Map<String, Price> getPrices() {
        return priceRepository.findAll().stream()
                .collect(Collectors.toMap(Price::getCryptoPair, p -> p, (p1, p2) -> p1));
    }

    @Override
    public Optional<Price> getPrice(String cryptoPair) {
        Optional<Price> cachedPrice = priceCacheService.getCachedPrice(cryptoPair);
        if (cachedPrice.isPresent()) {
            return cachedPrice;
        }
        return priceRepository.findTopByCryptoPairOrderByTimestampDesc(cryptoPair.toUpperCase());
    }
}
