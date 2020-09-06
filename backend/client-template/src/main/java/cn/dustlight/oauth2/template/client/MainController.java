package cn.dustlight.oauth2.template.client;

import cn.dustlight.oauth2.uim.client.entity.IUimUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @RequestMapping("")
    public String index(Authentication authorization, Model model) {
        if (authorization != null) {
            IUimUser details = ((IUimUser) authorization.getPrincipal());
            model.addAttribute("user", details);
            model.addAttribute("nickname", details.getNickname() != null && details.getNickname().trim().length() > 0 ?
                    details.getNickname() : details.getUsername());
            return "index";
        }
        return "index_unauthorized";
    }

}
