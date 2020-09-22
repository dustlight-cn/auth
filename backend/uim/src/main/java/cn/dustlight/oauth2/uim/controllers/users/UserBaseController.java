package cn.dustlight.oauth2.uim.controllers.users;

import cn.dustlight.oauth2.uim.Constants;
import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import cn.dustlight.validator.annotations.CodeParam;
import cn.dustlight.validator.annotations.CodeValue;
import cn.dustlight.validator.annotations.VerifyCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.V1.API_ROOT,
        produces = Constants.ContentType.APPLICATION_JSON)
@Tag(name = "用户基础业务",
        description = "负责用户的登录注册。")
public interface UserBaseController {

    /**
     * 获取当前会话信息
     *
     * @return
     */
    @Operation(summary = "获取登录用户信息", description = "获取当前会话信息")
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
    @Operation(summary = "获取用户信息")
    @GetMapping("user/{uid}")
    UimUser getUser(@PathVariable Long uid);

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
}
