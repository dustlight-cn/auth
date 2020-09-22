package cn.dustlight.validator.core;

import cn.dustlight.validator.annotations.SendCode;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.BeanFactory;

public class SendCodeAdvisor extends AbstractPointcutAdvisor {

    private SendCodeInterceptor interceptor = new SendCodeInterceptor();

    public Pointcut getPointcut() {
        return new AnnotationMatchingPointcut((Class) null, SendCode.class, true);
    }

    public Advice getAdvice() {
        return interceptor;
    }

    public BeanFactory getFactory() {
        return interceptor.getFactory();
    }

    public void setFactory(BeanFactory factory) {
        interceptor.setFactory(factory);
    }

    @Override
    public int getOrder() {
        return interceptor.getOrder();
    }

    @Override
    public void setOrder(int order) {
        interceptor.setOrder(order);
    }
}
