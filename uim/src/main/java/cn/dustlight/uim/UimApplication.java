package cn.dustlight.uim;

import cn.dustlight.uim.utils.ExceptionSupplier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@SpringBootApplication
public class UimApplication {

    public static void main(String[] args) {
        SpringApplication.run(UimApplication.class, args);
    }

    @RestController
    @RestControllerAdvice
    public static class HelloController {

        @GetMapping("/hello")
        public RestfulResult hello(OAuth2Authentication user) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("Username", user.getUserAuthentication().getName());
            userInfo.put("User Authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
            userInfo.put("OAuth2 Authorities", AuthorityUtils.authorityListToSet(user.getOAuth2Request().getAuthorities()));
            userInfo.put("Client-id",user.getOAuth2Request().getClientId());
            userInfo.put("Extensions",user.getOAuth2Request().getExtensions());
            return RestfulResult.success(userInfo);
        }

        @ExceptionHandler(Exception.class)
        public RestfulResult onException(Exception e) throws UnsupportedEncodingException {
            Logger.getLogger(UimApplication.class.getName()).warning(new ExceptionSupplier(e));
            return RestfulResult.error().setData(e.toString());
        }
    }
}