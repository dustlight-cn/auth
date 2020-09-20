package cn.dustlight.validator.store;

import cn.dustlight.validator.ValidatorException;

public class LoadCodeException extends ValidatorException {

    public LoadCodeException(String message) {
        super(message);
    }

    public LoadCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
