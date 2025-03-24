package com.aquariux.crypto_trading_system.service.contract;

import com.aquariux.crypto_trading_system.dto.api.page.Page;
import com.aquariux.crypto_trading_system.dto.api.page.PagingRequest;
import com.aquariux.crypto_trading_system.dto.api.trade.TradeHistoryResponse;
import com.aquariux.crypto_trading_system.entity.Trade;

import java.math.BigDecimal;
import java.util.UUID;

public interface TradeService {
    Trade executeTrade(UUID userId, String cryptoPair, String tradeType, BigDecimal tradeAmount);
    Page<TradeHistoryResponse> fetchHistory(UUID userId, PagingRequest pageRequest);
}
