package cn.dustlight.oauth2.template.client;

import cn.dustlight.oauth2.uim.client.entity.UimUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class RestResource {

    @RequestMapping("user")
    public Object userDetails(Authentication authentication) {
        return ((UimUser) authentication.getPrincipal());
    }
}
