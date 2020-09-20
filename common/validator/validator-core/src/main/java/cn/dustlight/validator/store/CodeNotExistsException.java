package cn.dustlight.validator.store;

public class CodeNotExistsException extends StoreCodeException {

    public CodeNotExistsException(String message) {
        super(message);
    }

    public CodeNotExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
