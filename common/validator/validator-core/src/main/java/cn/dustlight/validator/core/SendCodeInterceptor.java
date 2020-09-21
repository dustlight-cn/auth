package cn.dustlight.validator.core;

import cn.dustlight.validator.Util;
import cn.dustlight.validator.annotations.CodeParam;
import cn.dustlight.validator.annotations.CodeValue;
import cn.dustlight.validator.annotations.SendCode;
import cn.dustlight.validator.generator.CodeGenerator;
import cn.dustlight.validator.sender.CodeSender;
import cn.dustlight.validator.store.CodeStore;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class SendCodeInterceptor implements MethodInterceptor, Ordered {

    private BeanFactory factory;
    private int order = Ordered.LOWEST_PRECEDENCE;

    public SendCodeInterceptor(BeanFactory factory) {
        this.factory = factory;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();

        SendCode sendCodeAnnotation = method.getAnnotation(SendCode.class); // 获取注解

        /*
         * 获取Bean
         */
        CodeGenerator generator = Util.getBean(factory, sendCodeAnnotation.generator().value(), sendCodeAnnotation.generator().type());
        CodeStore store = Util.getBean(factory, sendCodeAnnotation.store().value(), sendCodeAnnotation.store().type());
        CodeSender sender = Util.getBean(factory, sendCodeAnnotation.sender().value(), sendCodeAnnotation.sender().type());

        Map<String, Object> parameters = Util.getParameters(methodInvocation); // 获取方法参数列表
        for (cn.dustlight.validator.annotations.Parameter parameter : sendCodeAnnotation.parameters())
            parameters.put(parameter.name(), parameter.value()); // 获取注解参数

        Code code = generator.generate(sendCodeAnnotation.value(), parameters); // 生成验证码
        Object codeValue = code.getValue();

        Parameter[] params = method.getParameters();
        Object[] objects = methodInvocation.getArguments();
        if (params != null && objects != null) {
            for (int i = 0, len = Math.min(params.length, objects.length); i < len; i++) {
                CodeParam codeParamAnnotation;
                CodeValue codeValueAnnotation;
                if ((codeValueAnnotation = params[i].getAnnotation(CodeValue.class)) != null &&
                        codeValueAnnotation.value().equals(sendCodeAnnotation.value())) {
                    objects[i] = codeValue; // 往验证码值注入方法参数
                    parameters.put(params[i].getName(), codeValue); // 更新参数表
                } else if ((codeParamAnnotation = params[i].getAnnotation(CodeParam.class)) != null &&
                        codeParamAnnotation.value().equals(sendCodeAnnotation.value())) {
                    String key = codeParamAnnotation.value().length() > 0 ? codeParamAnnotation.value() : params[i].getName();
                    Object value = objects[i] != null ? objects[i] : codeParamAnnotation.defaultValue();
                    code.getData().put(key, value); // 存储验证码参数
                }
            }
        }

        store.store(code, sendCodeAnnotation.duration(), parameters); // 存储验证码
        sender.send(code, parameters); // 发送验证码

        return methodInvocation.proceed();
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
