package cn.dustlight.oauth2.uim.client.confgurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


public class UimClientSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

    @Autowired
    private UimClientProperties properties;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestOAuth2AuthorizationRequestFilter restOAuth2AuthorizationRequestFilter;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    private static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (properties.isCsrfEnabled())
            http.csrf();
        else
            http.csrf().disable();
        if (properties.isApiEnabled()) {
            http.authorizeRequests().antMatchers("/api/clients").permitAll();
            http.addFilterBefore(new UimClientApiFilter("/api/user",
                    "/api/clients",
                    clientRegistrationRepository, objectMapper),
                    OAuth2AuthorizationRequestRedirectFilter.class);
        }
        http.authorizeRequests()
                .antMatchers(properties.getPublishPaths())
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutUrl(properties.getLogoutUrl())
                .and()
                .oauth2Client()
                .and()
                .oauth2Login()
                .loginProcessingUrl(properties.getLoginProcessingUrl())
                .authorizationEndpoint()
                .baseUri(properties.getAuthorizationRequestBaseUri())
                .and()
                .userInfoEndpoint()
                .userService(oAuth2UserService);
        if (properties.isRestfulEnabled()) {
            http.logout()
                    .logoutSuccessHandler(((httpServletRequest, httpServletResponse, authentication) -> httpServletResponse.setStatus(200)));
            http.exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler())
                    .and()
                    .oauth2Login()
                    .successHandler(successHandler())
                    .failureHandler(failHandler());
            http.addFilterBefore(restOAuth2AuthorizationRequestFilter, OAuth2AuthorizationRequestRedirectFilter.class);
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
            objectMapper.writeValue(httpServletResponse.getWriter(), authentication.getPrincipal());
        });
    }

    public static class RestOAuth2AuthorizationRequestFilter extends OncePerRequestFilter {

        private OAuth2AuthorizationRequestResolver authorizationRequestResolver;

        private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

        private ObjectMapper objectMapper;

        public RestOAuth2AuthorizationRequestFilter(OAuth2AuthorizationRequestResolver requestResolver,
                                                    AuthorizationRequestRepository<OAuth2AuthorizationRequest> requestAuthorizationRequestRepository,
                                                    ObjectMapper objectMapper) {
            this.authorizationRequestResolver = requestResolver;
            this.authorizationRequestRepository = requestAuthorizationRequestRepository;
            this.objectMapper = objectMapper;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            try {
                OAuth2AuthorizationRequest authorizationRequest = authorizationRequestResolver.resolve(request);
                if (authorizationRequest != null) {
                    if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(authorizationRequest.getGrantType())) {
                        authorizationRequestRepository.saveAuthorizationRequest(authorizationRequest, request, response);
                    }
                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("authorization_uri", authorizationRequest.getAuthorizationUri());
                    result.put("client_id", authorizationRequest.getClientId());
                    result.put("scopes", authorizationRequest.getScopes());
                    result.put("response_type", authorizationRequest.getResponseType().getValue());
                    result.put("state", authorizationRequest.getState());
                    result.put("redirect_uri", authorizationRequest.getRedirectUri());
                    response.setContentType(CONTENT_TYPE_JSON);
                    objectMapper.writeValue(response.getWriter(), result);
                    return;
                }
            } catch (Exception var11) {
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                response.getWriter().println(var11.getMessage());
                return;
            }
            filterChain.doFilter(request, response);
        }

        public OAuth2AuthorizationRequestResolver getAuthorizationRequestResolver() {
            return authorizationRequestResolver;
        }

        public void setAuthorizationRequestResolver(OAuth2AuthorizationRequestResolver authorizationRequestResolver) {
            this.authorizationRequestResolver = authorizationRequestResolver;
        }

        public AuthorizationRequestRepository<OAuth2AuthorizationRequest> getAuthorizationRequestRepository() {
            return authorizationRequestRepository;
        }

        public void setAuthorizationRequestRepository(AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository) {
            this.authorizationRequestRepository = authorizationRequestRepository;
        }

        public ObjectMapper getObjectMapper() {
            return objectMapper;
        }

        public void setObjectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }
    }
}