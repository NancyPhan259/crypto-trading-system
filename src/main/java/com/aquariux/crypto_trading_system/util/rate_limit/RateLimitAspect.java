package com.aquariux.crypto_trading_system.util.rate_limit;

import com.aquariux.crypto_trading_system.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RateLimitAspect {
    private final RateLimiterService rateLimiterService;
    private final HttpServletRequest request;

    public RateLimitAspect(RateLimiterService rateLimiterService, HttpServletRequest request) {
        this.rateLimiterService = rateLimiterService;
        this.request = request;
    }

    @Around("@annotation(rateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String clientIp = request.getRemoteAddr();
        if (!rateLimiterService.isAllowed(clientIp, rateLimit.maxRequests(), rateLimit.timeWindowSeconds())) {
            throw new RuntimeException("Too many requests. Please try again later.");
        }
        return joinPoint.proceed();
    }
}
