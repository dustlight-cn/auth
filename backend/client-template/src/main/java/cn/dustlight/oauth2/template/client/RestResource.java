package cn.dustlight.oauth2.template.client;

import cn.dustlight.oauth2.template.client.results.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class RestResource {

    @Autowired
    OAuth2RestTemplate template;

    @RequestMapping("user")
    public Object userDetails() {
        ResponseEntity<UserResult> userinfo = template.getForEntity("https://account.dustlight.cn/api/res/details", UserResult.class);
        return userinfo;
    }
}
