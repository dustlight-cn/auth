package cn.dustlight.auth.controllers.resources;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.DefaultPublicUser;
import cn.dustlight.auth.entities.DefaultUser;
import cn.dustlight.auth.entities.DefaultUserRole;
import cn.dustlight.auth.entities.User;
import cn.dustlight.auth.services.oauth.EnhancedRedisTokenStore;
import cn.dustlight.auth.services.storages.StorageHandler;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.util.Constants;
import cn.dustlight.auth.util.QueryResults;
import cn.dustlight.captcha.annotations.CodeParam;
import cn.dustlight.captcha.annotations.CodeValue;
import cn.dustlight.captcha.annotations.Store;
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
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

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

    @Autowired
    protected EnhancedRedisTokenStore enhancedRedisTokenStore;

    /**
     * 判断 Client 与用户是否拥有某权限。
     */
    public static boolean hasAuthority(OAuth2Authentication auth2Authentication,
                                       String authority) {
        SimpleGrantedAuthority a = new SimpleGrantedAuthority(authority);
        return auth2Authentication.getOAuth2Request().getAuthorities().contains(a) &&
                (!auth2Authentication.isClientOnly() && auth2Authentication.getUserAuthentication().getAuthorities().contains(a));

    }

    @GetMapping("users/{uid}")
    @Operation(summary = "获取用户信息", description = "获取用户的公开信息。如果应用与用户拥有 READ_USER 权限，则获取完整信息。")
    public User getUser(@PathVariable Long uid, OAuth2Authentication auth2Authentication) {
        boolean flag = hasAuthority(auth2Authentication, "READ_USER");
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

    @PreAuthorize("(#oauth2.client or hasAuthority('CREATE_USER')) and #oauth2.clientHasRole('CREATE_USER')")
    @PostMapping("users")
    @Operation(summary = "创建用户（用户名和邮箱不可重复）", description = "应用和用户需要 CREATE_USER 权限。")
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

    @PreAuthorize("(#oauth2.client or hasAuthority('DELETE_USER')) and #oauth2.clientHasRole('DELETE_USER')")
    @DeleteMapping("users/{uid}")
    @io.swagger.v3.oas.annotations.Operation(summary = "删除用户（永久删除）", description = "应用和用户需要 DELETE_USER 权限。")
    public void deleteUser(@PathVariable Long uid) {
        DefaultUser user = userService.loadUser(uid);
        if (user == null)
            ErrorEnum.USER_NOT_FOUND.throwException();
        userService.deleteUsers(Arrays.asList(uid));
        try {
            storageHandler.remove(generateAvatarKey(uid));
        } catch (Exception e) {
            ErrorEnum.DELETE_USER_AVATAR_FAIL.details(e.getMessage());
        }
        try {
            enhancedRedisTokenStore.deleteUserToken(user.getUsername());
        } catch (IOException e) {
            ErrorEnum.DELETE_USER_TOKEN_FAIL.throwException();
        }
        logger.debug(String.format("删除用户: [%s] ", uid));
    }

    @GetMapping(value = "users")
    @Operation(summary = "查找用户",
            description = "查询或者列出用户（取决于有无关键字(q)或者用户ID(uid)），获取公开信息。若应用和用户拥有 READ_USER 权限，则获取完整信息。")
    public QueryResults<? extends User> getUsers(@RequestParam(required = false, value = "uid") Collection<Long> uid,
                                                 @RequestParam(required = false, value = "q") String query,
                                                 @RequestParam(required = false) Integer offset,
                                                 @RequestParam(defaultValue = "10") Integer limit,
                                                 @RequestParam(required = false) Collection<String> order,
                                                 OAuth2Authentication auth2Authentication) {
        boolean flag = hasAuthority(auth2Authentication, "READ_USER");
        if (uid != null && uid.size() > 0) {
            if (flag) {
                QueryResults<DefaultUser> result = new QueryResults<>();
                Collection<DefaultUser> users = userService.loadUsers(uid);
                result.setData(users);
                result.setCount(users.size());
                return setAvatar(result);
            }
            QueryResults<DefaultPublicUser> result = new QueryResults<>();
            Collection<DefaultPublicUser> users = userService.loadPublicUserByUid(uid);
            result.setData(users);
            result.setCount(users.size());
            return setAvatar(result);
        } else {
            if (flag) {
                if (query != null && query.length() > 0)
                    return setAvatar(userService.searchUsers(query, order, offset, limit));
                else
                    return setAvatar(userService.listUsers(order, offset, limit));
            }
            return setAvatar(userService.searchPublicUsers(query, order, offset, limit));
        }
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_USER_PASSWORD')) and #oauth2.clientHasRole('WRITE_USER_PASSWORD')")
    @PutMapping("users/{uid}/password")
    @Operation(summary = "更新用户密码", description = "应用和用户需拥有 WRITE_USER_PASSWORD 权限。")
    public void updateUserPassword(@PathVariable Long uid, @RequestParam String password) {
        userService.updatePassword(uid, password);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('LOCK_USER')) and #oauth2.clientHasRole('LOCK_USER')")
    @PutMapping("users/{uid}/unlock-at")
    @Operation(summary = "设置用户解锁日期", description = "设置用户账号的解锁日期，在此日期日前账号不能使用。设置为 NULL 则不锁定。应用和用户需拥有 LOCK_USER 权限。")
    public void updateUserUnlockAt(@PathVariable Long uid, @RequestParam Date unlockAt) {
        userService.updateUnlockedAt(Arrays.asList(uid), unlockAt);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('LOCK_USER')) and #oauth2.clientHasRole('LOCK_USER')")
    @PutMapping("users/{uid}/expired-at")
    @Operation(summary = "设置用户解锁日期", description = "设置用户账号过期日期。设置为 NULL 则无过期时间。应用和用户需拥有 LOCK_USER 权限。")
    public void updateUserExpiredAt(@PathVariable Long uid, @RequestParam Date expiredAt) {
        userService.updateAccountExpiredAt(Arrays.asList(uid), expiredAt);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('LOCK_USER')) and #oauth2.clientHasRole('LOCK_USER')")
    @PutMapping("users/{uid}/ban")
    @Operation(summary = "设置用户封禁或解封", description = "封禁或解封用户。应用和用户需拥有 LOCK_USER 权限。")
    public void updateUserEnabled(@PathVariable Long uid, @RequestParam Boolean enabled) {
        userService.updateEnabled(Arrays.asList(uid), enabled);
        if(!enabled){
            DefaultUser user = userService.loadUser(uid);
            try {
                enhancedRedisTokenStore.deleteUserToken(user.getUsername());
            } catch (IOException e) {
                ErrorEnum.DELETE_USER_TOKEN_FAIL.throwException();
            }
        }
    }

    @PreAuthorize("(#oauth2.client or hasAnyAuthority('WRITE_USER_EMAIL')) and #oauth2.clientHasRole('WRITE_USER_EMAIL')")
    @VerifyCode(value = "ChangeEmail", store = @Store("userCodeStore"))
    @PutMapping("users/{uid}/email")
    @Operation(summary = "更新用户邮箱", description = "应用和用户需拥有 WRITE_USER_EMAIL 权限。")
    public void updateUserEmail(@PathVariable Long uid,
                                @CodeValue("ChangeEmail") @RequestParam String code,
                                @CodeParam(value = "ChangeEmail", name = "email") @Parameter(hidden = true) String email) {
        userService.updateEmail(uid, email);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAnyAuthority('WRITE_USER')) and #oauth2.clientHasRole('WRITE_USER')")
    @PutMapping("users/{uid}/gender")
    @Operation(summary = "更新用户性别", description = "应用和用户（修改自身信息除外）需要拥有 WRITE_USER 权限。")
    public void updateUserGender(@PathVariable Long uid, @RequestParam int gender) {
        userService.updateGender(uid, gender);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAnyAuthority('WRITE_USER')) and #oauth2.clientHasRole('WRITE_USER')")
    @PutMapping("users/{uid}/nickname")
    @Operation(summary = "更新用户昵称", description = "应用和用户（修改自身信息除外）需要拥有 WRITE_USER 权限。")
    public void updateUserNickname(@PathVariable Long uid, @RequestParam String nickname) {
        userService.updateNickname(uid, nickname);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_USER')) and #oauth2.clientHasRole('WRITE_USER')")
    @PutMapping(value = "users/{uid}/avatar", consumes = "image/*")
    @Operation(summary = "更新用户头像",
            description = "应用和用户（修改自身信息除外）需要拥有 WRITE_USER 权限。",
            requestBody = @RequestBody(required = true,
                    content = @Content(
                            mediaType = "image/*",
                            schema = @Schema(type = "string", format = "binary")
                    )
            ))
    public void updateUserAvatar(@PathVariable Long uid) {
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
    public void getUserAvatar(@PathVariable Long uid) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String key = generateAvatarKey(uid);
            storageHandler.handle(key, attributes.getRequest(), attributes.getResponse(), "image/*");
        } catch (IOException e) {
            ErrorEnum.RESOURCE_NOT_FOUND.details(e.getMessage()).throwException();
        }
    }

    /* -------------------------------------------------------------------------------------------------- */

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
