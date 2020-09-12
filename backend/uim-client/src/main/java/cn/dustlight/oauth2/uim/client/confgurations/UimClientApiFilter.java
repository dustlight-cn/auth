package cn.dustlight.oauth2.uim.client.confgurations;

import cn.dustlight.oauth2.uim.client.entity.IUimUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UimClientApiFilter implements Filter {

    private ObjectMapper mapper;
    private String userInfoPath;
    private String clientInfoPath;
    private AntPathMatcher antPathMatcher;
    private ClientRegistrationRepository clientRegistrationRepository;

    public UimClientApiFilter(ClientRegistrationRepository clientRegistrationRepository) {
        this(null, null, clientRegistrationRepository, null);
    }

    public UimClientApiFilter(String userInfoPath,
                              String clientInfoPath,
                              ClientRegistrationRepository clientRegistrationRepository,
                              ObjectMapper mapper) {
        if (clientInfoPath != null)
            this.clientInfoPath = clientInfoPath;
        else
            this.clientInfoPath = "/api/user";
        if (userInfoPath != null)
            this.userInfoPath = userInfoPath;
        else
            this.userInfoPath = "/api/clients";
        if (mapper != null)
            this.mapper = mapper;
        else
            this.mapper = new ObjectMapper();
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.antPathMatcher = new AntPathMatcher();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (antPathMatcher.match(userInfoPath, request.getServletPath())) {
            Authentication authorization = SecurityContextHolder.getContext().getAuthentication();
            if (authorization == null)
                response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            else {
                if (authorization.getPrincipal() instanceof IUimUser) {
                    IUimUser user = (IUimUser) authorization.getPrincipal();
                    response.setContentType("application/json;charset=utf-8");
                    mapper.writeValue(response.getWriter(), user);
                } else {
                    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                }
            }
            return;
        } else if (antPathMatcher.match(clientInfoPath, request.getServletPath())) {
            Iterable<ClientRegistration> clientRegistrations = null;
            ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
            if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
                clientRegistrations = (Iterable) clientRegistrationRepository;
            }
            if (clientRegistrations == null) {
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            } else {
                List<String> result = new ArrayList<>();
                clientRegistrations.forEach((registration) -> {
                    result.add(registration.getRegistrationId());
                });

                response.setContentType("application/json;charset=utf-8");
                mapper.writeValue(response.getWriter(), result);
            }
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
