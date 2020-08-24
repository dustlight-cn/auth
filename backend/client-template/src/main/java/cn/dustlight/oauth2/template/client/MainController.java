package cn.dustlight.oauth2.template.client;

import cn.dustlight.oauth2.template.client.results.UserDetails;
import cn.dustlight.oauth2.template.client.results.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    OAuth2RestTemplate template;

    @RequestMapping("")
    public String index(Authentication authorization, Model model) {

        if (authorization != null) {
            ResponseEntity<UserResult> userinfo = template.getForEntity("https://account.dustlight.cn/api/res/details", UserResult.class);
            UserDetails details = userinfo.getBody().getData();
            model.addAttribute("user", details);
            model.addAttribute("nickname", details.getNickname() != null && details.getNickname().trim().length() > 0 ?
                    details.getNickname() : details.getUsername());
            return "index";
        }
        return "index_unauthorized";
    }

}
