package com.aquariux.crypto_trading_system.aspect;

import com.aquariux.crypto_trading_system.annotation.RateLimit;
import com.aquariux.crypto_trading_system.service.RateLimiterService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RateLimitAspect {

    private final RateLimiterService rateLimiter;

    public RateLimitAspect(RateLimiterService rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Around("@annotation(rateLimit) && args(userId,..)")
    public Object enforceRateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit, String userId) throws Throwable {
        if (!rateLimiter.isAllowed(userId)) {
            throw new RuntimeException("Rate limit exceeded for user: " + userId);
        }
        return joinPoint.proceed();
    }
}

