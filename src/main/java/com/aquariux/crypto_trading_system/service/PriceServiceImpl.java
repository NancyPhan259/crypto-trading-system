package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.dto.AggregatedPrice;
import com.aquariux.crypto_trading_system.dto.BinancePrice;
import com.aquariux.crypto_trading_system.dto.HoubiPrice;
import com.aquariux.crypto_trading_system.model.entity.Price;
import com.aquariux.crypto_trading_system.respository.spec.PriceRepository;
import com.aquariux.crypto_trading_system.service.contract.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PriceServiceImpl implements PriceService {
     private static final Logger LOG = LoggerFactory.getLogger(PriceServiceImpl.class);

     private final RestTemplate restTemplate;
     private final PriceRepository priceRepository;

    public PriceServiceImpl(RestTemplate restTemplate, PriceRepository priceRepository) {
        this.restTemplate = restTemplate;
        this.priceRepository = priceRepository;
    }

    @Override
    public Optional<Price> getPrice(String cryptoPair) {
        return Optional.empty();
    }

    public Map<String, AggregatedPrice> getBestPrices(BinancePrice[] binancePrices, HoubiPrice.HoubiTicker[] houbiPrices) {

        Map<String, AggregatedPrice> bestPrices = new HashMap<>();

        Map<String, BinancePrice> binancePriceMap = new HashMap<>();
        for (BinancePrice binancePrice : binancePrices) {
            binancePriceMap.put(binancePrice.getSymbol().toUpperCase(), binancePrice);
        }

        for (HoubiPrice.HoubiTicker houbiPrice : houbiPrices) {

            String symbol = houbiPrice.getSymbol().toUpperCase();

            BinancePrice binancePrice = binancePriceMap.get(symbol);

            if (binancePrice == null) {
                LOG.error("Binance does not contain the corresponding crypto symbol");
                continue;
            }

            BigDecimal bestBid = binancePrice.getBidPrice().max(houbiPrice.getBid());

            BigDecimal bestAsk = binancePrice.getBidPrice().min(houbiPrice.getBid());

            bestPrices.put(symbol, new AggregatedPrice(symbol, bestBid, bestAsk));
        }

        return bestPrices;
    }

    public BinancePrice[] fetchBinancePrices() {
        try {
            String binanceUrl = "https://api.binance.com/api/v3/ticker/bookTicker";
            ResponseEntity<BinancePrice[]> response = restTemplate.getForEntity(binanceUrl, BinancePrice[].class);

            if (response.getBody() == null) {
                LOG.error("Failed to fetch prices from Binance: the body is null");
                return new BinancePrice[0];
            }
            return response.getBody();
        } catch (Exception e) {
            LOG.error("Failed to fetch prices from Binance: " + e.getMessage());
            return new BinancePrice[0];
        }
    }

    public HoubiPrice.HoubiTicker[] fetchHuobiPrices() {
        try {
            String huobiUrl = "https://api.huobi.pro/market/tickers";
            ResponseEntity<HoubiPrice> response = restTemplate.getForEntity(huobiUrl, HoubiPrice.class);
            if (response.getBody() == null) {
                LOG.error("Failed to fetch prices from Houbi: the body is null");
                return new HoubiPrice.HoubiTicker[0];
            }
            return response.getBody().getData();

        } catch (Exception e) {
            LOG.error("Failed to fetch prices from Huobi: " + e.getMessage());
            return new HoubiPrice.HoubiTicker[0];
        }
    }


}
