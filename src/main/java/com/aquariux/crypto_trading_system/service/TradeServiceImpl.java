package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.model.TradeSide;
import com.aquariux.crypto_trading_system.model.entity.Asset;
import com.aquariux.crypto_trading_system.model.entity.Price;
import com.aquariux.crypto_trading_system.model.entity.Trade;
import com.aquariux.crypto_trading_system.respository.AssetRepository;
import com.aquariux.crypto_trading_system.respository.PriceRepository;
import com.aquariux.crypto_trading_system.respository.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TradeServiceImpl implements TradeService {

    private static final Logger LOG = LoggerFactory.getLogger(TradeServiceImpl.class);

    private final TradeRepository tradeRepository;
    private final AssetRepository assetRepository;
    private final PriceRepository priceRepository;

    public TradeServiceImpl(TradeRepository tradeRepository, AssetRepository assetRepository, PriceRepository priceRepository) {
        this.tradeRepository = tradeRepository;
        this.assetRepository = assetRepository;
        this.priceRepository = priceRepository;
    }

    @Override
    @Transactional
    public Trade executeTrade(String userId, String cryptoPair, BigDecimal balance, TradeSide side) {
        Price price = priceRepository.findByCryptoPair(cryptoPair)
                .orElseThrow(() -> new RuntimeException("No price available for: " + cryptoPair));

        BigDecimal tradePrice = side == TradeSide.BUY ? price.getAskPrice() : price.getBidPrice();
        BigDecimal totalAmount = tradePrice.multiply(balance);

        Asset asset = assetRepository.findByUserIdAndSymbol(userId, cryptoPair)
                .orElse(new Asset(userId, cryptoPair, BigDecimal.ZERO));

        if (side == TradeSide.BUY) {
            asset.setBalance(asset.getBalance().add(balance));
        } else {
            if (asset.getBalance().compareTo(balance) < 0) {
                throw new RuntimeException("Not enough balance to sell " + cryptoPair);
            }
            asset.setBalance(asset.getBalance().subtract(balance));
        }

        assetRepository.save(asset);

        Trade trade = new Trade(userId, cryptoPair, tradePrice, balance, ZonedDateTime.now());
        return tradeRepository.save(trade);
    }

    @Override
    public List<Trade> getUserTradeHistory(String userId) {
        return tradeRepository.findByUserIdOrderByTimestampDesc(userId);
    }
}

