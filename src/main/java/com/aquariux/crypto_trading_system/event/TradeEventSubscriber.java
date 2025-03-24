package com.aquariux.crypto_trading_system.event;

import com.aquariux.crypto_trading_system.model.entity.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class TradeEventSubscriber implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(TradeEventSubscriber.class);

    private final GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Trade trade = (Trade) serializer.deserialize(message.getBody());
        LOG.info("📥 Received trade event: {}", trade);
        // Sau này có thể xử lý logic nâng cao ở đây
    }
}



