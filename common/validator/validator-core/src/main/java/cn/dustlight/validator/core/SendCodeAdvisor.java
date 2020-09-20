package cn.dustlight.validator.core;

import cn.dustlight.validator.annotation.SendCode;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.BeanFactory;

public class SendCodeAdvisor extends AbstractPointcutAdvisor {

    private BeanFactory factory;

    public SendCodeAdvisor(BeanFactory factory) {
        this.factory = factory;
    }

    public Pointcut getPointcut() {
        return new AnnotationMatchingPointcut((Class) null, SendCode.class, true);
    }

    public Advice getAdvice() {
        return new SendCodeInterceptor(factory);
    }
}
