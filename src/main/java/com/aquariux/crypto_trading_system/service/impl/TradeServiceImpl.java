package com.aquariux.crypto_trading_system.service.impl;

import com.aquariux.crypto_trading_system.model.TradeSide;
import com.aquariux.crypto_trading_system.model.entity.Asset;
import com.aquariux.crypto_trading_system.model.entity.AssetHistory;
import com.aquariux.crypto_trading_system.model.entity.Price;
import com.aquariux.crypto_trading_system.model.entity.Trade;
import com.aquariux.crypto_trading_system.respository.AssetHistoryRepository;
import com.aquariux.crypto_trading_system.respository.AssetRepository;
import com.aquariux.crypto_trading_system.respository.PriceRepository;
import com.aquariux.crypto_trading_system.respository.TradeRepository;
import com.aquariux.crypto_trading_system.service.PriceCacheService;
import com.aquariux.crypto_trading_system.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class TradeServiceImpl implements TradeService {

    private static final Logger LOG = LoggerFactory.getLogger(TradeServiceImpl.class);

    private final TradeRepository tradeRepository;
    private final AssetRepository assetRepository;
    private final PriceRepository priceRepository;
    private final AssetHistoryRepository assetHistoryRepository;
    private final PriceCacheService priceCacheService;

    public TradeServiceImpl(TradeRepository tradeRepository, AssetRepository assetRepository, PriceRepository priceRepository, AssetHistoryRepository assetHistoryRepository, PriceCacheService priceCacheService) {
        this.tradeRepository = tradeRepository;
        this.assetRepository = assetRepository;
        this.priceRepository = priceRepository;
        this.assetHistoryRepository = assetHistoryRepository;
        this.priceCacheService = priceCacheService;
    }

    @Override
    @Transactional
    public Trade executeTrade(String userId, String cryptoPair, BigDecimal quantity, TradeSide side) {

        Price price = priceCacheService.getCachedPrice(cryptoPair)
                .orElseGet(() -> {
            return priceRepository.findTopByCryptoPairOrderByTimestampDesc(cryptoPair)
                .map(latestPrice -> new Price(
                    latestPrice.getCryptoPair(),
                    latestPrice.getBidPrice(),
                    latestPrice.getAskPrice(), ZonedDateTime.now()
                ))
                .orElseThrow(() -> new RuntimeException("No aggregated price available for: " + cryptoPair));
        });

        BigDecimal tradePrice = side == TradeSide.BUY ? price.getAskPrice() : price.getBidPrice();
        BigDecimal totalAmount = tradePrice.multiply(quantity);

        String[] symbols = cryptoPair.split("(?=USD|USDT)");
        String baseSymbol = symbols[0]; // BTC or ETH
        String quoteSymbol = "USDT";

        Asset baseAsset = assetRepository.findByUserIdAndSymbol(userId, baseSymbol)
                .orElse(new Asset(userId, baseSymbol, BigDecimal.ZERO));
        Asset quoteAsset = assetRepository.findByUserIdAndSymbol(userId, quoteSymbol)
                .orElse(new Asset(userId, quoteSymbol, BigDecimal.ZERO));

        if (side == TradeSide.BUY) {
            if (quoteAsset.getBalance().compareTo(totalAmount) < 0) {
                throw new RuntimeException("Not enough USDT balance to buy " + baseSymbol);
            }
            quoteAsset.setBalance(quoteAsset.getBalance().subtract(totalAmount));
            baseAsset.setBalance(baseAsset.getBalance().add(quantity));
        } else {
            if (baseAsset.getBalance().compareTo(quantity) < 0) {
                throw new RuntimeException("Not enough balance to sell " + cryptoPair);
            }
            baseAsset.setBalance(baseAsset.getBalance().subtract(quantity));
            quoteAsset.setBalance(quoteAsset.getBalance().add(totalAmount));
        }

        assetRepository.save(baseAsset);
        assetRepository.save(quoteAsset);

        assetHistoryRepository.save(new AssetHistory(userId, baseSymbol, baseAsset.getBalance(), ZonedDateTime.now()));
        assetHistoryRepository.save(new AssetHistory(userId, quoteSymbol, quoteAsset.getBalance(), ZonedDateTime.now()));

        Trade trade = new Trade(userId, cryptoPair, tradePrice, quantity, ZonedDateTime.now());
        return tradeRepository.save(trade);
    }

    @Override
    public List<Trade> getUserTradeHistory(String userId) {
        return tradeRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    @Override
    public Page<Trade> getUserTradeHistory(String userId, Pageable pageable) {
        return tradeRepository.findByUserIdOrderByTimestampDesc(userId, pageable);
    }

}

