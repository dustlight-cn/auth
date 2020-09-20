package cn.dustlight.validator.verifier;

import cn.dustlight.validator.core.Code;

import java.util.Map;

/**
 * 验证码校验器
 */
public interface CodeVerifier<T> {

    void verify(Code<T> code, T target, Map<String, Object> parameters) throws VerifyCodeException;
}
