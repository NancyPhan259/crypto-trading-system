package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.dto.BinancePrice;
import com.aquariux.crypto_trading_system.dto.HoubiPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PriceFetchingService {
    private static final Logger LOG = LoggerFactory.getLogger(PriceFetchingService.class);

    private final RestTemplate restTemplate;

    public PriceFetchingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BinancePrice[] fetchBinancePrices() {
        try {
            String binanceUrl = "https://api.binance.com/api/v3/ticker/bookTicker";
            ResponseEntity<BinancePrice[]> response = restTemplate.getForEntity(binanceUrl, BinancePrice[].class);
            return response.getBody() != null ? response.getBody() : new BinancePrice[0];
        } catch (Exception e) {
            LOG.error("Failed to fetch prices from Binance: {}", e.getMessage());
            return new BinancePrice[0];
        }
    }

    public HoubiPrice.HoubiTicker[] fetchHuobiPrices() {
        try {
            String huobiUrl = "https://api.huobi.pro/market/tickers";
            ResponseEntity<HoubiPrice> response = restTemplate.getForEntity(huobiUrl, HoubiPrice.class);
            return response.getBody() != null ? response.getBody().getData() : new HoubiPrice.HoubiTicker[0];
        } catch (Exception e) {
            LOG.error("Failed to fetch prices from Huobi: {}", e.getMessage());
            return new HoubiPrice.HoubiTicker[0];
        }
    }
}

