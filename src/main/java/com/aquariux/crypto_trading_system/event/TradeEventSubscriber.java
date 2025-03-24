package com.aquariux.crypto_trading_system.event;

import com.aquariux.crypto_trading_system.entity.Trade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class TradeEventSubscriber {
    private static final Logger log = LoggerFactory.getLogger(TradeEventSubscriber.class);

    private final RedisTemplate<String, String> redisTemplate;
    private final ChannelTopic tradeSuccessTopic;

    @Autowired
    public TradeEventSubscriber(RedisTemplate<String, String> redisTemplate, ChannelTopic tradeSuccessTopic) {
        this.redisTemplate = redisTemplate;
        this.tradeSuccessTopic = tradeSuccessTopic;
    }

    public void publishTradeSuccess(Trade trade) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String tradeJson = objectMapper.writeValueAsString(trade);
            redisTemplate.convertAndSend(tradeSuccessTopic.getTopic(), tradeJson);
        } catch (Exception e) {
            log.error("Lỗi khi serialize trade event", e);
        }
    }

    public void onTradeSuccess(String message) {
        log.info("Nhận được sự kiện giao dịch: {}", message);
        // Parse message JSON thành Trade object nếu cần
    }
}


