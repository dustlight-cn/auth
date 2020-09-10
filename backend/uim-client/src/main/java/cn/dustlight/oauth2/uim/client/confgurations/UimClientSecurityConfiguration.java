package cn.dustlight.oauth2.uim.client.confgurations;

import cn.dustlight.oauth2.uim.client.UimClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@EnableConfigurationProperties(UimClientProperties.class)
public class UimClientSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

    @Autowired
    private UimClientProperties properties;

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    private static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Client()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2UserService);
        if (properties.isRestfulEnabled()) {
            http.exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler())
                    .and()
                    .oauth2Login()
                    .successHandler(successHandler())
                    .failureHandler(failHandler());
        }
    }

    protected AccessDeniedHandler accessDeniedHandler() {
        return ((httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.setStatus(403);
            httpServletResponse.getWriter().println(e.getMessage());
        });
    }

    protected AuthenticationEntryPoint authenticationEntryPoint() {
        return ((httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.setStatus(401);
            httpServletResponse.getWriter().println(e.getMessage());
        });
    }

    protected AuthenticationFailureHandler failHandler() {
        return ((httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.setStatus(400);
            httpServletResponse.getWriter().println(e.getMessage());
        });
    }

    protected AuthenticationSuccessHandler successHandler() {
        return ((httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.setStatus(200);
            httpServletResponse.setContentType(CONTENT_TYPE_JSON);
            converter.getObjectMapper().writeValue(httpServletResponse.getWriter(), authentication.getPrincipal());
        });
    }


}