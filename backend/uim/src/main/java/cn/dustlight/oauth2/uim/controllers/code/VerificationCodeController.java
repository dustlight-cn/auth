package cn.dustlight.oauth2.uim.controllers.code;

import cn.dustlight.oauth2.uim.Constants;
import cn.dustlight.validator.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.V1.API_ROOT)
@Tag(name = "验证码业务",
        description = "负责验证码相关业务。")
public interface VerificationCodeController {

    @Operation(summary = "获取图形验证码", description = "用于基础验证及邮箱验证之前。")
    @PostMapping("code/common")
    @SendCode
    void createCommonCode(@io.swagger.v3.oas.annotations.Parameter(hidden = true)
                          @CodeValue String code);

    @Operation(summary = "获取邮箱注册验证码", description = "发送验证码到邮箱，用于注册。")
    @PostMapping("code/registration")
    @VerifyCode
    @SendCode(value = "registration", generator = @Generator("numberCodeGenerator"), sender = @Sender("emailCodeSender"), parameters = {
            @Parameter(name = "template", value = "register")
    })
    void createRegistrationCode(@RequestParam(name = "code") @CodeValue String targetCode,
                                @io.swagger.v3.oas.annotations.Parameter(hidden = true) @CodeValue("registration") String code,
                                @CodeParam("registration") @RequestParam String email);

    @Operation(summary = "获取邮箱更改验证码", description = "发送验证码到邮箱，用于更改邮箱。")
    @PostMapping("code/email")
    @VerifyCode
    @SendCode(value = "email", generator = @Generator("numberCodeGenerator"), sender = @Sender("emailCodeSender"), parameters = {
            @Parameter(name = "template", value = "register")
    })
    void createEmailCode(@RequestParam(name = "code") @CodeValue String targetCode,
                         @io.swagger.v3.oas.annotations.Parameter(hidden = true) @CodeValue("email") String code,
                         @CodeParam("email") @RequestParam String email);

}
