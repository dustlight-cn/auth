package cn.dustlight.oauth2.uim.controllers.users;

import cn.dustlight.oauth2.uim.Constants;
import cn.dustlight.oauth2.uim.entities.results.QueryResults;
import cn.dustlight.oauth2.uim.entities.v1.users.User;
import cn.dustlight.validator.annotations.CodeParam;
import cn.dustlight.validator.annotations.CodeValue;
import cn.dustlight.validator.annotations.VerifyCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = Constants.V1.API_ROOT,
        produces = Constants.ContentType.APPLICATION_JSON)
@Tag(name = "用户及会话管理",
        description = "包括登入登出、注册注销、信息查询更新等。")
public interface UserController {

    /**
     * 获取当前会话信息
     *
     * @return
     */
    @Operation(summary = "获取登录用户信息", description = "获取当前会话信息")
    @GetMapping("session")
    @PreAuthorize("isAuthenticated()")
    User getSession();

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
    @Operation(summary = "获取用户信息")
    @GetMapping("user/{uid}")
    User getUser(@PathVariable Long uid);

    /**
     * 创建用户
     *
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱（自动注入）
     * @param code     验证码
     */
    @Operation(summary = "注册用户", description = "创建新用户，用户名和邮箱不可重复。")
    @PostMapping("user")
    @VerifyCode("registration")
    void createUser(@RequestParam String username, @RequestParam String password,
                    @Parameter(hidden = true) @CodeParam("registration") String email,
                    @RequestParam @CodeValue("registration") String code);

    /**
     * 删除用户
     *
     * @param uid 用户id
     */
    @Operation(summary = "注销用户", description = "销毁用户。注意，此方法将删除用户。")
    @DeleteMapping("user/{uid}")
    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('DELETE_USER') or hasAuthority('DELETE_USER')")
    void deleteUser(@PathVariable Long uid);


    /**
     * 查找用户
     *
     * @param query
     * @param offset
     * @param limit
     * @param order
     * @return
     */
    @Operation(summary = "查找用户")
    @GetMapping("users")
    QueryResults<? extends User, ? extends Number> getUsers(@RequestParam(required = false, value = "q") String query,
                                                            @RequestParam(required = false) Integer offset,
                                                            @RequestParam(required = false) Integer limit,
                                                            @RequestParam(required = false) Collection<String> order);

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
     * @param code  验证码
     * @param email 新邮箱（自动注入）
     */
    @Operation(summary = "更新用户邮箱")
    @PutMapping("user/{uid}/email")
    @VerifyCode("email")
    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
    void updateEmail(@PathVariable Long uid,
                     @RequestParam @CodeValue("email") String code,
                     @Parameter(hidden = true) @CodeParam("email") String email);

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
    @Operation(summary = "更新用户头像",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "image/*",
                            schema = @Schema(type = "string", format = "binary")
                    )
            ))
    @PutMapping(value = "user/{uid}/avatar", consumes = "image/*")
    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('WRITE_USER') or hasAuthority('WRITE_USER_ANY')")
    void updateAvatar(@PathVariable Long uid);

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
