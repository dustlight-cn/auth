package cn.dustlight.auth.configurations;

import cn.dustlight.auth.ErrorDetails;
import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class HandlerConfiguration {

    private final static Log log = LogFactory.getLog(HandlerConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public AccessDeniedHandler accessDeniedHandler(@Autowired ObjectMapper mapper) {
        return (httpServletRequest, httpServletResponse, e) -> {
            log.debug(e.getMessage(), e);
            send(httpServletResponse,
                    HttpStatus.FORBIDDEN,
                    ErrorEnum.ACCESS_DENIED.details(e.getMessage()),
                    mapper);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint authenticationEntryPoint(@Autowired ObjectMapper mapper) {
        return (httpServletRequest, httpServletResponse, e) -> {
            log.debug(e.getMessage(), e);
            send(httpServletResponse,
                    HttpStatus.FORBIDDEN,
                    ErrorEnum.UNAUTHORIZED.details(e.getMessage()),
                    mapper);
        };
    }

    private static void send(HttpServletResponse response,
                             HttpStatus status,
                             ErrorDetails details,
                             ObjectMapper objectMapper) throws IOException {
        response.setStatus(status.value());
        response.setContentType(Constants.ContentType.APPLICATION_JSON);
        try (Writer writer = response.getWriter()) {
            objectMapper.writeValue(writer, details);
        }
    }
}
