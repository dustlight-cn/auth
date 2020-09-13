package cn.dustlight.oauth2.uim.models.errors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum ErrorEnum {
    UNKNOWN(-1, "unknown error"),
    NO_ERRORS(0, "no errors"),
    UNAUTHORIZED(1, "unauthorized"),
    ACCESS_DENIED(2, "access denied"),
    SIGN_IN_FAIL(3, "sign in fail");


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
