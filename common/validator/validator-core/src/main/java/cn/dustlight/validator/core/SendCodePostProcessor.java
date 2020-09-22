package cn.dustlight.validator.core;

import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;

public class SendCodePostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    public SendCodePostProcessor() {
        this.advisor = new SendCodeAdvisor();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        ((SendCodeAdvisor) this.advisor).setFactory(beanFactory);
    }

    @Override
    public int getOrder() {
        return ((SendCodeAdvisor) this.advisor).getOrder();
    }

    @Override
    public void setOrder(int order) {
        ((SendCodeAdvisor) this.advisor).setOrder(order);
    }
}
