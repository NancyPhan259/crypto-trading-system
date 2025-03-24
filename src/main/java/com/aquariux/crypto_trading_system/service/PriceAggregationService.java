package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.dto.AggregatedPrice;
import com.aquariux.crypto_trading_system.dto.BinancePrice;
import com.aquariux.crypto_trading_system.dto.HoubiPrice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PriceAggregationService {

    private static final Set<String> VALID_SYMBOLS = Set.of("BTCUSDT", "ETHUSDT");

    public Map<String, AggregatedPrice> getBestPrices(BinancePrice[] binancePrices, HoubiPrice.HoubiTicker[] huobiPrices) {
        Map<String, AggregatedPrice> result = new HashMap<>();

        Map<String, BinancePrice> binanceMap = Arrays.stream(binancePrices)
                .filter(p -> VALID_SYMBOLS.contains(p.getSymbol().toUpperCase()))
                .collect(Collectors.toMap(p -> p.getSymbol().toUpperCase(), p -> p));

        for (HoubiPrice.HoubiTicker huobi : huobiPrices) {
            String symbol = huobi.getSymbol().toUpperCase();
            if (!VALID_SYMBOLS.contains(symbol)) continue;

            BinancePrice binance = binanceMap.get(symbol);
            if (binance == null) continue;

            BigDecimal bestBid = binance.getBidPrice().max(huobi.getBid());
            BigDecimal bestAsk = binance.getAskPrice().min(huobi.getAsk());

            result.put(symbol, new AggregatedPrice(symbol, bestBid, bestAsk));
        }

        return result;
    }
}



