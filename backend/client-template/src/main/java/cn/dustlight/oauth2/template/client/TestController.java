package cn.dustlight.oauth2.template.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class TestController {

    @Autowired
    OAuth2RestTemplate template;

    @RequestMapping("")
    public String index(Authentication authorization, Model model) {
        ResponseEntity<HashMap> userinfo = template.getForEntity("https://account.dustlight.cn/api/res/details", HashMap.class);
        model.addAttribute("b", (HashMap)userinfo.getBody().get("data"));
        model.addAttribute("a", authorization);
        return "index";
    }
}
