package com.aquariux.crypto_trading_system.controller;

import com.aquariux.crypto_trading_system.dto.CustomUserDetails;
import com.aquariux.crypto_trading_system.dto.api.page.AssetResponse;
import com.aquariux.crypto_trading_system.entity.Asset;
import com.aquariux.crypto_trading_system.service.contract.AssetService;
import com.aquariux.crypto_trading_system.util.rate_limit.RateLimit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/api/user/balance")
public class AssetController {

    private static final Logger LOG = LoggerFactory.getLogger(AssetController.class);

    private final AssetService priceService;

    private final ObjectMapper objectMapper;

    private final MessageDigest hashingFunc;

    @Autowired
    public AssetController(AssetService priceService,
                           ObjectMapper objectMapper) throws NoSuchAlgorithmException {
        this.priceService = priceService;
        this.objectMapper = objectMapper;
        this.hashingFunc = MessageDigest.getInstance("SHA-256");
    }

    @RateLimit(maxRequests = 50, timeWindowSeconds = 60)
    @GetMapping
    public ResponseEntity<AssetResponse> getAssetsBalance(@RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch)
            throws JsonProcessingException {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = principal.getUser().getUserId();

        Collection<Asset> assets = priceService.getAssets(userId);
        List<AssetResponse.AssetBalance> assetBalances = new LinkedList<>();
        for (Asset asset : assets) {
            assetBalances.add(new AssetResponse.AssetBalance(asset.getCryptoType(), asset.getBalance()));
        }
        AssetResponse assetResponse = new AssetResponse(userId, assetBalances);

        String json = objectMapper.writeValueAsString(assetResponse);
        String etag = generateETag(json);
        if (etag.equals(ifNoneMatch)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.ok()
                .eTag(etag)
                .body(assetResponse);
    }

    private String generateETag(String data) {
        byte[] hash = hashingFunc.digest(data.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}