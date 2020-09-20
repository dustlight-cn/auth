package cn.dustlight.validator.generator;

import cn.dustlight.validator.ValidatorException;

public class GenerateCodeException extends ValidatorException {

    public GenerateCodeException(String message) {
        super(message);
    }

    public GenerateCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
