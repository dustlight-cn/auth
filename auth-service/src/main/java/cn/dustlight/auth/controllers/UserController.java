package cn.dustlight.auth.controllers;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.controllers.resources.UserResource;
import cn.dustlight.auth.entities.DefaultPublicUser;
import cn.dustlight.auth.entities.DefaultUser;
import cn.dustlight.auth.entities.DefaultUserRole;
import cn.dustlight.auth.entities.User;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.util.Constants;
import cn.dustlight.captcha.annotations.CodeParam;
import cn.dustlight.captcha.annotations.CodeValue;
import cn.dustlight.captcha.annotations.Store;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@Tag(name = "User", description = "获取当前用户信息，用户注册。")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@CrossOrigin(origins = Constants.CrossOrigin.origin, allowCredentials = Constants.CrossOrigin.allowCredentials)
public class UserController {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    protected UserService<DefaultUser, DefaultPublicUser> userService;

    @PreAuthorize("#oauth2.hasAnyScope('read:user')")
    @GetMapping(value = "user")
    @Operation(summary = "获取当前 Token 的用户信息",
            description = "应用需要拥有 read:user 授权。",
            security = @SecurityRequirement(name = "AccessToken"))
    public User getTokenUser(OAuth2Authentication oAuth2Authentication) {
        if (!(oAuth2Authentication.getPrincipal() instanceof User))
            ErrorEnum.UNAUTHORIZED.details("Principal is not User").throwException();
        DefaultUser user = (DefaultUser) oAuth2Authentication.getPrincipal();
        return UserResource.setAvatar(userService.loadUser(user.getUid(), oAuth2Authentication.getOAuth2Request().getClientId()));
    }

    @VerifyCode("Register")
    @PostMapping("user")
    @Operation(summary = "用户注册（通过邮箱验证码或者手机号）")
    public User register(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("code") @CodeValue("Register") String code,
                         @Parameter(hidden = true) @CodeParam(value = "Register", name = "email") String email,
                         @Parameter(hidden = true) @CodeParam(value = "Register", name = "phone") String phone) {
        DefaultUserRole defaultRole = new DefaultUserRole();
        defaultRole.setRoleName("User");
        userService.createUser(username, password, phone, email, username, 0,
                Arrays.asList(defaultRole), null, null, null,
                true);
        if (logger.isDebugEnabled())
            logger.debug(String.format("【用户注册】 用户名：%s，邮箱：%s。", username, email));
        return UserResource.setAvatar(userService.loadUserByUsername(username));
    }

    @PreAuthorize("#oauth2.clientHasRole('WRITE_USER_PASSWORD')")
    @PutMapping("user/password")
    @Operation(summary = "通过原密码更改用户密码",
            description = "应用需要 WRITE_USER_PASSWORD 权限。",
            security = @SecurityRequirement(name = "AccessToken"))
    public void resetPassword(OAuth2Authentication oAuth2Authentication,
                              @RequestParam("oldPassword") String oldPassword,
                              @RequestParam("newPassword") String newPassword) {
        if (oldPassword.equals(newPassword))
            ErrorEnum.UPDATE_PASSWORD_FAIL_ORIGINAL_PASSWORD
                    .details("The new password must not be the same as the original password.")
                    .throwException();
        User user = (User) oAuth2Authentication.getPrincipal();
        user = userService.loadUser(user.getUid());
        if (!StringUtils.hasText(user.getEmail()) && !StringUtils.hasText(user.getPhone()))
            ErrorEnum.UPDATE_PASSWORD_FAIL_EMAIL_OR_PHONE_NOT_EXIST
                    .details("Can't change password without binding email address or phone.")
                    .throwException();
        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            ErrorEnum.PASSWORD_INVALID.throwException();
        userService.updatePassword(user.getUid(), newPassword);
        if (logger.isDebugEnabled())
            logger.debug(String.format("【用户更改密码】 用户名：%s。", user.getUsername()));
    }

    @VerifyCode("ResetPassword")
    @PutMapping("password")
    @Operation(summary = "邮箱或手机重置密码")
    public void resetPassword(@RequestParam("password") String password,
                              @RequestParam("code") @CodeValue("ResetPassword") String code,
                              @Parameter(hidden = true) @CodeParam(value = "ResetPassword", name = "email") String email,
                              @Parameter(hidden = true) @CodeParam(value = "ResetPassword", name = "phone") String phone) {
        if (StringUtils.hasText(email)) {
            userService.updatePasswordByEmail(email, password);
            if (logger.isDebugEnabled())
                logger.debug(String.format("【用户通过邮箱重置密码】 邮箱：%s。", email));
        } else if (StringUtils.hasText(phone)) {
            userService.updatePasswordByPhone(phone, password);
            if (logger.isDebugEnabled())
                logger.debug(String.format("【用户通过手机重置密码】 号码：%s。", phone));
        }
    }

    @PreAuthorize("#oauth2.clientHasRole('WRITE_USER_EMAIL')")
    @VerifyCode(value = "ChangeEmail", store = @Store("userCodeStore"))
    @PutMapping("user/email")
    @Operation(summary = "通过密码更改邮箱",
            description = "应用需要 WRITE_USER_EMAIL 权限。",
            security = @SecurityRequirement(name = "AccessToken"))
    public void resetEmail(OAuth2Authentication oAuth2Authentication,
                           @RequestParam("password") String password,
                           @CodeValue("ChangeEmail") @RequestParam("code") String code,
                           @CodeParam(value = "ChangeEmail", name = "email") @Parameter(hidden = true) String email) {
        User user = (User) oAuth2Authentication.getPrincipal();
        user = userService.loadUser(user.getUid());
        if (!passwordEncoder.matches(password, user.getPassword()))
            ErrorEnum.PASSWORD_INVALID.throwException();
        userService.updateEmail(user.getUid(), email);
        if (logger.isDebugEnabled())
            logger.debug(String.format("【用户更改邮箱】 用户名：%s，邮箱：%s。", user.getUsername(), email));
    }

    @PreAuthorize("#oauth2.clientHasRole('WRITE_USER_PHONE')")
    @VerifyCode(value = "ChangePhone", store = @Store("userCodeStore"))
    @PutMapping("user/phone")
    @Operation(summary = "通过密码更改手机号码",
            description = "应用需要 WRITE_USER_PHONE 权限。",
            security = @SecurityRequirement(name = "AccessToken"))
    public void resetPhone(OAuth2Authentication oAuth2Authentication,
                           @RequestParam("password") String password,
                           @CodeValue("ChangePhone") @RequestParam("code") String code,
                           @CodeParam(value = "ChangePhone", name = "phone") @Parameter(hidden = true) String phone) {
        User user = (User) oAuth2Authentication.getPrincipal();
        user = userService.loadUser(user.getUid());
        if (!passwordEncoder.matches(password, user.getPassword()))
            ErrorEnum.PASSWORD_INVALID.throwException();
        userService.updatePhone(user.getUid(), phone);
        if (logger.isDebugEnabled())
            logger.debug(String.format("【用户更改手机号码】 用户名：%s，号码：%s。", user.getUsername(), phone));
    }

    @GetMapping("username/{username}")
    @Operation(summary = "检查用户名是否存在")
    public boolean isUsernameExists(@PathVariable(name = "username") String username) {
        return userService.isUsernameExists(username);
    }

    @GetMapping("email/{email}")
    @Operation(summary = "检查邮箱是否存在")
    public boolean isEmailExists(@PathVariable(name = "email") String email) {
        return userService.isEmailExists(email);
    }

    @GetMapping("phone/{phone}")
    @Operation(summary = "检查手机号码是否存在")
    public boolean isPhoneExists(@PathVariable(name = "phone") String phone) {
        return userService.isPhoneExists(phone);
    }
}
