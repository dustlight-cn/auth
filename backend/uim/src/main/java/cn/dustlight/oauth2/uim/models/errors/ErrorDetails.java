package cn.dustlight.oauth2.uim.models.errors;

public class ErrorDetails {

    private int code;
    private String message;
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
