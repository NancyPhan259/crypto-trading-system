package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.model.entity.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;


@Service
public class PriceCacheServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(PriceCacheServiceImpl.class);

    private final RedisTemplate<String, Price> redisTemplate;

    @Value("${cache.price.ttl:10}")
    private long cacheTTL;

    public PriceCacheServiceImpl(RedisTemplate<String, Price> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cachePrice(String cryptoPair, Price price) {
        logger.info("Caching price for {}: {}", cryptoPair, price);
        redisTemplate.opsForValue().set(cryptoPair, price, Duration.ofMinutes(cacheTTL));
    }

    public Optional<Price> getCachedPrice(String cryptoPair) {
        Price price = redisTemplate.opsForValue().get(cryptoPair);
        logger.info("Retrieved cached price for {}: {}", cryptoPair, price);
        return Optional.ofNullable(price);
    }
}
