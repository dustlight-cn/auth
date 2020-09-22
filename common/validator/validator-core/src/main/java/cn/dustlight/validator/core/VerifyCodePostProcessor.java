package cn.dustlight.validator.core;

import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;

public class VerifyCodePostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    public VerifyCodePostProcessor() {
        this.advisor = new VerifyCodeAdvisor();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        ((VerifyCodeAdvisor) this.advisor).setFactory(beanFactory);
    }

    @Override
    public int getOrder() {
        return ((VerifyCodeAdvisor) this.advisor).getOrder();
    }

    @Override
    public void setOrder(int order) {
        ((VerifyCodeAdvisor) this.advisor).setOrder(order);
    }
}
