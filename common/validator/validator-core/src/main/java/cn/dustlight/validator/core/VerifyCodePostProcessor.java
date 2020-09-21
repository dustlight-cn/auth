package cn.dustlight.validator.core;

import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;

public class VerifyCodePostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        this.advisor = new VerifyCodeAdvisor(beanFactory);
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
