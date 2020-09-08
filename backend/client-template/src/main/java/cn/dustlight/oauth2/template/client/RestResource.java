package cn.dustlight.oauth2.template.client;

import cn.dustlight.oauth2.uim.client.entity.UimUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/")
public class RestResource {

    @Autowired
    OAuth2RestOperations restTemplate;

    @RequestMapping("user")
    public Object userDetails(Authentication authentication) {
        return ((UimUser) authentication.getPrincipal());
    }

    @RequestMapping("request")
    public Object request(@RequestParam String url) {
        Logger.getLogger(RestResource.class.getName()).info("REQUEST:TOKEN: "
                + restTemplate.getOAuth2ClientContext().getAccessToken().isExpired());
        return restTemplate.getForEntity(url, Object.class).getBody();
    }
}
