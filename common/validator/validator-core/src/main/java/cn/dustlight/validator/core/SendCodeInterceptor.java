package cn.dustlight.validator.core;

import cn.dustlight.validator.Util;
import cn.dustlight.validator.annotation.SendCode;
import cn.dustlight.validator.generator.CodeGenerator;
import cn.dustlight.validator.sender.CodeSender;
import cn.dustlight.validator.store.CodeStore;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class SendCodeInterceptor implements MethodInterceptor {

    private BeanFactory factory;

    public SendCodeInterceptor(BeanFactory factory) {
        this.factory = factory;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();

        SendCode sendCode = method.getAnnotation(SendCode.class); // 获取注解

        /*
         * 获取Bean
         */
        CodeGenerator generator = Util.getBean(factory, sendCode.generator().value(), sendCode.generator().generatorClass());
        CodeStore store = Util.getBean(factory, sendCode.store().value(), sendCode.store().generatorClass());
        CodeSender sender = Util.getBean(factory, sendCode.sender().value(), sendCode.sender().generatorClass());

        Map<String, Object> parameters = Util.getParameters(methodInvocation); // 获取参数列表

        Code code = generator.generate(sendCode.value(), parameters);
        store.store(code, sendCode.duration(), parameters);
        sender.send(code, parameters);

        Object codeValue = code.getValue();

        Parameter[] params = method.getParameters();
        Object[] objects = methodInvocation.getArguments();
        if (params != null && objects != null)
            for (int i = 0, len = Math.min(params.length, objects.length); i < len; i++) {
                if (params[i].getName().equals(sendCode.parameter()) &&
                        params[i].getType().isAssignableFrom(codeValue.getClass())) {
                    objects[i] = codeValue;
                    break;
                }
            }
        return methodInvocation.proceed();
    }

}
