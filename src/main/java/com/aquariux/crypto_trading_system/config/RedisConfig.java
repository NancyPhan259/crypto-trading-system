package com.aquariux.crypto_trading_system.config;

import com.aquariux.crypto_trading_system.entity.Price;
import com.aquariux.crypto_trading_system.entity.Asset;
import com.aquariux.crypto_trading_system.event.TradeEventSubscriber;
import com.aquariux.crypto_trading_system.util.serialize.GenericRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

    @Bean
    public RedisSerializer<Asset> assetRedisSerializer() {
        return GenericRedisSerializer.createSerializer(Asset.class);
    }

    @Bean
    public RedisSerializer<Price> priceRedisSerializer() {
        return GenericRedisSerializer.createSerializer(Price.class);
    }


    @Bean
    public RedisTemplate<String, Asset> assetRedisTemplate(RedisConnectionFactory connectionFactory) {
        return RedisTemplateFactory.createTemplate(connectionFactory, assetRedisSerializer());
    }

    @Bean
    public RedisTemplate<String, Price> priceRedisTemplate(RedisConnectionFactory connectionFactory) {
        return RedisTemplateFactory.createTemplate(connectionFactory, priceRedisSerializer());
    }

    @Bean
    public RedisTemplate<String, Object> genericRedisTemplate(RedisConnectionFactory connectionFactory) {
        return RedisTemplateFactory.createTemplate(connectionFactory, new GenericJackson2JsonRedisSerializer());
    }

    @Bean
    public RedissonClient redissonClient() {
        return Redisson.create();
    }

    @Bean
    public ChannelTopic tradeSuccessTopic() {
        return new ChannelTopic("trade:success");
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory, MessageListenerAdapter tradeListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(tradeListenerAdapter, tradeSuccessTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(TradeEventSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onTradeSuccess");
    }

}
