package com.aquariux.crypto_trading_system.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    int timeWindowInSeconds();
    int maxRequests();
}