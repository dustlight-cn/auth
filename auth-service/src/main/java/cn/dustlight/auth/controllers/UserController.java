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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@Tag(name = "User", description = "获取当前用户信息，用户注册。")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public class UserController {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @VerifyCode("Register")
    @PostMapping("user")
    @Operation(summary = "用户自助注册",
            description = "通过邮箱验证码自助注册账号。",
            security = @SecurityRequirement(name = "Access Token"))
    public User createUser(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("code") @CodeValue("Register") String code,
                           @Parameter(hidden = true) @CodeParam(value = "Register", name = "email") String email) {
        DefaultUserRole defaultRole = new DefaultUserRole();
        defaultRole.setRoleName("User");
        userService.createUser(username, password, email, username, 0,
                Arrays.asList(defaultRole), null, null, null,
                true);
        if (logger.isDebugEnabled())
            logger.debug(String.format("【用户注册】 用户名：%s，邮箱：%s。", username, email));
        return userService.loadUserByUsername(username);
    }

    @PutMapping("user/password")
    @Operation(summary = "更改用户密码",
            description = "通过原密码更改密码。",
            security = @SecurityRequirement(name = "Access Token"))
    public void updatePassword(OAuth2Authentication oAuth2Authentication,
                               @RequestParam("oldPassword") String oldPassword,
                               @RequestParam("newPassword") String newPassword) {
        User user = (User) oAuth2Authentication.getPrincipal();
        if (!passwordEncoder.matches(user.getPassword(), oldPassword))
            ErrorEnum.PASSWORD_INVALID.throwException();
        userService.updatePassword(user.getUid(), newPassword);
        if (logger.isDebugEnabled())
            logger.debug(String.format("【用户更改密码】 用户名：%s。", user.getUsername()));
    }

    @VerifyCode("ResetPasswordByEmail")
    @PutMapping("password")
    @Operation(summary = "邮箱重置密码",
            description = "通过邮箱验证码重置密码。",
            security = @SecurityRequirement(name = "Access Token"))
    public void updatePasswordWithEmail(@RequestParam("password") String password,
                                        @RequestParam("code") @CodeValue("ResetPasswordByEmail") String code,
                                        @CodeParam(value = "ResetPasswordByEmail", name = "email") String email) {
        userService.updatePasswordByEmail(email, password);
        if (logger.isDebugEnabled())
            logger.debug(String.format("【用户通过邮箱重置密码】 邮箱：%s。", email));
    }

    @VerifyCode("ChangeEmail")
    @PutMapping("user/email")
    @Operation(summary = "更改邮箱",
            description = "更改用户邮箱，需要输入密码。",
            security = @SecurityRequirement(name = "Access Token"))
    public void updateEmail(OAuth2Authentication oAuth2Authentication,
                            @RequestParam("password") String password,
                            @CodeValue("ChangeEmail") @RequestParam("code") String code,
                            @CodeParam(value = "ChangeEmail", name = "email") @Parameter(hidden = true) String email) {
        User user = (User) oAuth2Authentication.getPrincipal();
        if (!passwordEncoder.matches(user.getPassword(), password))
            ErrorEnum.PASSWORD_INVALID.throwException();
        userService.updateEmail(user.getUid(), email);
        if (logger.isDebugEnabled())
            logger.debug(String.format("【用户更改邮箱】 用户名：%s，邮箱：%s。", user.getUsername(), email));
    }
}
