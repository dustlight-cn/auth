package cn.dustlight.auth;

public class AuthException extends RuntimeException {

    private ErrorDetails errorDetails;

    public AuthException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    @Override
    public String toString() {
        return errorDetails == null ? super.toString() : errorDetails.toString();
    }
}
