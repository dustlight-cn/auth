package cn.dustlight.oauth2.uim;

import cn.dustlight.oauth2.uim.utils.ExceptionSupplier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;


@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
public class UimApplication {

    public static void main(String[] args) {
        SpringApplication.run(UimApplication.class, args);
    }

    @RestController
    @RestControllerAdvice
    public static class HelloController {

        @GetMapping("/hello")
        public RestfulResult hello(HttpServletRequest request) {
            return RestfulResult.success().setMsg("Hello World").setData(request.getParameterMap());
        }

        @GetMapping("/oauth_info")
        public RestfulResult oauthInfo(OAuth2Authentication user) {
            if (user.getDetails() instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) user.getDetails();
                if (details.getRemoteAddress().equals("127.0.0.1") || details.getRemoteAddress().equals("0:0:0:0:0:0:0:1"))
                    return RestfulResult.success(user);
            }
            return RestfulResult.error("Only access IP from '0:0:0:0:0:0:0:1' or '127.0.0.1'.");
        }

        @ExceptionHandler(Exception.class)
        public RestfulResult onException(Exception e) throws UnsupportedEncodingException {
            if (e instanceof AccessDeniedException)
                return RestfulConstants.ERROR_ACCESS_DENIED;
            Logger.getLogger(UimApplication.class.getName()).warning(new ExceptionSupplier(e));
            return RestfulResult.error().setData(e.getMessage());
        }
    }
}