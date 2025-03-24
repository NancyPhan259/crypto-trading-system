package com.aquariux.crypto_trading_system.respository.spec;

import com.aquariux.crypto_trading_system.dto.AggregatedPrice;
import com.aquariux.crypto_trading_system.entity.Price;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface  PriceRepository {
    List<Price> saveAggregatedPrices(Collection<AggregatedPrice> cryptoPairToAggregatedPrice);

    Optional<Price> findPrice(String cryptoPair);

}
