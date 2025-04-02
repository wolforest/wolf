package cn.coderule.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * com.wolf.framework.config.redis
 *
 * @author Wingle
 * @since 2020/5/29 2:34 下午
 **/
@Configuration
public class RedisAutoConfig {

    @Bean(name = "redisTemplate" )
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        FastJson2RedisSerializer<Object> fastJson2RedisSerializer = new FastJson2RedisSerializer<>(Object.class);

        redisTemplate.setEnableDefaultSerializer(true);
        redisTemplate.setDefaultSerializer(fastJson2RedisSerializer);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setValueSerializer(fastJson2RedisSerializer);
        redisTemplate.setHashValueSerializer(fastJson2RedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }






}
