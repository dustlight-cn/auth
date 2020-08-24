package cn.dustlight.oauth2.uim.configurations;

import cn.dustlight.oauth2.uim.RestfulConstants;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.dustlight.oauth2.uim.RestfulConstants.ERROR_AUTHENTICATION_FAILURE;

@Component
public class SignInFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        RestfulConstants.echo(ERROR_AUTHENTICATION_FAILURE.setData(e.getMessage()),httpServletResponse);
    }
}
