package cn.dustlight.validator.core;

import cn.dustlight.validator.annotation.VerifyCode;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.BeanFactory;

public class VerifyCodeAdvisor extends AbstractPointcutAdvisor {

    private BeanFactory factory;

    public VerifyCodeAdvisor(BeanFactory factory) {
        this.factory = factory;
    }

    public Pointcut getPointcut() {
        return new AnnotationMatchingPointcut((Class) null, VerifyCode.class, true);
    }

    public Advice getAdvice() {
        return new VerifyCodeInterceptor(factory);
    }
}
