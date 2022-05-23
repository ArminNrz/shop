package com.shop.configuration;

import com.shop.entity.OtpEntity;
import io.netty.util.internal.StringUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxTotal;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Bean
    public JedisClientConfiguration getJedisClientConfiguration() {
        //
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jedisPoolingClientConfigurationBuilder =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();

        // create generic object pool config
        @SuppressWarnings("rawtypes")
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig<>();

        // set value to generic object pool config
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMaxTotal(maxTotal);
        genericObjectPoolConfig.setMinIdle(minIdle);

        // build jedis pool client configuration pool with generic object pool config
        return jedisPoolingClientConfigurationBuilder.poolConfig(genericObjectPoolConfig).build();
    }

    @Bean
    public JedisConnectionFactory getJedisConnectionFactory() {
        //
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        // set host name
        redisStandaloneConfiguration.setHostName(host);

        // set password if not empty
        if (!StringUtil.isNullOrEmpty(password)) {
            redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        }

        // set port
        redisStandaloneConfiguration.setPort(port);

        //
        return new JedisConnectionFactory(redisStandaloneConfiguration, getJedisClientConfiguration());
    }

    @Bean
    @SuppressWarnings("rawtypes")
    public RedisTemplate redisTemplate() {
        //
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();

        //
        redisTemplate.setConnectionFactory(getJedisConnectionFactory());

        //
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //
        return redisTemplate;
    }

    @Bean
    @Qualifier("hashOperations")
    public HashOperations<String, String, OtpEntity> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }
}
