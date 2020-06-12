package cn.dustlight.uim;

import cn.dustlight.uim.utils.ExceptionSupplier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@EnableAuthorizationServer
@EnableResourceServer
@SpringBootApplication
public class UimApplication {

    public static void main(String[] args) {
        SpringApplication.run(UimApplication.class, args);
    }

    @RestController
    @RestControllerAdvice
    public static class HelloController {

        @GetMapping("/hello")
        public RestfulResult hello(OAuth2Authentication user, @RequestParam(required = false) String client) {
            Map<String, Object> userInfo = new HashMap<>();

            userInfo.put("user", user.getUserAuthentication().getName());
            userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
            userInfo.put("authorities2",user.getOAuth2Request().getAuthorities());
            return RestfulResult.success(userInfo);
        }

        @ExceptionHandler(Exception.class)
        public RestfulResult onException(Exception e) throws UnsupportedEncodingException {
            Logger.getLogger(UimApplication.class.getName()).warning(new ExceptionSupplier(e));
            return RestfulResult.error().setData(e.toString());
        }
    }
}