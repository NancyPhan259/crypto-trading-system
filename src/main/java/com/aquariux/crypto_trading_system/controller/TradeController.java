package com.aquariux.crypto_trading_system.controller;

import com.aquariux.crypto_trading_system.dto.TradeRequest;
import com.aquariux.crypto_trading_system.model.entity.Trade;
import com.aquariux.crypto_trading_system.service.TradeService;
import com.aquariux.crypto_trading_system.annotation.RateLimit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trade")
public class TradeController {
    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("/execute")
    public ResponseEntity<Trade> executeTrade(@RequestBody TradeRequest request) {
        Trade trade = tradeService.executeTrade(
                request.getUserId(),
                request.getCryptoPair(),
                request.getQuantity(),
                request.getSide()
        );
        return ResponseEntity.ok(trade);
    }

    @RateLimit(timeWindowInSeconds = 60, maxRequests = 5)
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Trade>> getTradeHistory(@PathVariable String userId) {
        return ResponseEntity.ok(tradeService.getUserTradeHistory(userId));
    }

    @RateLimit(timeWindowInSeconds = 60, maxRequests = 5)
    @GetMapping("/history/{userId}/paged")
    public Page<Trade> getHistory(
        @PathVariable String userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("tradeTimestamp").descending());
        return tradeService.getUserTradeHistory(userId, pageable);
    }
}

