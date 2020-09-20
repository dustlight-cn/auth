package cn.dustlight.validator.verifier;

import cn.dustlight.validator.ValidatorException;

public class VerifyCodeException extends ValidatorException {

    public VerifyCodeException(String message) {
        super(message);
    }

    public VerifyCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
