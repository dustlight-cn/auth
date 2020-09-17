package cn.dustlight.oauth2.uim.controllers.users;

import cn.dustlight.oauth2.uim.Constants;
import cn.dustlight.oauth2.uim.entities.v1.users.PublicUimUser;
import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(value = Constants.V1.API_ROOT,
        produces = Constants.ContentType.APPLICATION_JSON)
@Tag(name = "用户基础业务",
        description = "主要包含登录注册等服务。")
public interface UserBaseController {

    /**
     * 获取当前会话信息
     *
     * @return
     */
    @Operation(summary = "获取登录用户", description = "获取当前会话信息")
    @GetMapping("session")
    @PreAuthorize("isAuthenticated()")
    UimUser getSession();

    /**
     * 创建会话（登入）
     *
     * @param login    登入账号
     * @param password 密码
     */
    @Operation(summary = "登入", description = "创建会话")
    @PostMapping("session")
    void createSession(@RequestParam String login, @RequestParam String password);

    /**
     * 销毁会话（登出）
     */
    @Operation(summary = "登出", description = "销毁当前会话")
    @DeleteMapping("session")
    @PreAuthorize("isAuthenticated()")
    void deleteSession();

    /**
     * 获取用户
     *
     * @param uid 用户id
     * @return
     */
    @Operation(summary = "获取用户公开信息", description = "获取用户的公开信息")
    @GetMapping("user/{uid}")
    PublicUimUser getUser(@PathVariable Long uid);

    /**
     * 创建用户
     *
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     */
    @Operation(summary = "注册", description = "创建用户")
    @PostMapping("user")
    void createUser(@RequestParam String username, @RequestParam String password, @RequestParam String email);

    /**
     * 删除用户
     *
     * @param uid 用户id
     */
    @Operation(summary = "注销", description = "销毁用户")
    @DeleteMapping("user/{uid}")
    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('DELETE_USER') or hasAuthority('DELETE_USER')")
    void deleteUser(@PathVariable Long uid);

    /**
     * 更新用户密码
     *
     * @param uid      用户id
     * @param password 新密码
     */
    @Operation(summary = "更新用户密码")
    @PutMapping("user/{uid}/password")
    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
    void updatePassword(@PathVariable Long uid, @RequestParam String password);

    /**
     * 更新用户邮箱
     *
     * @param uid   用户id
     * @param email 新邮箱
     */
    @Operation(summary = "更新用户邮箱")
    @PutMapping("user/{uid}/email")
    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
    void updateEmail(@PathVariable Long uid, @RequestParam String email);

    /**
     * 更新用户性别
     *
     * @param uid    用户id
     * @param gender 性别
     */
    @Operation(summary = "更新用户性别")
    @PutMapping("user/{uid}/gender")

    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
    void updateGender(@PathVariable Long uid, @RequestParam int gender);

    /**
     * 更新用户头像
     *
     * @param uid 用户id
     */
    @Operation(summary = "更新用户头像")
    @PutMapping(value = "user/{uid}/avatar", consumes = "image/*")
    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
    void updateAvatar(@PathVariable Long uid, @RequestBody MultipartFile file);

    /**
     * 获取用户头像
     *
     * @param uid  用户id
     * @param size 图片尺寸
     * @param t    时间戳
     */
    @Operation(summary = "获取用户头像")
    @GetMapping("user/{uid}/avatar")
    void getAvatar(@PathVariable Long uid, @RequestParam(required = false) Integer size, @RequestParam(required = false) Long t);
}
