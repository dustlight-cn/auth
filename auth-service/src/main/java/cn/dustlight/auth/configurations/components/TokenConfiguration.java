package cn.dustlight.auth.configurations.components;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.User;
import cn.dustlight.auth.properties.AuthorizationCodeProperties;
import cn.dustlight.auth.services.oauth.EnhancedRedisTokenStore;
import cn.dustlight.auth.services.oauth.RedisAuthorizationCodeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.util.StringUtils;

import java.util.Map;

@EnableConfigurationProperties(TokenConfiguration.TokenProperties.class)
public class TokenConfiguration {

    private Log logger = LogFactory.getLog(getClass());

    @Bean
    public RedisTokenStore redisTokenStore(@Autowired RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public EnhancedRedisTokenStore enhancedRedisTokenStore(@Autowired RedisConnectionFactory redisConnectionFactory,
                                                           @Autowired RedisTokenStore redisTokenStore) {
        return new EnhancedRedisTokenStore(redisConnectionFactory, redisTokenStore);
    }

    @Bean
    public AccessTokenConverter accessTokenConverter() {
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter() {
            @Override
            public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
                Map claims = super.convertAccessToken(token, authentication);
                if (claims == null)
                    return null;
                claims.put("active", true);
                return claims;
            }
        };
        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter() {
            @Override
            public Map<String, ?> convertUserAuthentication(Authentication authentication) {
                Map response = super.convertUserAuthentication(authentication);
                if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof User) {
                    User user = (User) authentication.getPrincipal();
                    response.put("username", user.getUid() != null ? user.getUid().toString() : null);
                }
                return response;
            }
        };
        accessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        return accessTokenConverter;
    }

    @Bean
    public Jwt jwtAccessTokenConverter(@Autowired TokenProperties properties,
                                       @Autowired AccessTokenConverter accessTokenConverter) {

        JwtAccessTokenConverter jwtAccessTokenConverter = null;
        if (StringUtils.hasText(properties.getSigningKey()) && StringUtils.hasText(properties.getVerifierKey())) {
            jwtAccessTokenConverter = new JwtAccessTokenConverter();
            jwtAccessTokenConverter.setAccessTokenConverter(accessTokenConverter);
            jwtAccessTokenConverter.setSigningKey(properties.getSigningKey());
            jwtAccessTokenConverter.setVerifierKey(properties.getVerifierKey());
        }
        return new Jwt(jwtAccessTokenConverter);
    }

    @Bean("authApprovalStore")
    @ConditionalOnMissingBean(name = "authApprovalStore")
    public ApprovalStore authApprovalStore(@Autowired TokenStore authTokenStore) {
        TokenApprovalStore approvalStore = new TokenApprovalStore();
        approvalStore.setTokenStore(authTokenStore);
        return approvalStore;
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

    @ConfigurationProperties(prefix = "dustlight.auth.jwt")
    public static class TokenProperties {
        /**
         * 签名私钥
         */
        private String signingKey;
        /**
         * 验证公钥
         */
        private String verifierKey;

        public String getSigningKey() {
            return signingKey;
        }

        public void setSigningKey(String signingKey) {
            this.signingKey = signingKey;
        }

        public String getVerifierKey() {
            return verifierKey;
        }

        public void setVerifierKey(String verifierKey) {
            this.verifierKey = verifierKey;
        }
    }

    public static class Jwt {

        private JwtAccessTokenConverter converter;

        public Jwt(JwtAccessTokenConverter jwtAccessTokenConverter) {
            this.converter = jwtAccessTokenConverter;
        }

        public OAuth2AccessToken convert(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            if (converter == null)
                ErrorEnum.OAUTH_ERROR.details("Jwt is not support, cause RSA key not set.").throwException();
            return this.converter.enhance(accessToken, authentication);
        }

        public Map<String, ?> keys() {
            if (converter == null)
                ErrorEnum.OAUTH_ERROR.details("Jwt is not support, cause RSA key not set.").throwException();
            return converter.getKey();
        }

    }
}
