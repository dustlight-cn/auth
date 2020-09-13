package cn.dustlight.oauth2.uim.entities.errors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum ErrorEnum {
    NO_ERRORS(-1, "no errors"),
    UNKNOWN(0, "unknown error"),
    UNAUTHORIZED(1, "unauthorized"),
    ACCESS_DENIED(2, "access denied"),
    SIGN_IN_FAIL(3, "sign in fail"),
    REGISTER_FAIL(4, "register fail"),

    INPUT_INVALID(1000, "input invalid"),
    EMAIL_INVALID(1001, "email invalid"),
    USERNAME_INVALID(1002, "username invalid"),
    PASSWORD_INVALID(1003, "password invalid"),
    PHONE_INVALID(1004, "phone invalid"),
    CODE_INVALID(1005, "code invalid"),

    RESOURCE_NOT_FOUND(2000, "resource not found"),
    USER_NOT_FOUND(2001, "user not found"),

    RESOURCE_EXISTS(3000, "resource already exists"),
    EMAIL_EXISTS(3001, "email already exists");

    private ErrorDetails details;

    ErrorEnum(int code, String message) {
        this.details = new ErrorDetails(code, message);
    }

    public void throwException() {
        this.details.throwException();
    }

    @JsonIgnore
    public ErrorDetails getDetails() {
        return details;
    }

    public int getCode() {
        return this.details.getCode();
    }

    public String getMessage() {
        return this.details.getMessage();
    }

    public ErrorDetails message(String message) {
        return new ErrorDetails(this.details.getCode(), message != null ? message : this.details.getMessage());
    }
}
