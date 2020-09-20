package cn.dustlight.validator.verifier;

import cn.dustlight.validator.core.Code;

import java.util.Map;

/**
 * 验证码校验器
 */
public interface CodeVerifier {

    void verify(Code code, Object key, Map<String, Object> parameters) throws VerifyCodeException;
}
