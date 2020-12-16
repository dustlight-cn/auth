package cn.dustlight.auth.configurations;

import cn.dustlight.auth.properties.AuthorizationCodeProperties;
import cn.dustlight.auth.services.AuthApprovalHandler;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.services.RedisAuthorizationCodeService;
import cn.dustlight.auth.services.ScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

public class TokenStoreConfiguration {

    @Bean("authTokenStore")
    @ConditionalOnMissingBean(name = "authTokenStore")
    public RedisTokenStore authTokenStore(@Autowired RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean("authApprovalHandler")
    @ConditionalOnMissingBean(name = "authApprovalHandler")
    public AuthApprovalHandler authApprovalHandler(@Autowired ScopeService scopeService,
                                                   @Autowired ClientService clientService,
                                                   @Autowired TokenStore authTokenStore) {
        TokenApprovalStore approvalStore = new TokenApprovalStore();
        approvalStore.setTokenStore(authTokenStore);
        return new AuthApprovalHandler(scopeService, clientService, approvalStore);
    }

    @Bean("authorizationCodeServices")
    @ConditionalOnMissingBean(name = "authorizationCodeServices")
    public AuthorizationCodeServices authorizationCodeServices(@Autowired RedisConnectionFactory factory,
                                                               @Autowired AuthorizationCodeProperties properties) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.java());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.java());
        RedisAuthorizationCodeService instance = new RedisAuthorizationCodeService(redisTemplate);
        if (properties != null) {
            if (properties.getDuration() != null)
                instance.setDuration(properties.getDuration());
            if (properties.getKeyPrefix() != null)
                instance.setKeyPrefix(properties.getKeyPrefix());
        }
        return instance;
    }
}
