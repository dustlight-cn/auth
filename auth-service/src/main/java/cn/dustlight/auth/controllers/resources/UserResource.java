package cn.dustlight.auth.controllers.resources;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.DefaultPublicUser;
import cn.dustlight.auth.entities.DefaultUser;
import cn.dustlight.auth.entities.DefaultUserRole;
import cn.dustlight.auth.entities.User;
import cn.dustlight.auth.services.StorageHandler;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.util.Constants;
import cn.dustlight.auth.util.QueryResults;
import cn.dustlight.captcha.annotations.CodeParam;
import cn.dustlight.captcha.annotations.CodeValue;
import cn.dustlight.captcha.annotations.VerifyCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

@RestController
@Tag(name = "Users", description = "用户增删改查、信息更新。")
@SecurityRequirement(name = "AccessToken")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@CrossOrigin(origins = Constants.CrossOrigin.origin, allowCredentials = Constants.CrossOrigin.allowCredentials)
public class UserResource {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    protected UserService<DefaultUser, DefaultPublicUser> userService;

    @Autowired
    protected StorageHandler storageHandler;

    @PreAuthorize("#oauth2.hasAnyScope('read:user')")
    @GetMapping("users/{uid}")
    @Operation(summary = "获取用户信息", description = "获取用户的信息。" +
            "权限：应用需要 'read:user' 授权作用域。（若用户拥有 'READ_USER' 权限，可获取用户完整信息。）")
    public User getUser(@PathVariable Long uid) {
        boolean flag = SecurityContextHolder.getContext().getAuthentication().getAuthorities().
                contains(new SimpleGrantedAuthority("READ_USER"));
        DefaultUser user = null;
        if (flag) {
            user = userService.loadUser(uid);
        } else {
            Collection<DefaultPublicUser> collection = userService.loadPublicUserByUid(Arrays.asList(uid));
            if (collection != null && collection.size() > 0)
                user = collection.iterator().next();
        }
        if (user == null)
            ErrorEnum.USER_NOT_FOUND.throwException();
        logger.debug(String.format("查询用户: [%s] ", user.getUsername()));
        return setAvatar(user);
    }

    @PreAuthorize("#oauth2.clientHasRole('CREATE_USER') and (#oauth2.client or hasAuthority('CREATE_USER'))")
    @PostMapping("users")
    @Operation(summary = "注册用户", description = "创建新用户，用户名和邮箱不可重复。" +
            "权限：" +
            "1、应用拥有 'CREATE_USER' 权限。" +
            "2、用户拥有 'CREATE_USER' 权限。")
    public User createUser(@RequestParam(name = "username") String username,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "email") String email) {
        DefaultUserRole defaultRole = new DefaultUserRole();
        defaultRole.setRoleName("User");
        userService.createUser(username, password, email, username, 0,
                Arrays.asList(defaultRole), null, null, null,
                true);
        logger.debug(String.format("创建用户: [%s] 邮箱：[%s]", username, email));
        return setAvatar(userService.loadUserByUsername(username));
    }

    @PreAuthorize("#oauth2.clientHasRole('DELETE_USER') and (#oauth2.client or hasAuthority('DELETE_USER') or #user.matchUid(#uid))")
    @DeleteMapping("users/{uid}")
    @io.swagger.v3.oas.annotations.Operation(summary = "注销用户", description = "删除用户。权限：" +
            "1、应用拥有 'DELETE_USER' 权限。" +
            "2、用户拥有 'DELETE_USER' 权限，或者 'uid' 为用户所属。")
    public void deleteUser(@PathVariable Long uid) {
        userService.deleteUsers(Arrays.asList(uid));
        logger.debug(String.format("删除用户: [%s] ", uid));
    }

    @PreAuthorize("#oauth2.hasAnyScope('read:user')")
    @GetMapping(value = "users")
    @Operation(summary = "查找用户", description = "查询或者列出用户（取决于有无关键字）。" +
            "权限：应用拥有 'read:user' 授权作用域。（若用户拥有 'READ_USER' 权限，可获取用户完整信息。）")
    public QueryResults<? extends User> getUsers(@RequestParam(required = false, value = "q") String query,
                                                 @RequestParam(required = false) Integer offset,
                                                 @RequestParam(defaultValue = "10") Integer limit,
                                                 @RequestParam(required = false) Collection<String> order) {
        boolean flag = SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("READ_USER"));
        if (flag) {
            if (query != null && query.length() > 0)
                return setAvatar(userService.searchUsers(query, order, offset, limit));
            else
                return setAvatar(userService.listUsers(order, offset, limit));
        }
        return setAvatar(userService.searchPublicUsers(query, order, offset, limit));
    }

    @PreAuthorize("#oauth2.hasAnyScope('write:user') and (#oauth2.client or hasAuthority('WRITE_USER'))")
    @PutMapping("users/{uid}/password")
    @Operation(summary = "更新用户密码", description = "更新用户的密码。" +
            "权限：应用拥有 'write:user' 授权作用域。")
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
            String key = generateAvatarKey(uid);
            storageHandler.handle(key, attributes.getRequest(), attributes.getResponse());
        } catch (IOException e) {
            ErrorEnum.UPDATE_USER_FAIL.details(e.getMessage()).throwException();
        }
    }

    @GetMapping("users/{uid}/avatar")
    @Operation(summary = "获取用户头像")
    public void getAvatar(@PathVariable Long uid) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String key = generateAvatarKey(uid);
            storageHandler.handle(key, attributes.getRequest(), attributes.getResponse(), "image/*");
        } catch (IOException e) {
            ErrorEnum.RESOURCE_NOT_FOUND.details(e.getMessage()).throwException();
        }
    }

    protected String generateAvatarKey(Object id) {
        return String.format(Constants.AVATAR_FORMAT, id);
    }

    public static <T extends DefaultUser> T setAvatar(T user) {
        if (user == null)
            return null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String url = URI.create(attributes.getRequest().getRequestURL().toString())
                .resolve(Constants.API_ROOT + "/users/" + user.getUid() + "/avatar")
                .toASCIIString();
        user.setAvatar(url);
        return user;
    }

    public static <T extends DefaultUser, C extends Collection<T>> C setAvatar(C users) {
        if (users == null)
            return null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        URI uri = URI.create(attributes.getRequest().getRequestURL().toString());
        for (T user : users) {
            if (user == null)
                continue;
            String url = uri.resolve(Constants.API_ROOT + "/users/" + user.getUid() + "/avatar").toASCIIString();
            user.setAvatar(url);
        }
        return users;
    }

    public static <T extends DefaultUser> QueryResults<T> setAvatar(QueryResults<T> results) {
        if (results == null || results.getData() == null)
            return results;
        results.setData(setAvatar(results.getData()));
        return results;
    }
}
