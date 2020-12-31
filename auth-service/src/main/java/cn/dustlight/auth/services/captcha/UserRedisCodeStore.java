package cn.dustlight.auth.services.captcha;

import cn.dustlight.auth.entities.User;
import cn.dustlight.captcha.store.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpSession;

public class UserRedisCodeStore<T> extends RedisCodeStore<T> {

    private String keyPrefix;

    public UserRedisCodeStore(RedisConnectionFactory factory) {
        this(factory, "CAPTCHA_CODE");
    }

    public UserRedisCodeStore(RedisConnectionFactory factory, String keyPrefix) {
        super(factory, keyPrefix);
        this.keyPrefix = keyPrefix;
    }

    @Override
    protected String key(String codeName) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof OAuth2Authentication && authentication.getPrincipal() instanceof User) {
                return String.format("%s:%s:%s", keyPrefix, ((User) authentication.getPrincipal()).getUid(), codeName);
            } else {
                throw new StoreCodeException("Authentication is not OAuth2Authentication or user is not User.");
            }
        } catch (Exception e) {
            if (e instanceof StoreCodeException)
                throw e;
            throw new StoreCodeException("TokenRedisCodeStore Error: " + e.getMessage(), e);
        }
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    @Override
    protected HttpSession getSession(boolean createIfNull) {
        throw new StoreCodeException("Http Session is not supported.");
    }
}
