package cn.dustlight.auth.configurations.security;

import cn.dustlight.captcha.annotations.EnableCaptcha;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableCaptcha
@ConditionalOnProperty(name = "dustlight.auth.captcha.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(CaptchaConfiguration.CaptchaProperties.class)
public class CaptchaConfiguration {

    @ConfigurationProperties(prefix = "dustlight.auth.captcha")
    public static class CaptchaProperties {
        /**
         * 是否启用 Captcha
         */
        private Boolean enabled = true;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }
}
