package com.aquariux.crypto_trading_system.util.validation;

import java.util.Set;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CryptoPairValidator implements ConstraintValidator<CryptoPairValid, String> {

    public static final Set<String> VALID_CRYPTO_PAIRS = Set.of("ETHUSDT", "BTCUSDT");

    public static final Set<String> VALID_CRYPTO_QUOTES = Set.of("USDT", "ETH", "BTC");

    @Override
    public boolean isValid(String cryptoPair, ConstraintValidatorContext context) {
        return VALID_CRYPTO_PAIRS.contains(cryptoPair);
    }
}
