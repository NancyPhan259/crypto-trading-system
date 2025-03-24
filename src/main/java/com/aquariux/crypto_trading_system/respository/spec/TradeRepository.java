package com.aquariux.crypto_trading_system.respository.spec;

import com.aquariux.crypto_trading_system.dto.api.page.PagingRequest;
import com.aquariux.crypto_trading_system.entity.Trade;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository {
    Trade save(Trade trade);

    int countTrades(String userId);

    List<Trade> getAllTrades(String userId, PagingRequest pageRequest);
}
