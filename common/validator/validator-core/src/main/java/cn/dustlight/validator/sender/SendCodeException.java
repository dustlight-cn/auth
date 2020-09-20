package cn.dustlight.validator.sender;

import cn.dustlight.validator.ValidatorException;

public class SendCodeException extends ValidatorException {

    public SendCodeException(String message) {
        super(message);
    }

    public SendCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
