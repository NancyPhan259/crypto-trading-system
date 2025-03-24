package com.aquariux.crypto_trading_system.service;
import com.aquariux.crypto_trading_system.dto.AggregatedPrice;
import com.aquariux.crypto_trading_system.dto.BinancePrice;
import com.aquariux.crypto_trading_system.dto.HoubiPrice;
import com.aquariux.crypto_trading_system.model.entity.Price;
import com.aquariux.crypto_trading_system.respository.PriceRepository;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ScheduleUpdatePriceServiceImpl {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleUpdatePriceServiceImpl.class);

    private final PriceFetchingService priceFetchingService;
    private final PriceAggregationService priceAggregationService;
    private final PriceRepository priceRepository;
    private final PriceCacheServiceImpl priceCacheService;

    public ScheduleUpdatePriceServiceImpl(
            PriceFetchingService priceFetchingService,
            PriceAggregationService priceAggregationService,
            PriceRepository priceRepository,
            PriceCacheServiceImpl priceCacheService
    ) {
        this.priceFetchingService = priceFetchingService;
        this.priceAggregationService = priceAggregationService;
        this.priceRepository = priceRepository;
        this.priceCacheService = priceCacheService;
    }

    @Scheduled(fixedRate = 10_000)
    @SchedulerLock(name = "updatePrice", lockAtMostFor = "15s", lockAtLeastFor = "10s")
    public void scheduleUpdateBestPrice() {
        BinancePrice[] binancePrices = priceFetchingService.fetchBinancePrices();
        HoubiPrice.HoubiTicker[] huobiPrices = priceFetchingService.fetchHuobiPrices();

        Map<String, AggregatedPrice> bestPrices = priceAggregationService.getBestPrices(binancePrices, huobiPrices);

        LOG.info("Best prices calculated: {}", bestPrices.keySet());
        List<Price> prices = priceRepository.saveAggregatedPrices(bestPrices.values());
        LOG.info("Saved {} prices to DB", prices.size());
        prices.forEach(price -> priceCacheService.cachePrice(price.getCryptoPair(), price));

        LOG.info("Binance prices received: {}", binancePrices.length);
        LOG.info("Huobi prices received: {}", huobiPrices.length);
        LOG.info("Best prices calculated: {}", bestPrices.keySet());
        LOG.info("Prices updated and cached: {}", prices);
    }
}



