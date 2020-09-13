package cn.dustlight.oauth2.uim.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

import java.time.Duration;

public class RedisAuthorizationCodeService extends RandomValueAuthorizationCodeServices {

    private String keyPrefix = "auth_code:";
    private RedisTemplate redisTemplate;
    private long duration = 1000 * 60 * 15; // 15 min


    public RedisAuthorizationCodeService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void store(String s, OAuth2Authentication oAuth2Authentication) {
        redisTemplate.opsForValue().set(keyPrefix + s, oAuth2Authentication, Duration.ofMillis(duration));
    }

    @Override
    protected OAuth2Authentication remove(String s) {
        String k = keyPrefix + s;
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) redisTemplate.opsForValue().get(k);
        redisTemplate.delete(k);
        return oAuth2Authentication;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }
}
