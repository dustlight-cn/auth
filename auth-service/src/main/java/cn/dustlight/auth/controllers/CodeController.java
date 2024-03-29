package cn.dustlight.auth.controllers;

import cn.dustlight.auth.util.Constants;
import cn.dustlight.captcha.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "Code", description = "验证码相关业务。")
@RestController
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@CrossOrigin(origins = Constants.CrossOrigin.origin, allowCredentials = Constants.CrossOrigin.allowCredentials)
public class CodeController {

    private final Log logger = LogFactory.getLog(this.getClass());

    @SendCode(value = "Register", sender = @Sender("verifiedEmailSender"), parameters = {
            @Parameter(name = "SUBJECT", value = "邮箱注册"),
            @Parameter(name = "TEMPLATE", value = "mail/EmailCode.html"),
            @Parameter(name = "CHECK_EXISTS", value = "false") // 检查邮箱是否不存在
    })
    @VerifyCode(store = @Store("reCaptchaStore"), verifier = @Verifier("reCaptchaVerifier"))
    @PostMapping("code/registration")
    @Operation(summary = "获取注册邮箱验证码", description = "发送验证码到邮箱，用于注册。")
    @Deprecated
    public void createRegistrationCode(@RequestParam("g-recaptcha-response") String recaptchaResponse,
                                       @CodeValue("Register") @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code,
                                       @RequestParam @CodeParam(value = "Register", name = "email") String email) {
        logger.debug(String.format("发送邮箱注册验证码：%s\t邮箱：%s", code, email));
    }

    @SendCode(value = "Register", sender = @Sender("verifiedEmailSender"), parameters = {
            @Parameter(name = "SUBJECT", value = "邮箱注册"),
            @Parameter(name = "TEMPLATE", value = "mail/EmailCode.html"),
            @Parameter(name = "CHECK_EXISTS", value = "false") // 检查邮箱是否不存在
    })
    @VerifyCode(store = @Store("reCaptchaStore"), verifier = @Verifier("reCaptchaVerifier"))
    @PostMapping("code/registration/email")
    @Operation(summary = "获取注册邮箱验证码", description = "发送验证码到邮箱，用于注册。")
    public void createRegistrationEmailCode(@RequestParam("g-recaptcha-response") String recaptchaResponse,
                                            @CodeValue("Register") @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code,
                                            @RequestParam @CodeParam(value = "Register", name = "email") String email) {
        logger.debug(String.format("发送邮箱注册验证码：%s\t邮箱：%s", code, email));
    }

    @SendCode(value = "Register", sender = @Sender("verifiedSmsSender"), parameters = {
            @Parameter(name = "TEMPLATE_CODE", value = "signup"),
            @Parameter(name = "TEMPLATE_ID", value = "signup"),
            @Parameter(name = "CHECK_EXISTS", value = "false") // 检查号码是否不存在
    })
    @VerifyCode(store = @Store("reCaptchaStore"), verifier = @Verifier("reCaptchaVerifier"))
    @PostMapping("code/registration/phone")
    @Operation(summary = "获取注册手机验证码", description = "发送验证码到手机，用于注册。")
    public void createRegistrationPhoneCode(@RequestParam("g-recaptcha-response") String recaptchaResponse,
                                            @CodeValue("Register") @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code,
                                            @RequestParam @CodeParam(value = "Register", name = "phone") String phone) {
        logger.debug(String.format("发送邮箱注册验证码：%s\t手机：%s", code, phone));
    }

    @SendCode(value = "ChangeEmail",
            sender = @Sender("verifiedEmailSender"),
            store = @Store("userCodeStore"),
            parameters = {
                    @Parameter(name = "SUBJECT", value = "更换用户邮箱"),
                    @Parameter(name = "TEMPLATE", value = "mail/ChangeEmail.html"),
                    @Parameter(name = "CHECK_EXISTS", value = "false") // 检查邮箱是否不存在
            })
    @VerifyCode(store = @Store("reCaptchaStore"), verifier = @Verifier("reCaptchaVerifier"))
    @PostMapping("code/email")
    @Operation(summary = "获取更换邮箱验证码", description = "发送验证码到邮箱，用于更改邮箱。", security = @SecurityRequirement(name = "AccessToken"))
    public void createUpdateEmailCode(@RequestParam("g-recaptcha-response") String recaptchaResponse,
                                      @CodeValue("ChangeEmail") @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code,
                                      @RequestParam @CodeParam("ChangeEmail") String email,
                                      HttpServletRequest request) {
        logger.debug(String.format("发送更新邮箱验证码：%s\t邮箱：%s", code, email));
    }

    @SendCode(value = "ChangePhone",
            sender = @Sender("verifiedSmsSender"),
            store = @Store("userCodeStore"),
            parameters = {
                    @Parameter(name = "TEMPLATE_CODE", value = "changePhone"),
                    @Parameter(name = "TEMPLATE_ID", value = "changePhone"),
                    @Parameter(name = "CHECK_EXISTS", value = "false") // 检查号码是否不存在
            })
    @VerifyCode(store = @Store("reCaptchaStore"), verifier = @Verifier("reCaptchaVerifier"))
    @PostMapping("code/phone")
    @Operation(summary = "获取更换手机号码验证码", description = "发送验证码到手机，用于更改手机号码。", security = @SecurityRequirement(name = "AccessToken"))
    public void createUpdatePhoneCode(@RequestParam("g-recaptcha-response") String recaptchaResponse,
                                      @CodeValue("ChangePhone") @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code,
                                      @RequestParam @CodeParam("ChangePhone") String phone,
                                      HttpServletRequest request) {
        logger.debug(String.format("发送更新手机验证码：%s\t号码：%s", code, phone));
    }

    @SendCode(value = "ResetPassword",
            sender = @Sender("verifiedEmailSender"),
            parameters = {
                    @Parameter(name = "SUBJECT", value = "重置密码"),
                    @Parameter(name = "TEMPLATE", value = "mail/ResetPasswordByEmail.html")
            })
    @VerifyCode(store = @Store("reCaptchaStore"), verifier = @Verifier("reCaptchaVerifier"))
    @PostMapping("code/password/email")
    @Operation(summary = "获取重置密码邮箱验证码", description = "发送验证码到邮箱，用于更改密码。")
    public void createUpdatePasswordEmailCode(@RequestParam("g-recaptcha-response") String recaptchaResponse,
                                              @CodeValue("ResetPassword") @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code,
                                              @RequestParam @CodeParam("ResetPassword") String email) {
        logger.debug(String.format("发送密码重置邮箱验证码：%s\t邮箱：%s", code, email));
    }

    @SendCode(value = "ResetPassword",
            sender = @Sender("verifiedSmsSender"),
            parameters = {
                    @Parameter(name = "TEMPLATE_CODE", value = "resetPassword"),
                    @Parameter(name = "TEMPLATE_ID", value = "resetPassword"),
            })
    @VerifyCode(store = @Store("reCaptchaStore"), verifier = @Verifier("reCaptchaVerifier"))
    @PostMapping("code/password/phone")
    @Operation(summary = "获取重置密码手机验证码", description = "发送验证码到手机，用于更改密码。")
    public void createUpdatePasswordPhoneCode(@RequestParam("g-recaptcha-response") String recaptchaResponse,
                                              @CodeValue("ResetPassword") @io.swagger.v3.oas.annotations.Parameter(hidden = true) String code,
                                              @RequestParam @CodeParam("ResetPassword") String phone) {
        logger.debug(String.format("发送密码重置手机验证码：%s\t号码：%s", code, phone));
    }
}
