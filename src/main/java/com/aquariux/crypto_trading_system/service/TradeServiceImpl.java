package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.dto.api.page.Page;
import com.aquariux.crypto_trading_system.dto.api.page.PagingRequest;
import com.aquariux.crypto_trading_system.dto.api.trade.TradeHistoryResponse;
import com.aquariux.crypto_trading_system.entity.Asset;
import com.aquariux.crypto_trading_system.entity.Price;
import com.aquariux.crypto_trading_system.entity.Trade;
import com.aquariux.crypto_trading_system.event.TradeEventPublisher;
import com.aquariux.crypto_trading_system.respository.spec.AssetRepository;
import com.aquariux.crypto_trading_system.respository.spec.TradeRepository;
import com.aquariux.crypto_trading_system.service.contract.PriceService;
import com.aquariux.crypto_trading_system.service.contract.TradeService;
import com.aquariux.crypto_trading_system.util.exception.InvalidTrade;
import com.aquariux.crypto_trading_system.util.extract.CryptoPairExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class TradeServiceImpl implements TradeService {

    private static final Logger LOG = LoggerFactory.getLogger(TradeServiceImpl.class);

    private static final int MAX_RETRIES_OPTIMISTIC_LOCK = 5;

    private final CryptoPairExtractor cryptoPairExtractor;

    private final UUID idGenerator;

    private final PriceService priceService;

    private final AssetRepository assetRepository;

    private final TradeRepository tradeRepository;

    private final Set<String> validTradeTypes;

    private final TradeEventPublisher tradeEventPublisher;

    @Autowired
    public TradeServiceImpl(PriceService priceService,
                            CryptoPairExtractor cryptoPairExtractor,
                            UUID idGenerator,
                            AssetRepository assetRepository,
                            TradeRepository tradeRepository, TradeEventPublisher tradeEventPublisher) {
    this.priceService = priceService;
    this.cryptoPairExtractor = cryptoPairExtractor;
    this.idGenerator = idGenerator;
    this.assetRepository = assetRepository;
    this.tradeRepository = tradeRepository;
    this.tradeEventPublisher = tradeEventPublisher;
    this.validTradeTypes = new HashSet<>();
    validTradeTypes.add("BUY");
    validTradeTypes.add("SELL");
}

    @Override
    public Trade executeTrade(UUID userId, String cryptoPair, String tradeType, BigDecimal tradeAmount) {
        String[] coins = cryptoPairExtractor.extractCurrencies(cryptoPair);
        if (coins.length != 2) {
            throw new InvalidTrade(String.format("Invalid crypto pair, the system does not support this pair %s",
                    cryptoPair));
        }
        String baseCoin = coins[0];
        String quoteCoin = coins[1];

        Optional<Price> optionalLatestPrice = priceService.getPrice(cryptoPair);
        if (optionalLatestPrice.isEmpty()) {
            throw new InvalidTrade(String.format("Could not get the price of the crypto pair %s", cryptoPair));
        }
        Price latestPrice = optionalLatestPrice.get();

        if (!validTradeTypes.contains(tradeType)) {
            throw new InvalidTrade(String.format("Invalid trade type, it must be %s instead of %s",
                    validTradeTypes, tradeType));
        }
        BigDecimal tradePrice = tradeType.equals("BUY") ? latestPrice.getAskPrice() : latestPrice.getBidPrice();

        if (tradeAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTrade("The trade amount is lower than zero");
        }

        Trade trade;
        if (tradeType.equals("BUY")) {
            trade = buy(userId, cryptoPair, tradeType, tradeAmount, tradePrice, baseCoin, quoteCoin);
        } else {
            trade = sell(userId, cryptoPair, tradeType, tradeAmount, tradePrice, baseCoin, quoteCoin);
        }
        tradeEventPublisher.publishTradeEvent(trade);
        return trade;
    }


    @Transactional
    private Trade buy(UUID userId, String cryptoPair, String tradeType, BigDecimal tradeAmount, BigDecimal tradePrice, String baseCoin, String quoteCoin) {

        for (int attempt = 0; attempt < MAX_RETRIES_OPTIMISTIC_LOCK; attempt++) {

            try {

                Map<String, Asset> userAssets = assetRepository.getAssets(userId, Arrays.asList(quoteCoin, baseCoin));

                Asset quoteCoinAsset = userAssets.get(quoteCoin);
                BigDecimal totalCost = tradePrice.multiply(tradeAmount);
                if (quoteCoinAsset.getBalance().compareTo(totalCost) < 0) {
                    throw new InvalidTrade(String.format("Insufficient %s balance, the remaining %.5f < total cost %.5f", quoteCoin, quoteCoinAsset.getBalance(), totalCost));
                }
                userAssets.put(quoteCoin, new Asset(userId, quoteCoin, quoteCoinAsset.getBalance().subtract(totalCost), quoteCoinAsset.getVersion()));

                Asset baseCoinAsset = userAssets.getOrDefault(baseCoin, new Asset(userId, baseCoin, BigDecimal.ZERO, 0));
                userAssets.put(baseCoin, new Asset(userId, baseCoin, baseCoinAsset.getBalance().add(tradeAmount), baseCoinAsset.getVersion()));

                assetRepository.updateAssets(userAssets.values());

                Trade trade = new Trade(UUID.randomUUID(), userId.toString(), cryptoPair, tradeType, tradeAmount, totalCost, ZonedDateTime.now());
                tradeRepository.save(trade);
                return trade;

            } catch (OptimisticLockingFailureException e) {
                LOG.warn("Optimistic locking failure for Trade Buy {}, retrying... Attempt: {}",
                        userId + " " + cryptoPair + " " + tradeAmount + " " + tradePrice,
                        attempt);
            } catch (InvalidTrade e) {
                throw e;
            } catch (Throwable throwable) {
                LOG.error(throwable.getMessage(), throwable);
                throw new InvalidTrade(
                        String.format("The transaction for the Trade Buy %s is fail ! Please try again !", userId + " " + cryptoPair + " " + tradeAmount + " " + tradePrice),
                        throwable);
            }
        }

        throw new InvalidTrade("Failed to complete the trade after multiple attempts");
    }

    @Transactional
    private Trade sell(UUID userId, String cryptoPair, String tradeType, BigDecimal tradeAmount, BigDecimal tradePrice, String baseCoin, String quoteCoin) {
         for (int attempt = 0; attempt < MAX_RETRIES_OPTIMISTIC_LOCK; attempt++) {

             try {
                 Map<String, Asset> assets = assetRepository.getAssets(userId, Arrays.asList(quoteCoin, baseCoin));
                 Asset baseCoinAsset = assets.get(baseCoin);
                 if (baseCoinAsset.getBalance().compareTo(tradeAmount) < 0) {
                     throw new InvalidTrade(String.format("Insufficient %s balance, the remaining %.5f < trade amount %.5f", baseCoin, baseCoinAsset.getBalance(), tradeAmount));
                 }
                 assets.put(baseCoin, new Asset(userId, baseCoin, baseCoinAsset.getBalance().subtract(tradeAmount), baseCoinAsset.getVersion()));

                 Asset quoteCoinAsset = assets.getOrDefault(quoteCoin, new Asset(userId, quoteCoin, BigDecimal.ZERO, 0));
                 BigDecimal totalRevenue = tradePrice.multiply(tradeAmount);
                 assets.put(quoteCoin, new Asset(userId, quoteCoin, quoteCoinAsset.getBalance().add(totalRevenue), quoteCoinAsset.getVersion()));

                 assetRepository.updateAssets(assets.values());

                 Trade trade = new Trade(UUID.randomUUID(), userId.toString(), cryptoPair, tradeType, tradeAmount, totalRevenue, ZonedDateTime.now());
                 tradeRepository.save(trade);
                 return trade;

             } catch (OptimisticLockingFailureException e) {
                 LOG.warn("Optimistic locking failure for Trade Sell {}, retrying... Attempt: {}",
                         userId + " " + cryptoPair + " " + tradeAmount + " " + tradePrice,
                         attempt);
             } catch (InvalidTrade e) {
                 throw e;
             } catch (Throwable throwable) {
                 LOG.error(throwable.getMessage(), throwable);
                 throw new InvalidTrade(
                         String.format("The transaction for the Trade Sell %s is fail ! Please try again !", userId + " " + cryptoPair + " " + tradeAmount + " " + tradePrice),
                         throwable);
             }
         }

         throw new InvalidTrade("Failed to complete the trade after multiple attempts");
    }

    @Override
    public Page<TradeHistoryResponse> fetchHistory(UUID userId, PagingRequest pageRequest) {
        List<Trade> trades = tradeRepository.getAllTrades(userId.toString(), pageRequest);
        List<TradeHistoryResponse> tradeHistoryResponse = trades.stream().map(trade -> new TradeHistoryResponse(trade.getId(),
                trade.getUserId(), trade.getCryptoPair(), trade.getTradeType(), trade.getTradeAmount(),
                trade.getTradePrice(), trade.getTradeTimestamp())).toList();

        long totalElements = tradeRepository.countTrades(userId.toString());
        String lastId = trades.isEmpty() ? null : trades.get(trades.size() - 1).getId().toString();

        return new Page<>(tradeHistoryResponse, lastId, pageRequest.getPageSize(), totalElements);
    }
}
