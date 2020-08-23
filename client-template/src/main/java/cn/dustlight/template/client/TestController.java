package cn.dustlight.template.client;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TestController {

    @RequestMapping("")
    public String index(Authentication authorization, Model model) {

        model.addAttribute("a", authorization);
        return "index";
    }
}
