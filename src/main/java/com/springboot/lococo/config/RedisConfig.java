package com.springboot.lococo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(); // host/port는 application.properties 사용
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // Key는 String으로 직렬화
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // Value는 JSON 형식으로 직렬화하여 Object 타입을 저장할 수 있게 함
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Hash Key는 String으로 직렬화
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // Hash Value는 JSON 형식으로 직렬화
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }
}
