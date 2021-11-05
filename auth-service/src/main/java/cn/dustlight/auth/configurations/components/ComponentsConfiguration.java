package cn.dustlight.auth.configurations.components;

import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.services.captcha.UserRedisCodeStore;
import cn.dustlight.auth.services.captcha.VerifiedEmailSender;
import cn.dustlight.auth.services.captcha.VerifiedSmsSender;
import cn.dustlight.captcha.AliyunSmsProperties;
import cn.dustlight.captcha.EmailSenderProperties;
import cn.dustlight.captcha.RedisCodeStoreProperties;
import cn.dustlight.captcha.TencentSmsProperties;
import cn.dustlight.captcha.sender.AliyunSmsSender;
import cn.dustlight.captcha.sender.CodeSender;
import cn.dustlight.captcha.sender.EmailCodeSender;
import cn.dustlight.captcha.sender.TencentSmsSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ComponentsConfiguration {

    private Log logger = LogFactory.getLog(getClass().getName());

    @Bean("passwordEncoder")
    @ConditionalOnMissingBean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("verifiedEmailSender")
    @ConditionalOnMissingBean(name = "verifiedEmailSender")
    public CodeSender<String> verifiedEmailSender(@Autowired UserService userService,
                                                  @Autowired JavaMailSender mailSender,
                                                  @Autowired MailProperties mailProperties,
                                                  @Autowired EmailSenderProperties emailSenderProperties) {
        return new VerifiedEmailSender(mailSender,
                mailProperties,
                emailSenderProperties,
                new EmailCodeSender.DefaultTemplateProvider(),
                userService);
    }

    @Bean("verifiedSmsSender")
    @ConditionalOnMissingBean(name = "verifiedSmsSender")
    public CodeSender<String> verifiedSmsSender(@Autowired UserService userService,
                                                @Autowired(required = false) TencentSmsSender tencentSmsSender,
                                                @Autowired(required = false) TencentSmsProperties tencentSmsProperties,
                                                @Autowired(required = false) AliyunSmsSender aliyunSmsSender,
                                                @Autowired(required = false) AliyunSmsProperties aliyunSmsProperties) {
        if (tencentSmsSender != null && tencentSmsProperties != null) {
            logger.info("SMS Code Sender Using : TencentSmsSender");
            return new VerifiedSmsSender(userService, tencentSmsSender, tencentSmsProperties.getPhoneParamName());
        }
        else if (aliyunSmsSender != null && aliyunSmsProperties != null) {
            logger.info("SMS Code Sender Using : AliyunSmsSender");
            return new VerifiedSmsSender(userService, aliyunSmsSender, aliyunSmsProperties.getPhoneParamName());
        }
        else {
            logger.warn("SMS Code Sender Using : None");
            return new VerifiedSmsSender(userService, null, "phone");
        }
    }

    @Bean("userCodeStore")
    @ConditionalOnMissingBean(name = "userCodeStore")
    public UserRedisCodeStore userCodeStore(@Autowired RedisConnectionFactory factory,
                                            @Autowired RedisCodeStoreProperties properties) {
        UserRedisCodeStore userRedisCodeStore = new UserRedisCodeStore(factory, properties.getKeyPrefix());
        return userRedisCodeStore;
    }
}
