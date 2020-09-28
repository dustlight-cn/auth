package cn.dustlight.oauth2.uim.entities.errors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum ErrorEnum {
    NO_ERRORS(-1, "no errors"),
    UNKNOWN(0, "unknown error"),
    UNAUTHORIZED(1, "unauthorized"),
    ACCESS_DENIED(2, "access denied"),
    SIGN_IN_FAIL(3, "sign in fail"),
    REGISTER_FAIL(4, "register fail"),
    VERIFY_FAIL(5, "verify fail"),
    OAUTH_ERROR(10, "OAuth2 Error"),

    INPUT_INVALID(1000, "input invalid"),
    EMAIL_INVALID(1001, "email invalid"),
    USERNAME_INVALID(1002, "username invalid"),
    PASSWORD_INVALID(1003, "password invalid"),
    PHONE_INVALID(1004, "phone invalid"),
    CODE_INVALID(1005, "code invalid"),

    RESOURCE_NOT_FOUND(2000, "resource not found"),
    EMAIL_NOT_FOUND(2001, "email not found"),
    USER_NOT_FOUND(2002, "user not found"),

    RESOURCE_EXISTS(3000, "resource already exists"),
    EMAIL_EXISTS(3001, "email already exists"),
    USER_EXISTS(3001, "user already exists"),

    CREATE_RESOURCE_FAIL(4000, "fail to create resource"),
    CREATE_USER_FAIL(4001, "fail to create user"),
    CREATE_ROLE_FAIL(4002, "fail to create role"),

    UPDATE_RESOURCE_FAIL(5000, "fail to update resource"),
    UPDATE_USER_FAIL(5001, "fail to update user"),
    UPDATE_ROLE_FAIL(5002, "fail to update role"),

    DELETE_RESOURCE_FAIL(6000, "fail to delete resource"),
    DELETE_USER_FAIL(6001, "fail to delete user"),
    DELETE_ROLE_FAIL(6002, "fail to delete role");

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

    public String getErrorDetails() {
        return this.details.getDetails();
    }

    public ErrorDetails message(String message) {
        return new ErrorDetails(this.details.getCode(), message != null ? message : this.details.getMessage());
    }

    public ErrorDetails details(String details) {
        ErrorDetails instance = new ErrorDetails(this.details.getCode(), this.details.getMessage());
        instance.setDetails(details != null ? details : this.details.getDetails());
        return instance;
    }
}
