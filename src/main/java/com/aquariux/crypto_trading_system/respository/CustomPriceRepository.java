package com.aquariux.crypto_trading_system.respository;

import com.aquariux.crypto_trading_system.dto.AggregatedPrice;
import com.aquariux.crypto_trading_system.model.entity.Price;

import java.util.Collection;
import java.util.List;

public interface CustomPriceRepository {
    List<Price> saveAggregatedPrices(Collection<AggregatedPrice> prices);
}

