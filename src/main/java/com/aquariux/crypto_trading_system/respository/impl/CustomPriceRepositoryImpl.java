package com.aquariux.crypto_trading_system.respository.impl;
import com.aquariux.crypto_trading_system.dto.AggregatedPrice;
import com.aquariux.crypto_trading_system.model.entity.Price;
import com.aquariux.crypto_trading_system.respository.CustomPriceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class CustomPriceRepositoryImpl implements CustomPriceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Price> saveAggregatedPrices(Collection<AggregatedPrice> prices) {
        List<Price> saved = new ArrayList<>();
        for (AggregatedPrice aggregated : prices) {
            Price price = new Price(
                    aggregated.getSymbol(),
                    aggregated.getBestBid(),
                    aggregated.getBestAsk(),
                    ZonedDateTime.now()
            );
            entityManager.persist(price);
            saved.add(price);
        }
        return saved;
    }
}


