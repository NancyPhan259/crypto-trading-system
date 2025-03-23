package com.aquariux.crypto_trading_system.util.rate_limit;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {
    int maxRequests();
    int timeWindowSeconds();
}