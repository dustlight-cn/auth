package cn.dustlight.auth.controllers;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.User;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Session", description = "登录、注销等。")
@RestController
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public class SessionController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected UserService userService;

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
}
