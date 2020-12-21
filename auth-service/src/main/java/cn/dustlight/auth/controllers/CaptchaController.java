package cn.dustlight.auth.controllers;

import cn.dustlight.auth.util.Constants;
import cn.dustlight.captcha.annotations.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "CAPTCHA", description = "验证码相关业务。")
@RestController
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public class CaptchaController {

    private final Log logger = LogFactory.getLog(this.getClass());

    @SendCode
    @PostMapping(value = "code/common", produces = "image/jpeg")
    @Operation(summary = "获取图形验证码", description = "用于基础验证及邮箱验证之前。")
    public void createCommonCode(@CodeValue @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code) {
        logger.debug("生成图形验证码：" + code);
    }

    @SendCode(value = "registration", sender = @Sender("emailCodeSender"), parameters = {
            @Parameter(name = "SUBJECT", value = "邮箱注册"),
            @Parameter(name = "TEMPLATE", value = "mail/EmailCode.html")
    })
    @VerifyCode
    @PostMapping("code/registration")
    @Operation(summary = "获取邮箱注册验证码", description = "发送验证码到邮箱，用于注册。")
    public void createRegistrationCode(@CodeValue @RequestParam(name = "code") String targetCode,
                                       @CodeValue("registration") @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code,
                                       @RequestParam @CodeParam(value = "registration", name = "email") String email) {
        logger.debug(String.format("消费图像验证码：%s\t发送邮箱注册验证码：%s\t邮箱：%s", targetCode, code, email));
    }

    @SendCode(value = "email", sender = @Sender("emailCodeSender"), parameters = {
            @Parameter(name = "SUBJECT", value = "更换注册邮箱"),
            @Parameter(name = "TEMPLATE", value = "mail/EmailCode.html")
    })
    @VerifyCode
    @PostMapping("code/email")
    @Operation(summary = "获取邮箱更改验证码", description = "发送验证码到邮箱，用于更改邮箱。")
    public void createEmailCode(@CodeValue @RequestParam(name = "code") String targetCode,
                                @CodeValue("email") @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code,
                                @RequestParam @CodeParam("email") String email) {
        logger.debug(String.format("消费图像验证码：%s\t发送更新邮箱验证码：%s\t邮箱：%s", targetCode, code, email));
    }

}
