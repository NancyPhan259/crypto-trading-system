package com.aquariux.crypto_trading_system.event;

import com.aquariux.crypto_trading_system.model.entity.Trade;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class TradeEventPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;


    public TradeEventPublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publishTradeEvent(Trade trade) {
        redisTemplate.convertAndSend(topic.getTopic(), trade);
    }

    public void onTradeSuccess(String message) {
        System.out.println("Received message: " + message);
    }
}

