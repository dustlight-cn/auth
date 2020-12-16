package cn.dustlight.auth;


public enum ErrorEnum {

    NO_ERRORS(-1, "No errors"),
    UNKNOWN(0, "Unknown error"),
    UNAUTHORIZED(1, "Unauthorized"),
    ACCESS_DENIED(2, "Access denied"),
    SIGN_IN_FAIL(3, "Sign in fail"),
    REGISTER_FAIL(4, "Register fail"),
    VERIFY_FAIL(5, "Verify fail"),
    OAUTH_ERROR(10, "OAuth2 Error"),

    INPUT_INVALID(1000, "Input invalid"),
    EMAIL_INVALID(1001, "Email invalid"),
    USERNAME_INVALID(1002, "Username invalid"),
    PASSWORD_INVALID(1003, "Password invalid"),
    PHONE_INVALID(1004, "Phone invalid"),
    CODE_INVALID(1005, "Code invalid"),

    RESOURCE_NOT_FOUND(2000, "Resource not found"),
    EMAIL_NOT_FOUND(2001, "Email not found"),
    USER_NOT_FOUND(2002, "User not found"),
    CLIENT_NOT_FOUND(2003, "Client not found"),

    RESOURCE_EXISTS(3000, "Resource already exists"),
    EMAIL_EXISTS(3001, "Email already exists"),
    USER_EXISTS(3002, "User already exists"),
    CLIENT_EXISTS(3003, "Client already exists"),

    CREATE_RESOURCE_FAIL(4000, "Fail to create resource"),
    CREATE_USER_FAIL(4001, "Fail to create user"),
    CREATE_ROLE_FAIL(4002, "Fail to create role"),
    CREATE_AUTHORITY_FAIL(4003, "Fail to create authority"),
    CREATE_SCOPE_FAIL(4004, "Fail to create scope"),
    CREATE_CLIENT_FAIL(4005, "Fail to create client"),
    CREATE_GRANT_TYPE_FAIL(4006, "Fail to create grant type"),

    UPDATE_RESOURCE_FAIL(5000, "Fail to update resource"),
    UPDATE_USER_FAIL(5001, "Fail to update user"),
    UPDATE_ROLE_FAIL(5002, "Fail to update role"),
    UPDATE_AUTHORITY_FAIL(5003, "Fail to update authority"),
    UPDATE_SCOPE_FAIL(5004, "Fail to update scope"),
    UPDATE_CLIENT_FAIL(5005, "Fail to update client"),
    UPDATE_GRANT_TYPE_FAIL(5006, "Fail to update grant type"),

    DELETE_RESOURCE_FAIL(6000, "Fail to delete resource"),
    DELETE_USER_FAIL(6001, "Fail to delete user"),
    DELETE_ROLE_FAIL(6002, "Fail to delete role"),
    DELETE_AUTHORITY_FAIL(6003, "Fail to delete authority"),
    DELETE_SCOPE_FAIL(6004, "Fail to delete scope"),
    DELETE_CLIENT_FAIL(6005, "Fail to delete client"),
    DELETE_GRANT_TYPE_FAIL(6006, "Fail to delete grant type");

    private ErrorDetails details;

    ErrorEnum(int code, String message) {
        this.details = new ErrorDetails(code, message);
    }

    public void throwException() {
        this.details.throwException();
    }

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
