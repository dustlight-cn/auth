package cn.dustlight.validator.core;

import cn.dustlight.validator.Util;
import cn.dustlight.validator.annotations.CodeParam;
import cn.dustlight.validator.annotations.CodeValue;
import cn.dustlight.validator.annotations.VerifyCode;
import cn.dustlight.validator.store.CodeStore;
import cn.dustlight.validator.verifier.CodeVerifier;
import cn.dustlight.validator.verifier.VerifyCodeException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class VerifyCodeInterceptor implements MethodInterceptor, Ordered {

    private BeanFactory factory;
    private int order = 1;

    public VerifyCodeInterceptor(BeanFactory factory) {
        this.factory = factory;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();

        VerifyCode verifyCodeAnnotation = method.getAnnotation(VerifyCode.class);

        /**
         * 获取Bean
         */
        CodeStore store = Util.getBean(factory, verifyCodeAnnotation.store().value(), verifyCodeAnnotation.store().type());
        CodeVerifier verifier = Util.getBean(factory, verifyCodeAnnotation.verifier().value(), verifyCodeAnnotation.verifier().type());

        Map<String, Object> parameters = Util.getParameters(methodInvocation); // 获取方法参数
        for (cn.dustlight.validator.annotations.Parameter parameter : verifyCodeAnnotation.parameters())
            parameters.put(parameter.name(), parameter.value()); // 获取注解参数

        Code code = store.load(verifyCodeAnnotation.value(), parameters);
        Object codeValue = code.getValue();
        Object targetValue = parameters.get(verifyCodeAnnotation.value());

        Parameter[] params = method.getParameters();
        Object[] objects = methodInvocation.getArguments();
        if (params != null && objects != null) {
            for (int i = 0, len = Math.min(params.length, objects.length); i < len; i++) {
                CodeParam codeParamAnnotation;
                CodeValue codeValueAnnotation;
                if ((codeValueAnnotation = params[i].getAnnotation(CodeValue.class)) != null &&
                        codeValueAnnotation.value().equals(verifyCodeAnnotation.value())) {
                    targetValue = objects[i];
                    objects[i] = codeValue; // 往验证码值注入方法参数
                    parameters.put(params[i].getName(), codeValue); // 更新参数表
                } else if ((codeParamAnnotation = params[i].getAnnotation(CodeParam.class)) != null &&
                        codeParamAnnotation.value().equals(verifyCodeAnnotation.value())) {
                    String key = codeParamAnnotation.value().length() > 0 ? codeParamAnnotation.value() : params[i].getName();
                    Object value = code.getData() == null ? null : code.getData().get(key);
                    if (value == null && params[i].getType().isAssignableFrom(String.class))
                        value = codeParamAnnotation.defaultValue();
                    objects[i] = code.getData().getOrDefault(key, value); // 将验证码参数注入方法参数
                }
            }
        }

        try {
            verifier.verify(code, targetValue, parameters);
            store.remove(verifyCodeAnnotation.value()); // 验证成功删除验证码
        } catch (VerifyCodeException e) {
            // 验证失败
            addChanceCount(code); // 记录++
            if (checkChance(code, verifyCodeAnnotation.delete().onFail())) {
                store.store(code, null, parameters);
            } else {
                store.remove(verifyCodeAnnotation.value()); // 重试次数用光
            }
            throw e;
        }


        return methodInvocation.proceed();
    }

    boolean checkChance(Code code, int chance) {
        if (code.getData().get("CHANCE") == null)
            return true;
        Integer CHANCE = Integer.valueOf(code.getData().get("CHANCE").toString());
        if (CHANCE >= chance)
            return false;
        return true;
    }

    void addChanceCount(Code code) {
        if (code.getData().get("CHANCE") == null) {
            code.getData().put("CHANCE", 1);
            return;
        }
        Integer CHANCE = Integer.valueOf(code.getData().get("CHANCE").toString());
        code.getData().put("CHANCE", CHANCE + 1);
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}