package cn.dustlight.validator.verifier;

public class VerifyFailException extends VerifyCodeException {

    public VerifyFailException(String message) {
        super(message);
    }

    public VerifyFailException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
