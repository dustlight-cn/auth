package cn.dustlight.auth.controllers;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.DefaultUserRole;
import cn.dustlight.auth.entities.User;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.util.Constants;
import cn.dustlight.captcha.annotations.CodeParam;
import cn.dustlight.captcha.annotations.CodeValue;
import cn.dustlight.captcha.annotations.VerifyCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@Tag(name = "User", description = "获取当前用户信息，用户注册。")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public class UserController {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    protected UserService userService;

    @PreAuthorize("#oauth2.hasAnyScope('read:user')")
    @GetMapping(value = "user")
    @Operation(summary = "获取当前用户信息",
            description = "获取当前用户信息。权限：应用需要 'read:user' 授权作用域。",
            security = @SecurityRequirement(name = "Access Token"))
    public User getUser(OAuth2Authentication oAuth2Authentication) {
        if (!(oAuth2Authentication.getPrincipal() instanceof User))
            ErrorEnum.UNAUTHORIZED.details("Principal is not User").throwException();
        User user = (User) oAuth2Authentication.getPrincipal();
        return user;
    }

    @VerifyCode("registration")
    @PostMapping("/user")
    @Operation(summary = "用户自助注册",
            description = "通过邮箱验证码自助注册账号。",
            security = @SecurityRequirement(name = "Access Token"))
    public User createUser(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("code") @CodeValue("registration") String code,
                           @Parameter(hidden = true) @CodeParam(value = "registration", name = "email") String email) {
        DefaultUserRole defaultRole = new DefaultUserRole();
        defaultRole.setRoleName("User");
        userService.createUser(username, password, email, username, 0,
                Arrays.asList(defaultRole), null, null, null,
                true);
        if (logger.isDebugEnabled())
            logger.debug(String.format("【用户注册】 用户名：%s，邮箱：%s。", username, email));
        return userService.loadUserByUsername(username);
    }
}
