package cn.dustlight.oauth2.uim;

import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestfulConstants {

    public static final RestfulResult SUCCESS = RestfulResult.success();

    public static final String MSG_UNKNOWN_ERROR = "unknown error";
    public static final int CODE_UNKNOWN_ERROR = 0;
    public static final RestfulResult ERROR_UNKNOWN = new RestfulResult().setCode(CODE_UNKNOWN_ERROR).setMsg(MSG_UNKNOWN_ERROR);

    public static final String MSG_UNAUTHORIZED = "unauthorized";
    public static final int CODE_UNAUTHORIZED = 1;
    public static final RestfulResult ERROR_UNAUTHORIZED = RestfulResult.error(CODE_UNAUTHORIZED, MSG_UNAUTHORIZED);

    public static final String MSG_AUTHENTICATION_FAILURE = "authentication failure";
    public static final int CODE_AUTHENTICATION_FAILURE = 2;
    public static final RestfulResult ERROR_AUTHENTICATION_FAILURE = RestfulResult.error(CODE_AUTHENTICATION_FAILURE, MSG_AUTHENTICATION_FAILURE);

    public static final String MSG_ACCESS_DENIED = "access denied";
    public static final int CODE_ACCESS_DENIED = 3;
    public static final RestfulResult ERROR_ACCESS_DENIED = RestfulResult.error(CODE_ACCESS_DENIED, MSG_ACCESS_DENIED);

    public static final String MSG_PARAMETER_INVALID = "parameter invalid";
    public static final int CODE_PARAMETER_INVALID = 100;
    public static final RestfulResult ERROR_PARAMETER_INVALID = RestfulResult.error(CODE_PARAMETER_INVALID, MSG_PARAMETER_INVALID);
    public static final String MSG_EMAIL_INVALID = "email invalid";
    public static final int CODE_EMAIL_INVALID = 101;
    public static final RestfulResult ERROR_EMAIL_INVALID = RestfulResult.error(CODE_EMAIL_INVALID, MSG_EMAIL_INVALID);
    public static final String MSG_USERNAME_INVALID = "username invalid";
    public static final int CODE_USERNAME_INVALID = 102;
    public static final RestfulResult ERROR_USERNAME_INVALID = RestfulResult.error(CODE_USERNAME_INVALID, MSG_USERNAME_INVALID);
    public static final String MSG_PASSWORD_INVALID = "password invalid";
    public static final int CODE_PASSWORD_INVALID = 103;
    public static final RestfulResult ERROR_PASSWORD_INVALID = RestfulResult.error(CODE_PASSWORD_INVALID, MSG_PASSWORD_INVALID);
    public static final String MSG_PHONE_INVALID = "phone invalid";
    public static final int CODE_PHONE_INVALID = 104;
    public static final RestfulResult ERROR_PHONE_INVALID = RestfulResult.error(CODE_PHONE_INVALID, MSG_PHONE_INVALID);
    public static final String MSG_VERIFICATION_CODE_INVALID = "verification code invalid";
    public static final int CODE_VERIFICATION_CODE_INVALID = 105;
    public static final RestfulResult ERROR_VERIFICATION_CODE_INVALID = RestfulResult.error(CODE_VERIFICATION_CODE_INVALID, MSG_VERIFICATION_CODE_INVALID);

    public static final String MSG_ALREADY_EXISTS = "already exists";
    public static final int CODE_ALREADY_EXISTS = 200;
    public static final RestfulResult ERROR_ALREADY_EXISTS = RestfulResult.error(CODE_ALREADY_EXISTS, MSG_ALREADY_EXISTS);
    public static final String MSG_USERNAME_EXISTS = "username exists";
    public static final int CODE_USERNAME_EXISTS = 201;
    public static final RestfulResult ERROR_USERNAME_EXISTS = RestfulResult.error(CODE_USERNAME_EXISTS, MSG_USERNAME_EXISTS);
    public static final String MSG_EMAIL_EXISTS = "email exists";
    public static final int CODE_EMAIL_EXISTS = 202;
    public static final RestfulResult ERROR_EMAIL_EXISTS = RestfulResult.error(CODE_EMAIL_EXISTS, MSG_EMAIL_EXISTS);
    public static final String MSG_PHONE_EXISTS = "phone exists";
    public static final int CODE_PHONE_EXISTS = 203;
    public static final RestfulResult ERROR_PHONE_EXISTS = RestfulResult.error(CODE_PHONE_EXISTS, MSG_PHONE_EXISTS);

    public static final String MSG_NOT_FOUND = "not found";
    public static final int CODE_NOT_FOUND = 300;
    public static final RestfulResult ERROR_NOT_FOUND = RestfulResult.error(CODE_NOT_FOUND, MSG_NOT_FOUND);
    public static final String MSG_USER_NOT_FOUND = "user not found";
    public static final int CODE_USER_NOT_FOUND = 301;
    public static final RestfulResult ERROR_USER_NOT_FOUND = RestfulResult.error(CODE_USER_NOT_FOUND, MSG_USER_NOT_FOUND);


    /* Static methods */
    private static final String ContentType = "text/json;charset=utf-8";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void echo(RestfulResult restfulResult, HttpServletResponse response) throws IOException {
        response.setContentType(ContentType);
        mapper.writeValue(response.getWriter(), restfulResult);
    }

    public static void success(HttpServletResponse response) throws IOException {
        echo(SUCCESS, response);
    }

    public static void success(HttpServletResponse response, Object data) throws IOException {
        echo(RestfulResult.success(data), response);
    }
}
