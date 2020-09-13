package cn.dustlight.oauth2.uim.services.code;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

public class RedisVerificationCodeStoreService implements VerificationCodeStoreService {

    private String prefix = "ver_code:";
    private StringRedisTemplate template;

    public RedisVerificationCodeStoreService(StringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public void store(String key, String code, long duration) {
        if (key == null)
            throw new NullPointerException("verification key can not be null!");
        if (code == null)
            throw new NullPointerException("verification code can not be null!");
        template.opsForValue().set(key, code, Duration.ofMillis(duration));
    }

    @Override
    public String getCode(String key) {
        if (key == null)
            throw new NullPointerException("verification key can not be null!");
        return template.opsForValue().get(key);
    }

    @Override
    public void remove(String key) {
        if (key == null)
            throw new NullPointerException("verification key can not be null!");
        template.delete(key);
    }

    @Override
    public boolean verify(String key, String code) {
        if (key == null)
            throw new NullPointerException("verification key can not be null!");
        if (code == null)
            throw new NullPointerException("verification code can not be null!");
        return code.equals(template.opsForValue().get(key));
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public StringRedisTemplate getTemplate() {
        return template;
    }

    public void setTemplate(StringRedisTemplate template) {
        this.template = template;
    }
}
