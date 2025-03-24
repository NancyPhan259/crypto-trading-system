package com.aquariux.crypto_trading_system.controller;


import com.aquariux.crypto_trading_system.dto.CustomUserDetails;
import com.aquariux.crypto_trading_system.dto.api.page.Page;
import com.aquariux.crypto_trading_system.dto.api.page.PagingRequest;
import com.aquariux.crypto_trading_system.dto.api.trade.TradeHistoryResponse;
import com.aquariux.crypto_trading_system.dto.api.trade.TradeRequest;
import com.aquariux.crypto_trading_system.dto.api.trade.TradeResponse;
import com.aquariux.crypto_trading_system.entity.Trade;
import com.aquariux.crypto_trading_system.service.contract.TradeService;
import com.aquariux.crypto_trading_system.util.extract.FieldsExtractor;
import com.aquariux.crypto_trading_system.util.rate_limit.RateLimit;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/trades")
public class TradeController {
    private final TradeService tradeService;
    private final FieldsExtractor fieldsExtractor;

    public TradeController(TradeService tradeService, FieldsExtractor fieldsExtractor) {
        this.tradeService = tradeService;
        this.fieldsExtractor = fieldsExtractor;
    }

    @RateLimit(maxRequests = 50, timeWindowSeconds = 60)
    @PostMapping
    public ResponseEntity<TradeResponse> executeTrade(@Valid @RequestBody TradeRequest tradeRequest) {
        System.out.println(">>> TradeController: executeTrade method is called!");
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = principal.getUser().getUserId();
        Trade trade = tradeService.executeTrade(userId, tradeRequest.getCryptoPair(),
                tradeRequest.getTradeType(), tradeRequest.getTradeAmount());
        TradeResponse tradeExecutedSuccessfully = new TradeResponse(trade.getId(), trade.getUserId(), trade.getCryptoPair(),
                trade.getTradeType(), trade.getTradeAmount(), trade.getTradePrice(), trade.getTradeTimestamp(),
                "Trade executed successfully");
        return ResponseEntity.ok(tradeExecutedSuccessfully);
    }


    @RateLimit(maxRequests = 100, timeWindowSeconds = 60)
    @GetMapping("/history")
    public ResponseEntity<Page<TradeHistoryResponse>> getTradeHistory(@RequestParam(value = "_order", required = false) Optional<String> order,
                                                                      @RequestParam(value = "_sort", required = false) Optional<String> sort,
                                                                      @RequestParam(value = "limit", required = false) Optional<Integer> pageSize,
                                                                      @RequestParam(value = "lastId", required = false) Optional<String> lastId) {

        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = principal.getUser().getUserId();

        String actualSortField = sort.orElse("id");
        if (!fieldsExtractor.checkValidSortableField(actualSortField, Trade.class)) {
            ResponseEntity.badRequest().body(String.format("The _sort %s is not match with the system", sort));
        }

        PagingRequest pageRequest = new PagingRequest(
                order.orElse("asc").equalsIgnoreCase("desc") ? "desc" : "asc",
                actualSortField,
                pageSize.orElse(12),
                lastId.orElse(null)
        );

        Page<TradeHistoryResponse> tradeHistoriesResponse = tradeService.fetchHistory(userId, pageRequest);
        return ResponseEntity.ok(tradeHistoriesResponse);
    }
}
