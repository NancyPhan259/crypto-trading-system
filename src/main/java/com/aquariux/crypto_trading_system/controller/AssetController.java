package com.aquariux.crypto_trading_system.controller;

import com.aquariux.crypto_trading_system.model.entity.Asset;
import com.aquariux.crypto_trading_system.service.AssetService;
import com.aquariux.crypto_trading_system.annotation.RateLimit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {
    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @RateLimit
    @GetMapping("/{userId}")
    public ResponseEntity<List<Asset>> getUserAssets(@PathVariable String userId) {
        return ResponseEntity.ok(assetService.getAllAssetsForUser(userId));
    }

    @RateLimit
    @GetMapping("/{userId}/{symbol}")
    public ResponseEntity<BigDecimal> getUserBalance(
            @PathVariable String userId,
            @PathVariable String symbol
    ) {
        return ResponseEntity.ok(assetService.getUserAssetBalance(userId, symbol));
    }

    @PostMapping("/{userId}/{symbol}")
    public ResponseEntity<Void> updateUserAsset(
            @PathVariable String userId,
            @PathVariable String symbol,
            @RequestParam BigDecimal amountChange
    ) {
        assetService.updateAsset(userId, symbol, amountChange);
        return ResponseEntity.ok().build();
    }
}