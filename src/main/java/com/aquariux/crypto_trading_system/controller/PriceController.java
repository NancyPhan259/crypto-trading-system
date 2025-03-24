package com.aquariux.crypto_trading_system.controller;

import com.aquariux.crypto_trading_system.dto.PriceResponse;
import com.aquariux.crypto_trading_system.model.entity.Price;
import com.aquariux.crypto_trading_system.service.PriceService;
import com.aquariux.crypto_trading_system.annotation.RateLimit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * REST controller to expose latest prices.
 */
@RestController
@RequestMapping("/api/price/latest")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @RateLimit
    @GetMapping
    public ResponseEntity<List<PriceResponse>> getLatestPrices() {
        Map<String, Price> prices = priceService.getPrices();
        List<PriceResponse> priceResponses = new LinkedList<>();
        for (Price price : prices.values()) {
            priceResponses.add(new PriceResponse(
                    price.getCryptoPair(),
                    price.getBidPrice(),
                    price.getAskPrice(),
                    price.getTimestamp()
            ));
        }
        return ResponseEntity.ok(priceResponses);
    }
}
