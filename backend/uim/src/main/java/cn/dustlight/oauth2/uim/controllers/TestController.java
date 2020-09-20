package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.Constants;
import cn.dustlight.validator.annotation.SendCode;
import cn.dustlight.validator.annotation.VerifyCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@RestController
@RequestMapping(Constants.V1.API_ROOT + "test/")
public class TestController {

    private static final Logger logger = Logger.getLogger("Test");

    @GetMapping("code")
    @SendCode
    public void test(HttpServletRequest request) {
        logger.info("send code");
    }

    @GetMapping("resource")
    @VerifyCode
    public String api(String code) {
        return "hello world";
    }
}
