package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.model.entity.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class PriceCacheServiceImpl {
    private final RedisTemplate<String, Price> redisTemplate;
    private final long cacheTTL = 10_000; // TTL: 10s

    @Autowired
    public PriceCacheServiceImpl(RedisTemplate<String, Price> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cachePrice(String cryptoPair, Price price) {
        redisTemplate.opsForValue().set(cryptoPair, price, Duration.ofMillis(cacheTTL));
    }

    public Price getCachedPrice(String cryptoPair) {
        return redisTemplate.opsForValue().get(cryptoPair);
    }
}
