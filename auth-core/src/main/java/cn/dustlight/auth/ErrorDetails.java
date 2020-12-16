package cn.dustlight.auth;

public class ErrorDetails {

    private String message;
    private int code;
    private String details;
    private AuthException authException;

    public ErrorDetails(int code, String message) {
        this.code = code;
        this.message = message;
        authException = new AuthException(this);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ErrorDetails message(String message) {
        this.message = message;
        return this;
    }

    public ErrorDetails code(int code) {
        this.code = code;
        return this;
    }

    public ErrorDetails details(String details) {
        this.details = details;
        return this;
    }

    public void setAuthException(AuthException authException) {
        this.authException = authException;
    }

    public void throwException() throws AuthException {
        throw authException;
    }
}
