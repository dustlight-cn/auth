package cn.dustlight.validator.store;

import cn.dustlight.validator.ValidatorException;

public class RemoveCodeException extends ValidatorException {

    public RemoveCodeException(String message) {
        super(message);
    }

    public RemoveCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
