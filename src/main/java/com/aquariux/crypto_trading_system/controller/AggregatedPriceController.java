package com.aquariux.crypto_trading_system.controller;

import com.aquariux.crypto_trading_system.dto.api.price.PriceResponse;
import com.aquariux.crypto_trading_system.model.entity.Price;
import com.aquariux.crypto_trading_system.service.contract.PriceService;
import com.aquariux.crypto_trading_system.util.rate_limit.RateLimit;
import com.aquariux.crypto_trading_system.util.validation.CryptoPairValid;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/price/lastest")
public class AggregatedPriceController {
    private final PriceService priceService;

    @Autowired
    public AggregatedPriceController(PriceService priceService) {
        this.priceService = priceService;
    }


    @RateLimit(maxRequests = 50, timeWindowSeconds = 60)
    @GetMapping
    public ResponseEntity<List<PriceResponse>> getLatestPrices() {
        Map<String, Price> prices = priceService.getPrices();
        List<PriceResponse> priceResponses = new LinkedList<>();
        for (Price price : prices.values()) {
            priceResponses.add(new PriceResponse(price.getCryptoPair(), price.getBidPrice(), price.getAskPrice(), price.getTimestamp()));
        }
        return ResponseEntity.ok(priceResponses);
    }

    @RateLimit(maxRequests = 50, timeWindowSeconds = 60)
    @GetMapping("/{cryptoPair}")
    public ResponseEntity<PriceResponse> getLatestPrice(@Valid @CryptoPairValid @PathVariable("cryptoPair") String cryptoPair) {
        Optional<Price> optionalPrice = priceService.getPrice(cryptoPair);
        if (optionalPrice.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Price price = optionalPrice.get();
        return ResponseEntity.ok(new PriceResponse(price.getCryptoPair(), price.getBidPrice(), price.getAskPrice(), price.getTimestamp()));
    }
}
