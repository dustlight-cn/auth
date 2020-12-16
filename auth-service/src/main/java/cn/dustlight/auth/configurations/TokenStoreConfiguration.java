package cn.dustlight.auth.configurations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

public class TokenStoreConfiguration {

    @Bean("authTokenStore")
    @ConditionalOnMissingBean(name = "authTokenStore")
    public RedisTokenStore authTokenStore(@Autowired RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }

}
