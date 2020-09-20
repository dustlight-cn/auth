package cn.dustlight.validator.core;

import cn.dustlight.validator.Util;
import cn.dustlight.validator.annotation.VerifyCode;
import cn.dustlight.validator.store.CodeStore;
import cn.dustlight.validator.verifier.CodeVerifier;
import cn.dustlight.validator.verifier.VerifyCodeException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.Map;

public class VerifyCodeInterceptor implements MethodInterceptor {

    private BeanFactory factory;

    public VerifyCodeInterceptor(BeanFactory factory) {
        this.factory = factory;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        VerifyCode verifyCode = method.getAnnotation(VerifyCode.class);

        CodeStore store = Util.getBean(factory, verifyCode.store().value(), verifyCode.store().generatorClass());
        CodeVerifier verifier = Util.getBean(factory, verifyCode.verifier().value(), verifyCode.verifier().generatorClass());
        Map<String, Object> parameters = Util.getParameters(methodInvocation);

        Code code = store.load(verifyCode.value(), parameters);
        try {
            verifier.verify(code, verifyCode.value(), parameters);
            store.remove(verifyCode.value()); // 验证成功删除验证码
        } catch (VerifyCodeException e) {
            // 验证失败
            addChanceCount(code); // 记录++
            if (checkChance(code, verifyCode.delete().onFail())) {
                store.store(code, null, parameters);
            } else {
                store.remove(verifyCode.value()); // 重试次数用光
            }
            throw e;
        }


        return methodInvocation.proceed();
    }

    boolean checkChance(Code code, int chance) {
        if (code.getDate().get("CHANCE") == null)
            return true;
        Integer CHANCE = Integer.valueOf(code.getDate().get("CHANCE").toString());
        if (CHANCE >= chance)
            return false;
        return true;
    }

    void addChanceCount(Code code) {
        if (code.getDate().get("CHANCE") == null) {
            code.getDate().put("CHANCE", 1);
            return;
        }
        Integer CHANCE = Integer.valueOf(code.getDate().get("CHANCE").toString());
        code.getDate().put("CHANCE", CHANCE + 1);
    }
}
