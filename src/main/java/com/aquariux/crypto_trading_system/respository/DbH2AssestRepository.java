package com.aquariux.crypto_trading_system.respository;

import com.aquariux.crypto_trading_system.entity.Asset;
import com.aquariux.crypto_trading_system.respository.spec.AssetRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
public class DbH2AssestRepository implements AssetRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RedissonClient redissonClient;
    private final RedisTemplate<String, Asset> redisTemplate;

    @Autowired
    public DbH2AssestRepository(JdbcTemplate jdbcTemplate, RedissonClient redissonClient, RedisTemplate<String, Asset> redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redissonClient = redissonClient;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Asset getAsset(UUID userId, String cryptoType) {
        String sql = "SELECT * FROM asset WHERE user_id = ? AND crypto_type = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            BigDecimal balance = rs.getBigDecimal("balance");
            int version = rs.getInt("version");
            return new Asset(userId, cryptoType, balance, version);
        }, userId, cryptoType);
    }

    @Override
    public Map<String, Asset> getAssets(UUID userId, List<String> coins) {
        Map<String, Asset> assetMap = new HashMap<>();
        List<String> coinsToFetchFromDB = new ArrayList<>();

        for (String coin : coins) {
            Asset cachedAsset = getAssetFromCache(userId, coin);
            if (cachedAsset != null) {
                assetMap.put(coin, cachedAsset);
            } else {
                coinsToFetchFromDB.add(coin);
            }
        }

        if (!coinsToFetchFromDB.isEmpty()) {
            String sql = "SELECT * FROM asset WHERE user_id = ? AND crypto_type IN (";
            String placeholders = String.join(", ", Collections.nCopies(coinsToFetchFromDB.size(), "?"));
            sql += placeholders + ")";

            Object[] params = new Object[coinsToFetchFromDB.size() + 1];
            params[0] = userId;
            System.arraycopy(coinsToFetchFromDB.toArray(), 0, params, 1, coinsToFetchFromDB.size());

            jdbcTemplate.query(sql, (rs, rowNum) -> {
                Asset asset = new Asset(rs.getObject("user_id", UUID.class), rs.getString("crypto_type"),
                        rs.getBigDecimal("balance"), rs.getInt("version"));

                assetMap.put(asset.getCryptoType(), asset);
                saveAssetToCache(asset);
                return asset;
            }, params);
        }

        return assetMap;
    }

    @Override
    public void updateAssets(Collection<Asset> assets) {
        String lockKey = "lock:assets:update";
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean locked = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!locked) {
                throw new IllegalStateException("Could not acquire lock, try again later.");
            }

            for (Asset asset : assets) {
                evictCache(asset.getUserId(), asset.getCryptoType());
                saveAssetToCache(asset);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to acquire Redis lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private void saveAssetToCache(Asset asset) {
        String key = "asset:" + asset.getUserId() + ":" + asset.getCryptoType();
        redisTemplate.opsForValue().set(key, asset, Duration.ofMinutes(10));
    }

    public Asset getAssetFromCache(UUID userId, String cryptoType) {
        String key = "asset:" + userId + ":" + cryptoType;
        return redisTemplate.opsForValue().get(key);
    }

    public void evictCache(UUID userId, String cryptoType) {
        String key = "asset:" + userId.toString() + ":" + cryptoType;
        redisTemplate.delete(key);
    }


}
