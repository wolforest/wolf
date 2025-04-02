package cn.coderule.framework.config;

import cn.coderule.framework.layer.web.auth.session.SessionSaver;
import cn.coderule.framework.layer.web.auth.WolfAuth;
import cn.coderule.framework.layer.web.auth.auth.AuthConfig;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = {"wolf.auth.enable"}, havingValue = "true")
public class AuthAutoConfig {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public WolfAuth session(AuthConfig authConfig) {
        return new WolfAuth(redisTemplate(), authConfig);
    }

    @Bean
    public SessionSaver sessionSaver(WolfAuth wolfAuth) {
        return new SessionSaver(wolfAuth.getSession());
    }

    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        FastJson2RedisSerializer<Object> fastJson2RedisSerializer = new FastJson2RedisSerializer<>(Object.class);

        redisTemplate.setEnableDefaultSerializer(true);
        redisTemplate.setDefaultSerializer(fastJson2RedisSerializer);

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        redisTemplate.setValueSerializer(fastJson2RedisSerializer);
        redisTemplate.setHashValueSerializer(fastJson2RedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
