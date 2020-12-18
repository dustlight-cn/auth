package cn.dustlight.auth.controllers;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.DefaultUserRole;
import cn.dustlight.auth.entities.User;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.util.Constants;
import cn.dustlight.auth.util.QueryResults;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

@RestController
@RequestMapping(value = Constants.API_ROOT,
        produces = Constants.ContentType.APPLICATION_JSON)
@Tag(name = "用户及会话管理",
        description = "包括登入登出、注册注销、信息查询更新等。")
public class DefaultUserController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    protected UserService userService;

//    @Autowired
//    protected RestfulStorage storage;

    /**
     * 获取用户
     *
     * @param uid 用户id
     * @return
     */
    @Operation(summary = "获取用户信息", security = @SecurityRequirement(name = "OAuth2"))
    @GetMapping("users/{uid}")
    @PreAuthorize("#oauth2.clientHasAnyRole('READ_USER','READ_USER_ANY')")
    public User getUser(Long uid) {
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
        return user;
    }

    /**
     * 创建用户
     *
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     */
    @Operation(summary = "注册用户", description = "创建新用户，用户名和邮箱不可重复。", security = @SecurityRequirement(name = "OAuth2"))
    @PostMapping("users")
    @PreAuthorize("#oauth2.clientHasAnyRole('CREATE_USER')")
    public User createUser(String username, String password, String email) {
        DefaultUserRole defaultRole = new DefaultUserRole();
        defaultRole.setRoleName("User");
        userService.createUser(username, password, email, username, 0,
                Arrays.asList(defaultRole), null, null, null,
                true);
        return userService.loadUserByUsername(username);
    }

    /**
     * 删除用户
     *
     * @param uid 用户id
     */
    @Operation(summary = "注销用户", description = "销毁用户。注意，此方法将删除用户。", security = @SecurityRequirement(name = "OAuth2"))
    @DeleteMapping("users/{uid}")
    @PreAuthorize("#oauth2.clientHasAnyRole('DELETE_USER','DELETE_USER_ANY')")
    public void deleteUser(Long uid) {
        userService.deleteUsers(Arrays.asList(uid));
        logger.debug(String.format("删除用户: [%s] ", uid));
    }

    /**
     * 查找用户
     *
     * @param query
     * @param offset
     * @param limit
     * @param order
     * @return
     */
    @Operation(summary = "查找用户", security = @SecurityRequirement(name = "OAuth2"))
    @GetMapping("users")
    @PreAuthorize("#oauth2.clientHasAnyRole('READ_USER','READ_USER_ANY')")
    public QueryResults<? extends User> getUsers(String query, Integer offset, Integer limit, LinkedHashSet<String> order) {
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

    /**
     * 更新用户密码
     *
     * @param uid      用户id
     * @param password 新密码
     */
    @Operation(summary = "更新用户密码", security = @SecurityRequirement(name = "OAuth2"))
    @PutMapping("users/{uid}/password")
    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
    public void updatePassword(Long uid, String password) {
        userService.updatePassword(uid, password);
    }

    /**
     * 更新用户邮箱
     *
     * @param uid   用户id
     * @param code  验证码
     * @param email 新邮箱（自动注入）
     */
    @Operation(summary = "更新用户邮箱", security = @SecurityRequirement(name = "OAuth2"))
    @PutMapping("users/{uid}/email")
    public void updateEmail(Long uid, String code, String email) {
        userService.updateEmail(uid, email);
    }

    /**
     * 更新用户性别
     *
     * @param uid    用户id
     * @param gender 性别
     */
    @Operation(summary = "更新用户性别", security = @SecurityRequirement(name = "OAuth2"))
    @PutMapping("users/{uid}/gender")
    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
    public void updateGender(Long uid, int gender) {
        userService.updateGender(uid, gender);
    }

//    /**
//     * 更新用户头像
//     *
//     * @param uid 用户id
//     */
//    @Operation(summary = "更新用户头像",
//            requestBody = @RequestBody(
//                    required = true,
//                    content = @Content(
//                            mediaType = "image/*",
//                            schema = @Schema(type = "string", format = "binary")
//                    )
//            ))
//    @PutMapping(value = "users/{uid}/avatar", consumes = "image/*")
//    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
//    public void updateAvatar(Long uid) {
//        try {
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            HttpServletResponse response = attributes.getResponse();
//            String key = generateAvatarKey(uid);
//            String redirectUrl = storage.generatePutUrl(key, Permission.WRITABLE, 1000 * 60 * 60L); // 生成签名上传URL
//            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
//            response.setHeader("Location", redirectUrl);
//        } catch (IOException e) {
//            ErrorEnum.UPDATE_USER_FAIL.details(e.getMessage()).throwException();
//        }
//    }
//
//    /**
//     * 获取用户头像
//     *
//     * @param uid 用户id
//     */
//    @Operation(summary = "获取用户头像")
//    @GetMapping("users/{uid}/avatar")
//    public void getAvatar(Long uid) {
//        try {
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            HttpServletResponse response = attributes.getResponse();
//            String key = generateAvatarKey(uid);
//            if (!storage.isExist(key))
//                ErrorEnum.RESOURCE_NOT_FOUND.throwException();
//            String redirectUrl = storage.generateGetUrl(key, 1000 * 60 * 60L); // 生成下载URL
//            response.setStatus(HttpStatus.FOUND.value());
//            response.setHeader("Location", redirectUrl);
//        } catch (IOException e) {
//            ErrorEnum.RESOURCE_NOT_FOUND.details(e.getMessage()).throwException();
//        }
//    }

    protected String generateAvatarKey(Object id) {
        return String.format("users/%s/avatar", id);
    }
}
