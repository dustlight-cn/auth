package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.entities.errors.ErrorDetails;
import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.validator.ValidatorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RestControllerAdvice
public class ExceptionController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @ExceptionHandler(Throwable.class)
    public ErrorDetails onException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("Unknown error. path: " + request.getContextPath() +
                        ", remote: " + request.getRemoteAddr(),
                e);
        return ErrorEnum.UNKNOWN.getDetails();
    }

    @ExceptionHandler(ErrorDetails.ErrorDetailsException.class)
    public ErrorDetails onErrorException(ErrorDetails.ErrorDetailsException e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return e.getErrorDetails();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ErrorDetails onAuthenticationException(AuthenticationException e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ErrorEnum.SIGN_IN_FAIL.details(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorDetails onAccessDeniedException(AccessDeniedException e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return ErrorEnum.ACCESS_DENIED.details(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorDetails onNotFound(NoHandlerFoundException e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return ErrorEnum.RESOURCE_NOT_FOUND.getDetails();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ErrorDetails onMissingServletRequestParameter(MissingServletRequestParameterException e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ErrorEnum.INPUT_INVALID.details(e.getMessage());
    }

    @ExceptionHandler(ValidatorException.class)
    public ErrorDetails onValidatorException(ValidatorException e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        logger.warn(e.getMessage());
        return ErrorEnum.VERIFY_FAIL.details(e.getMessage());
    }

    @ExceptionHandler(ServletException.class)
    public ErrorDetails onServletException(ServletException e, HttpServletRequest request, HttpServletResponse response) {
        Integer statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        if (request != null && request.getAttribute("javax.servlet.error.status_code") != null)
            statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        response.setStatus(statusCode);
        return ErrorEnum.UNKNOWN.details(e.getMessage());
    }
}
