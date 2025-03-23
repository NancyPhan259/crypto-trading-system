package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.dto.AggregatedPrice;
import com.aquariux.crypto_trading_system.dto.BinancePrice;
import com.aquariux.crypto_trading_system.dto.HoubiPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceAggregationService {
    private static final Logger LOG = LoggerFactory.getLogger(PriceAggregationService.class);

    public Map<String, AggregatedPrice> getBestPrices(BinancePrice[] binancePrices, HoubiPrice.HoubiTicker[] houbiPrices) {
        Map<String, AggregatedPrice> bestPrices = new HashMap<>();
        Map<String, BinancePrice> binancePriceMap = Arrays.stream(binancePrices)
                .collect(Collectors.toMap(b -> b.getSymbol().toUpperCase(), b -> b));

        for (HoubiPrice.HoubiTicker houbiPrice : houbiPrices) {
            String symbol = houbiPrice.getSymbol().toUpperCase();
            BinancePrice binancePrice = binancePriceMap.get(symbol);

            if (binancePrice == null) {
                LOG.error("Binance does not contain the corresponding crypto symbol");
                continue;
            }

            BigDecimal bestBid = binancePrice.getBidPrice().max(houbiPrice.getBid());
            BigDecimal bestAsk = binancePrice.getAskPrice().min(houbiPrice.getAsk());

            bestPrices.put(symbol, new AggregatedPrice(symbol, bestBid, bestAsk));
        }
        return bestPrices;
    }
}

