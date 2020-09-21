package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.Constants;
import cn.dustlight.validator.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping(value = Constants.V1.API_ROOT + "test/", produces = Constants.ContentType.APPLICATION_JSON)
public class TestController {

    private static final Logger logger = Logger.getLogger("Test");

    @GetMapping("code")
    @SendCode
    public void code(@Parameter(hidden = true) @CodeValue String code) {
        logger.info("Send code :" + code);
    }

    @GetMapping("verify")
    @VerifyCode
    @SendCode(value = "register",
            generator = @Generator(value = "numberCodeGenerator"),
            sender = @Sender(value = "emailCodeSender"),
            parameters = {
                    @cn.dustlight.validator.annotations.Parameter(name = "title", value = "邮箱验证"),
                    @cn.dustlight.validator.annotations.Parameter(name = "content", value = "您的注册验证码为：${code}")
            }
    )
    public void verify(@RequestParam @CodeValue String code,
                       @RequestParam @CodeParam String email) {
        logger.info("Send email code: " + code + " to: " + email);
    }

    @PostMapping("register")
    @VerifyCode(value = "register")
    public String register(@RequestParam @CodeValue String code,
                           @Parameter(hidden = true) @CodeParam String email) {
        return email;
    }
}
