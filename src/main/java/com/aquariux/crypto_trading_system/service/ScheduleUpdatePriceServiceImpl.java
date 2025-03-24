package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.dto.AggregatedPrice;
import com.aquariux.crypto_trading_system.dto.BinancePrice;
import com.aquariux.crypto_trading_system.dto.HoubiPrice;
import com.aquariux.crypto_trading_system.entity.Price;
import com.aquariux.crypto_trading_system.respository.spec.PriceRepository;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.aquariux.crypto_trading_system.util.validation.CryptoPairValidator.VALID_CRYPTO_PAIRS;

@Service
public class ScheduleUpdatePriceServiceImpl {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleUpdatePriceServiceImpl.class);

    private final PriceRepository priceRepository;
    private final PriceFetchingService priceFetchingService;
    private final PriceAggregationService priceAggregationService;
    private final PriceCacheServiceImpl priceCacheServiceImpl;


    public ScheduleUpdatePriceServiceImpl(PriceRepository priceRepository,
                                          PriceFetchingService priceFetchingService,
                                          PriceAggregationService priceAggregationService,
                                          PriceCacheServiceImpl priceCacheServiceImpl) {
        this.priceRepository = priceRepository;
        this.priceFetchingService = priceFetchingService;
        this.priceAggregationService = priceAggregationService;
        this.priceCacheServiceImpl = priceCacheServiceImpl;
    }

    @Scheduled(fixedRate = 10000)
    @SchedulerLock(name = "updatePrice", lockAtMostFor = "15s", lockAtLeastFor = "10s")
    public void scheduleUpdateBestPrice() {
        BinancePrice[] binancePrices = priceFetchingService.fetchBinancePrices();
        BinancePrice[] filteredBinancePrices = Arrays.stream(binancePrices)
                .filter(price -> VALID_CRYPTO_PAIRS.contains(price.getSymbol().toUpperCase()))
                .toArray(BinancePrice[]::new);

        HoubiPrice.HoubiTicker[] houbiPrices = priceFetchingService.fetchHuobiPrices();
        HoubiPrice.HoubiTicker[] filteredHoubiPrice = Arrays.stream(houbiPrices)
                .filter(price -> VALID_CRYPTO_PAIRS.contains(price.getSymbol().toUpperCase()))
                .toArray(HoubiPrice.HoubiTicker[]::new);

        Map<String, AggregatedPrice> bestPrices = priceAggregationService.getBestPrices(filteredBinancePrices, filteredHoubiPrice);
        List<Price> prices = priceRepository.saveAggregatedPrices(bestPrices.values());
        prices.forEach(price -> priceCacheServiceImpl.cachePrice(price.getCryptoPair(), price));

        LOG.info("Updated prices: {}", prices);
    }
}
