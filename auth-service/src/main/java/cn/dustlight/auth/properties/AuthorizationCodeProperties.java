package cn.dustlight.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dustlight.auth.service.authorization-code")
public class AuthorizationCodeProperties {

    private String keyPrefix = "auth_code:";
    private Long duration = 1000 * 60 * 15L;

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
