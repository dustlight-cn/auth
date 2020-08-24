package cn.dustlight.oauth2.uim.configurations;

import cn.dustlight.oauth2.uim.RestfulConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    AntPathMatcher matcher;

    @Autowired
    private UimProperties properties;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String p = httpServletRequest.getServletPath();
        for (String path : properties.getRedirectToLoginPath()) {
            if (matcher.match(path, p)) {
                httpServletResponse.sendRedirect(properties.getFormLogin().getLoginPage() + "?redirect_back=true");
                return;
            }
        }
        RestfulConstants.echo(RestfulConstants.ERROR_UNAUTHORIZED.copy().setData(e.getMessage()), httpServletResponse);
    }

    @Bean
    protected AntPathMatcher createAntPathMatcher() {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher;
    }
}
