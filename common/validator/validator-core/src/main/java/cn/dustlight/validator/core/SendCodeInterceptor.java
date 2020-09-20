package cn.dustlight.validator.core;

import cn.dustlight.validator.BeanUtils;
import cn.dustlight.validator.annotation.SendCode;
import cn.dustlight.validator.generator.CodeGenerator;
import cn.dustlight.validator.sender.CodeSender;
import cn.dustlight.validator.store.CodeStore;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

public class SendCodeInterceptor implements MethodInterceptor {

    private BeanFactory factory;

    public SendCodeInterceptor(BeanFactory factory) {
        this.factory = factory;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        SendCode sendCode = method.getAnnotation(SendCode.class);
        CodeGenerator generator = BeanUtils.getBean(factory, sendCode.generator(), CodeGenerator.class);
        CodeStore store = BeanUtils.getBean(factory, sendCode.store(), CodeStore.class);
        CodeSender sender = BeanUtils.getBean(factory, sendCode.sender(), CodeSender.class);
        Map<String, Object> parameters = new LinkedHashMap<>();
        if (method.getParameterCount() > 0) {
            Object[] arguments = methodInvocation.getArguments();
            Parameter[] parameters1 = method.getParameters();
            for (int i = 0, len = Math.min(arguments.length, parameters1.length); i < len; i++)
                parameters.put(parameters1[i].getName(), arguments[i]);
        }

        Code code = generator.generate(sendCode.value(), parameters);
        store.store(code, sendCode.value(), sendCode.duration(), parameters);
        sender.send(code, sendCode.value(), parameters);
        return methodInvocation.proceed();
    }

}
