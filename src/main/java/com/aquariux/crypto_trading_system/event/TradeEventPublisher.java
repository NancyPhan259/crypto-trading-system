package com.aquariux.crypto_trading_system.event;

import com.aquariux.crypto_trading_system.entity.Trade;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class TradeEventPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic tradeTopic;

    public TradeEventPublisher(RedisTemplate<String, Object> genericRedisTemplate, ChannelTopic tradeTopic) {
        this.redisTemplate = genericRedisTemplate;
        this.tradeTopic = tradeTopic;
    }

    public void publishTradeEvent(Trade trade) {
        redisTemplate.convertAndSend(tradeTopic.getTopic(), trade);
    }

    public void onTradeSuccess(String message) {
        System.out.println("Received message: " + message);
    }
}

