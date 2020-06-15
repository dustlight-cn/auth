package cn.dustlight.sender.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleEmailConfiguration {

    @Bean
    public SimpleEmailSender createSimpleEmailSender() {
        return new SimpleEmailSender();
    }
}
