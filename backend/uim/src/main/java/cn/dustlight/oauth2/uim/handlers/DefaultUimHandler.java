package cn.dustlight.oauth2.uim.handlers;

import cn.dustlight.oauth2.uim.models.IUserDetails;
import cn.dustlight.oauth2.uim.models.errors.ErrorEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultUimHandler implements UimHandler {

    private static final String CONTENT_TYPE = "application/json;charset=utf-8";
    private ObjectMapper mapper;

    public DefaultUimHandler(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
    }

    @Override
    public void handleAuthenticationEntryPoint(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        sendObject(HttpStatus.UNAUTHORIZED.value(), ErrorEnum.UNAUTHORIZED.message(e.getMessage()), httpServletResponse);
    }

    @Override
    public void handleAccessDenied(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        sendObject(HttpStatus.UNAUTHORIZED.value(), ErrorEnum.UNAUTHORIZED.message(e.getMessage()), httpServletResponse);
    }

    @Override
    public void handleSignInSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
    }

    @Override
    public void handleSignInFail(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        sendObject(HttpStatus.BAD_REQUEST.value(), ErrorEnum.SIGN_IN_FAIL.message(e.getMessage()), httpServletResponse);
    }

    @Override
    public void handleLogout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        sendObject(HttpStatus.OK.value(), ErrorEnum.NO_ERRORS.getDetails(), httpServletResponse);
    }

    protected void sendObject(int status, Object object, HttpServletResponse response) throws IOException {
        response.setStatus(status);
        response.setContentType(CONTENT_TYPE);
        mapper.writeValue(response.getWriter(), object);
    }
}
