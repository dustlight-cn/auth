package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.models.errors.ErrorDetails;
import cn.dustlight.oauth2.uim.models.errors.ErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@RestController
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ErrorDetails onException(Exception e, HttpServletResponse response) throws UnsupportedEncodingException {
        if (e instanceof ErrorDetails.ErrorDetailsException) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return ((ErrorDetails.ErrorDetailsException) e).getErrorDetails();
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ErrorEnum.UNKNOWN.message(e.getMessage());
    }
}
