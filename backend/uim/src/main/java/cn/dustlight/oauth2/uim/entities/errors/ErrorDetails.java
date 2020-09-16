package cn.dustlight.oauth2.uim.entities.errors;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {

    private int code;
    private String message;
    private String details;
    private ErrorDetailsException errorDetailsException;

    public ErrorDetails(int code, String message) {
        this.code = code;
        this.message = message;
        errorDetailsException = new ErrorDetailsException(this);
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

    public void setErrorDetailsException(ErrorDetailsException errorDetailsException) {
        this.errorDetailsException = errorDetailsException;
    }

    public void throwException() throws ErrorDetailsException {
        throw errorDetailsException;
    }

    public static class ErrorDetailsException extends RuntimeException {
        private ErrorDetails errorDetails;

        public ErrorDetailsException(ErrorDetails errorDetails) {
            super(errorDetails.message);
            this.errorDetails = errorDetails;
        }

        public ErrorDetails getErrorDetails() {
            return errorDetails;
        }
    }
}
