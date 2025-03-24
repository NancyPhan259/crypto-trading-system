package com.aquariux.crypto_trading_system;

import com.aquariux.crypto_trading_system.dto.AggregatedPrice;
import com.aquariux.crypto_trading_system.dto.BinancePrice;
import com.aquariux.crypto_trading_system.dto.HoubiPrice;
import com.aquariux.crypto_trading_system.model.entity.Price;
import com.aquariux.crypto_trading_system.respository.PriceRepository;
import com.aquariux.crypto_trading_system.service.PriceAggregationService;
import com.aquariux.crypto_trading_system.service.PriceCacheServiceImpl;
import com.aquariux.crypto_trading_system.service.PriceFetchingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ScheduleUpdatePriceServiceTest {

    private PriceFetchingService fetchingService;
    private PriceAggregationService aggregationService;
    private PriceRepository priceRepository;
    private PriceCacheServiceImpl priceCacheService;
//    private ScheduleUpdatePriceService scheduler;

    @BeforeEach
    void setUp() {
        fetchingService = mock(PriceFetchingService.class);
        aggregationService = mock(PriceAggregationService.class);
        priceRepository = mock(PriceRepository.class);
        priceCacheService = mock(PriceCacheServiceImpl.class);

//        scheduler = new ScheduleUpdatePriceService(fetchingService, aggregationService, priceRepository, priceCacheService);
    }

    @Test
    void shouldFetchAggregateAndSavePrices() {
        BinancePrice binancePrice = new BinancePrice("BTCUSDT", new BigDecimal("100.00"), new BigDecimal("105.00"));
        HoubiPrice.HoubiTicker houbiPrice = new HoubiPrice.HoubiTicker("BTCUSDT", new BigDecimal("101.00"), new BigDecimal("104.00"));

        when(fetchingService.fetchBinancePrices()).thenReturn(new BinancePrice[] { binancePrice });
        when(fetchingService.fetchHuobiPrices()).thenReturn(new HoubiPrice.HoubiTicker[] { houbiPrice });

        AggregatedPrice aggPrice = new AggregatedPrice("BTCUSDT", new BigDecimal("101.00"), new BigDecimal("104.00"));
        when(aggregationService.getBestPrices(any(), any())).thenReturn(Map.of("BTCUSDT", aggPrice));

        Price priceEntity = new Price("BTCUSDT", aggPrice.getBestBid(), aggPrice.getBestAsk(), ZonedDateTime.now());
        when(priceRepository.saveAggregatedPrices(any())).thenReturn(List.of(priceEntity));

//        scheduler.scheduleUpdateBestPrice();

        verify(fetchingService).fetchBinancePrices();
        verify(fetchingService).fetchHuobiPrices();
        verify(aggregationService).getBestPrices(any(), any());
        verify(priceRepository).saveAggregatedPrices(any());
        verify(priceCacheService).cachePrice(eq("BTCUSDT"), any(Price.class));
    }
}
