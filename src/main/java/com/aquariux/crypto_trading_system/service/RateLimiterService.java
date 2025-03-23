package com.aquariux.crypto_trading_system.service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {
    private final RedisTemplate<String, String> redisTemplate;


    public RateLimiterService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String clientId, int maxRequests, int timeWindowSeconds) {
        String key = "rate_limit:" + clientId;
        Long currentCount = redisTemplate.opsForValue().increment(key);

        if (currentCount == 1) {
            redisTemplate.expire(key, timeWindowSeconds, TimeUnit.SECONDS);
        }

        return currentCount <= maxRequests;
    }
}
