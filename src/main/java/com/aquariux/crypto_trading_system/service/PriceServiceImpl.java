package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.model.entity.Price;
import com.aquariux.crypto_trading_system.respository.spec.PriceRepository;
import com.aquariux.crypto_trading_system.service.contract.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.aquariux.crypto_trading_system.util.validation.CryptoPairValidator.VALID_CRYPTO_PAIRS;

@Service
public class PriceServiceImpl implements PriceService {
     private static final Logger LOG = LoggerFactory.getLogger(PriceServiceImpl.class);

     private final PriceRepository priceRepository;
     private final PriceCacheServiceImpl priceCacheServiceImpl;

    public PriceServiceImpl(RestTemplate restTemplate, PriceRepository priceRepository, PriceCacheServiceImpl priceCacheServiceImpl) {
        this.priceRepository = priceRepository;
        this.priceCacheServiceImpl = priceCacheServiceImpl;
    }

    @Override
    public Map<String, Price> getPrices() {
        Map<String, Price> prices = new HashMap<>();
        for (String cryptoPair : VALID_CRYPTO_PAIRS) {
            getPrice(cryptoPair).ifPresent(price -> prices.put(cryptoPair, price));
        }
        return prices;
    }


    @Override
    public Optional<Price> getPrice(String cryptoPair) {
        Optional<Price> cachedPrice = priceCacheServiceImpl.getCachedPrice(cryptoPair);
        if (cachedPrice.isPresent()) {
            return cachedPrice;
        }
        Optional<Price> priceFromDb = priceRepository.findPrice(cryptoPair);
                if (priceFromDb.isPresent()) {
                    return priceFromDb;
                }
        LOG.error("Price not found in cache or DB for pair: {}", cryptoPair);
        return Optional.empty();
    }
}
