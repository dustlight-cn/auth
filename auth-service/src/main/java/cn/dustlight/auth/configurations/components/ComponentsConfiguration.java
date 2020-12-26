package cn.dustlight.auth.configurations.components;

import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.services.captcha.VerifiedEmailSender;
import cn.dustlight.captcha.EmailSenderProperties;
import cn.dustlight.captcha.sender.CodeSender;
import cn.dustlight.captcha.sender.EmailCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ComponentsConfiguration {

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

}
