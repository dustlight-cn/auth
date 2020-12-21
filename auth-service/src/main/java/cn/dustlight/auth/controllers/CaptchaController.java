package cn.dustlight.auth.controllers;

import cn.dustlight.auth.util.Constants;
import cn.dustlight.captcha.annotations.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "CAPTCHA", description = "验证码相关业务。")
@RestController
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public class CaptchaController {

    private final Log logger = LogFactory.getLog(this.getClass());

    @SendCode
    @GetMapping(value = "code/image", produces = "image/jpeg")
    @Operation(summary = "获取图形验证码", description = "用于基础验证及邮箱验证之前。")
    public void createCommonCode(@CodeValue @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code) {
        logger.debug("生成图形验证码：" + code);
    }

    @SendCode(value = "Register", sender = @Sender("emailCodeSender"), parameters = {
            @Parameter(name = "SUBJECT", value = "邮箱注册"),
            @Parameter(name = "TEMPLATE", value = "mail/EmailCode.html")
    })
    @VerifyCode
    @PostMapping("code/registration")
    @Operation(summary = "获取注册邮箱验证码", description = "发送验证码到邮箱，用于注册。")
    public void createRegistrationCode(@CodeValue @RequestParam(name = "code") String targetCode,
                                       @CodeValue("Register") @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code,
                                       @RequestParam @CodeParam(value = "Register", name = "email") String email) {
        logger.debug(String.format("消费图像验证码：%s\t发送邮箱注册验证码：%s\t邮箱：%s", targetCode, code, email));
    }

    @SendCode(value = "ChangeEmail", sender = @Sender("emailCodeSender"), parameters = {
            @Parameter(name = "SUBJECT", value = "更换用户邮箱"),
            @Parameter(name = "TEMPLATE", value = "mail/ChangeEmail.html")
    })
    @VerifyCode
    @PostMapping("code/email")
    @Operation(summary = "获取更换邮箱验证码", description = "发送验证码到邮箱，用于更改邮箱。")
    public void createUpdateEmailCode(@CodeValue @RequestParam(name = "code") String targetCode,
                                      @CodeValue("ChangeEmail") @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code,
                                      @RequestParam @CodeParam("ChangeEmail") String email) {
        logger.debug(String.format("消费图像验证码：%s\t发送更新邮箱验证码：%s\t邮箱：%s", targetCode, code, email));
    }

    @SendCode(value = "ResetPasswordByEmail", sender = @Sender("emailCodeSender"), parameters = {
            @Parameter(name = "SUBJECT", value = "重置密码"),
            @Parameter(name = "TEMPLATE", value = "mail/ResetPasswordByEmail.html")
    })
    @VerifyCode
    @PostMapping("code/password/email")
    @Operation(summary = "获取重置密码邮箱验证码", description = "发送验证码到邮箱，用于更改邮箱。")
    public void createUpdatePasswordEmailCode(@CodeValue @RequestParam(name = "code") String targetCode,
                                              @CodeValue("ResetPasswordByEmail") @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code,
                                              @RequestParam @CodeParam("ResetPasswordByEmail") String email) {
        logger.debug(String.format("消费图像验证码：%s\t发送密码重置邮箱验证码：%s\t邮箱：%s", targetCode, code, email));
    }
}
