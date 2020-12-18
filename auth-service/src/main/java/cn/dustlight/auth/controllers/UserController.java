package cn.dustlight.auth.controllers;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.DefaultUserRole;
import cn.dustlight.auth.entities.User;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.util.Constants;
import cn.dustlight.auth.util.QueryResults;
import cn.dustlight.captcha.annotations.CodeParam;
import cn.dustlight.captcha.annotations.CodeValue;
import cn.dustlight.captcha.annotations.VerifyCode;
import cn.dustlight.storage.core.Permission;
import cn.dustlight.storage.core.RestfulStorage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RequestMapping(value = Constants.API_ROOT,
        produces = Constants.ContentType.APPLICATION_JSON)
@Tag(name = "用户及会话管理",
        description = "包括登入登出、注册注销、信息查询更新等。")
@RestController
public class UserController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected UserService userService;

    @Qualifier("authStorage")
    @Autowired
    protected RestfulStorage storage;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("session")
    @Operation(summary = "获取登录用户信息", description = "获取当前会话信息")
    public User getSession() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User cache = (User) authentication.getPrincipal();
        User snapshot = userService.loadUser(cache.getUid());
        logger.debug(String.format("用户: [%s] 访问会话信息", snapshot.getUsername()));
        return snapshot;
    }

    @PostMapping("session")
    @Operation(summary = "登入", description = "创建会话")
    public User createSession(@RequestParam String login, @RequestParam String password) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug(String.format("用户: [%s] 创建会话", authentication.getName()));
            return (User) authentication.getPrincipal();
        } catch (InternalAuthenticationServiceException e) {
            this.logger.error("An internal error occurred while trying to authenticate the user.", e);
            SecurityContextHolder.clearContext();
            ErrorEnum.SIGN_IN_FAIL.details(e.getMessage()).throwException();
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            ErrorEnum.SIGN_IN_FAIL.details(e.getMessage()).throwException();
        } catch (Exception e) {
            this.logger.error("An unknown error occurred while trying to authenticate the user.", e);
            SecurityContextHolder.clearContext();
            ErrorEnum.SIGN_IN_FAIL.details(e.getMessage()).throwException();
        }
        return null;
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("session")
    @Operation(summary = "登出", description = "销毁当前会话")
    public void deleteSession() {
        logger.debug(String.format("用户: [%s] 销毁会话", SecurityContextHolder.getContext().getAuthentication().getName()));
        SecurityContextHolder.clearContext();
    }

    @GetMapping("users/{uid}")
    @Operation(summary = "获取用户信息")
    public User getUser(@PathVariable Long uid) {
        boolean flag = SecurityContextHolder.getContext().getAuthentication().getAuthorities().
                contains(new SimpleGrantedAuthority("READ_USER_ANY"));
        User user = null;
        if (flag) {
            user = userService.loadUser(uid);
        } else {
            Collection<User> collection = userService.loadPublicUserByUid(Arrays.asList(uid));
            if (collection != null && collection.size() > 0)
                user = collection.iterator().next();
        }
        if (user == null)
            ErrorEnum.USER_NOT_FOUND.throwException();
        logger.debug(String.format("查询用户: [%s] ", user.getUsername()));
        return user;
    }

    @VerifyCode("registration")
    @PostMapping("users")
    @Operation(summary = "注册用户", description = "创建新用户，用户名和邮箱不可重复。")
    public User createUser(@RequestParam String username,
                           @RequestParam String password,
                           @CodeParam("registration") @Parameter(hidden = true) String email,
                           @CodeValue("registration") @RequestParam String code) {
        DefaultUserRole defaultRole = new DefaultUserRole();
        defaultRole.setRoleName("User");
        userService.createUser(username, password, email, username, 0,
                Arrays.asList(defaultRole), null, null, null,
                true);
        logger.debug(String.format("创建用户: [%s] 邮箱：[%s]", username, email));
        return userService.loadUserByUsername(username);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('DELETE_USER') or hasAuthority('DELETE_USER_ANY')")
    @DeleteMapping("users/{uid}")
    @io.swagger.v3.oas.annotations.Operation(summary = "注销用户", description = "销毁用户。注意，此方法将删除用户。")
    public void deleteUser(@PathVariable Long uid) {
        userService.deleteUsers(Arrays.asList(uid));
        logger.debug(String.format("删除用户: [%s] ", uid));
    }

    @GetMapping("users")
    @Operation(summary = "查找用户")
    public QueryResults<? extends User> getUsers(@RequestParam(required = false, value = "q") String query,
                                                 @RequestParam(required = false) Integer offset,
                                                 @RequestParam(required = false) Integer limit,
                                                 @RequestParam(required = false) Collection<String> order) {
        boolean flag = SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("READ_USER_ANY"));
        if (flag) {
            if (query != null && query.length() > 0)
                return userService.searchUsers(query, order, offset, limit);
            else
                return userService.listUsers(order, offset, limit);
        }
        return userService.searchPublicUsers(query, order, offset, limit);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
    @PutMapping("users/{uid}/password")
    @Operation(summary = "更新用户密码")
    public void updatePassword(@PathVariable Long uid, @RequestParam String password) {
        userService.updatePassword(uid, password);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
    @VerifyCode("email")
    @PutMapping("users/{uid}/email")
    @Operation(summary = "更新用户邮箱")
    public void updateEmail(@PathVariable Long uid, @CodeValue("email") @RequestParam String code, @CodeParam("email") @Parameter(hidden = true) String email) {
        userService.updateEmail(uid, email);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
    @PutMapping("users/{uid}/gender")
    @Operation(summary = "更新用户性别")
    public void updateGender(@PathVariable Long uid, @RequestParam int gender) {
        userService.updateGender(uid, gender);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
    @PutMapping(value = "users/{uid}/avatar", consumes = "image/*")
    @Operation(summary = "更新用户头像", requestBody = @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "image/*",
                    schema = @Schema(type = "string", format = "binary")
            )
    ))
    public void updateAvatar(@PathVariable Long uid) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();

            String key = generateAvatarKey(uid);
            String redirectUrl = storage.generatePutUrl(key, Permission.WRITABLE, 1000 * 60 * 60L); // 生成签名上传URL
            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
            response.setHeader("Location", redirectUrl);
        } catch (IOException e) {
            ErrorEnum.UPDATE_USER_FAIL.details(e.getMessage()).throwException();
        }
    }

    @GetMapping("users/{uid}/avatar")
    @Operation(summary = "获取用户头像")
    public void getAvatar(@PathVariable Long uid) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();

            String key = generateAvatarKey(uid);
            if (!storage.isExist(key))
                ErrorEnum.RESOURCE_NOT_FOUND.throwException();
            String redirectUrl = storage.generateGetUrl(key, 1000 * 60 * 60L); // 生成下载URL
            response.setStatus(HttpStatus.FOUND.value());
            response.setHeader("Location", redirectUrl);
        } catch (IOException e) {
            ErrorEnum.RESOURCE_NOT_FOUND.details(e.getMessage()).throwException();
        }
    }

    protected String generateAvatarKey(Object id) {
        return String.format("users/%s/avatar", id);
    }
}
