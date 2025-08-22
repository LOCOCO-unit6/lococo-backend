package com.springboot.lococo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer; // 변경
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map; // 추가

@Configuration
public class RedisConfig {
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // Key 직렬화는 그대로
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // Value 직렬화를 Jackson2JsonRedisSerializer로 변경
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // ObjectMapper 설정 (필요에 따라)
        ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // ...
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // Value 직렬화에 적용
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        // Hash Key 직렬화는 그대로
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // Hash Value 직렬화에 적용
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }
}
