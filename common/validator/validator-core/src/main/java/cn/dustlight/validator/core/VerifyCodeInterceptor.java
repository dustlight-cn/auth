package cn.dustlight.validator.core;

import cn.dustlight.validator.BeanUtils;
import cn.dustlight.validator.annotation.VerifyCode;
import cn.dustlight.validator.store.CodeStore;
import cn.dustlight.validator.verifier.CodeVerifier;
import cn.dustlight.validator.verifier.VerifyCodeException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

public class VerifyCodeInterceptor implements MethodInterceptor {

    private BeanFactory factory;

    public VerifyCodeInterceptor(BeanFactory factory) {
        this.factory = factory;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        VerifyCode sendCode = method.getAnnotation(VerifyCode.class);

        CodeStore store = BeanUtils.getBean(factory, sendCode.store(), CodeStore.class);
        CodeVerifier verifier = BeanUtils.getBean(factory, sendCode.verifier(), CodeVerifier.class);
        Map<String, Object> parameters = new LinkedHashMap<>();
        if (method.getParameterCount() > 0) {
            Object[] arguments = methodInvocation.getArguments();
            Parameter[] parameters1 = method.getParameters();
            for (int i = 0, len = Math.min(arguments.length, parameters1.length); i < len; i++)
                parameters.put(parameters1[i].getName(), arguments[i]);
        }

        Code code = store.load(sendCode.value(), parameters);
        try {
            verifier.verify(code, sendCode.value(), parameters);
            store.remove(sendCode.value()); // 验证成功删除验证码
        } catch (VerifyCodeException e) {
            // 验证失败
            addChanceCount(code); // 记录++
            if (checkChance(code, sendCode.delete().onFail())) {
                store.store(code, sendCode.value(), null, parameters);
            } else {
                store.remove(sendCode.value()); // 重试次数用光
            }
            throw e;
        }


        return methodInvocation.proceed();
    }

    boolean checkChance(Code code, int chance) {
        if (code.getBody().get("CHANCE") == null)
            return true;
        Integer CHANCE = Integer.valueOf(code.getBody().get("CHANCE").toString());
        if (CHANCE >= chance)
            return false;
        return true;
    }

    void addChanceCount(Code code) {
        if (code.getBody().get("CHANCE") == null) {
            code.getBody().put("CHANCE", 1);
            return;
        }
        Integer CHANCE = Integer.valueOf(code.getBody().get("CHANCE").toString());
        code.getBody().put("CHANCE", CHANCE + 1);
    }
}
