package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.model.TradeSide;
import com.aquariux.crypto_trading_system.model.entity.Trade;

import java.math.BigDecimal;
import java.util.List;

public interface TradeService {
    Trade executeTrade(String userId, String cryptoPair, BigDecimal quantity, TradeSide side);
    List<Trade> getUserTradeHistory(String userId);
}
