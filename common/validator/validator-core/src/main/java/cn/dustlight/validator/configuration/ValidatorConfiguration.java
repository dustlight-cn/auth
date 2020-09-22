package cn.dustlight.validator.configuration;

import cn.dustlight.validator.annotations.EnableValidator;
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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

@Configuration(
        proxyBeanMethods = false
)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ValidatorConfiguration implements ImportAware {

    @Nullable
    protected AnnotationAttributes attrs;

    @Override
    public void setImportMetadata(AnnotationMetadata annotationMetadata) {
        this.attrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableValidator.class.getName(), false));
        if (this.attrs == null) {
            throw new IllegalArgumentException("@EnableValidator is not present on importing class " + annotationMetadata.getClassName());
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SendCodePostProcessor sendCodePostProcessor() {
        SendCodePostProcessor sendCodePostProcessor = new SendCodePostProcessor();
        if (attrs != null) {
            sendCodePostProcessor.setProxyTargetClass(attrs.getBoolean("proxyTargetClass"));
            sendCodePostProcessor.setOrder(attrs.getNumber("orderOfSend"));
        }
        return sendCodePostProcessor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public VerifyCodePostProcessor verifyCodePostProcessor() {
        VerifyCodePostProcessor verifyCodePostProcessor = new VerifyCodePostProcessor();
        if (attrs != null) {
            verifyCodePostProcessor.setProxyTargetClass(attrs.getBoolean("proxyTargetClass"));
            verifyCodePostProcessor.setOrder(attrs.getNumber("orderOfVerify"));
        }
        return verifyCodePostProcessor;
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
