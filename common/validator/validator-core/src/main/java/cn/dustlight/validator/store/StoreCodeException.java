package cn.dustlight.validator.store;

import cn.dustlight.validator.ValidatorException;

public class StoreCodeException extends ValidatorException {

    public StoreCodeException(String message) {
        super(message);
    }

    public StoreCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
