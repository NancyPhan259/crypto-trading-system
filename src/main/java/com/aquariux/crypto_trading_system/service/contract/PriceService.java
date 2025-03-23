package com.aquariux.crypto_trading_system.service.contract;

import com.aquariux.crypto_trading_system.model.entity.Price;

import java.util.Map;
import java.util.Optional;

    public interface PriceService {
        Map<String, Price> getPrices();

        Optional<Price> getPrice(String cryptoPair);
}
