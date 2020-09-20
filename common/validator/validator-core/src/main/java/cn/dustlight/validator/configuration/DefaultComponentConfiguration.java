package cn.dustlight.validator.configuration;

import cn.dustlight.validator.core.SendCodePostProcessor;
import cn.dustlight.validator.core.VerifyCodePostProcessor;
import cn.dustlight.validator.generator.CodeGenerator;
import cn.dustlight.validator.generator.RandomStringCodeGenerator;
import cn.dustlight.validator.sender.CodeSender;
import cn.dustlight.validator.sender.SimpleImageCodeSender;
import cn.dustlight.validator.store.CodeStore;
import cn.dustlight.validator.store.HttpSessionCodeStore;
import cn.dustlight.validator.verifier.CodeVerifier;
import cn.dustlight.validator.verifier.StringCodeVerifier;
import org.springframework.context.annotation.Bean;

public class DefaultComponentConfiguration {

    @Bean
    public SendCodePostProcessor sendCodePostProcessor() {
        return new SendCodePostProcessor();
    }

    @Bean
    public VerifyCodePostProcessor verifyCodePostProcessor() {
        return new VerifyCodePostProcessor();
    }

    @Bean
    public CodeGenerator defaultCodeGenerator() {
        RandomStringCodeGenerator generator = new RandomStringCodeGenerator();
        return generator;
    }

    @Bean
    public CodeStore defaultCodeStore() {
        HttpSessionCodeStore store = new HttpSessionCodeStore();
        return store;
    }

    @Bean
    public CodeSender defaultCodeSender() {
        SimpleImageCodeSender sender = new SimpleImageCodeSender();
        return sender;
    }

    @Bean
    public CodeVerifier defaultCodeVerifier() {
        StringCodeVerifier verifier = new StringCodeVerifier();
        return verifier;
    }
}
