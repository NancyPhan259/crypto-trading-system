package com.aquariux.crypto_trading_system.respository.spec;

import com.aquariux.crypto_trading_system.dto.AggregatedPrice;
import com.aquariux.crypto_trading_system.model.entity.Price;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface  PriceRepository {
    List<Price> saveAggregatedPrices(Collection<AggregatedPrice> cryptoPairToAggregatedPrice);

}
