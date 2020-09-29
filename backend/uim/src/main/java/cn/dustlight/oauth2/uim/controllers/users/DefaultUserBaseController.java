package cn.dustlight.oauth2.uim.controllers.users;

import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.oauth2.uim.entities.v1.roles.DefaultUserRole;
import cn.dustlight.oauth2.uim.entities.v1.users.User;
import cn.dustlight.oauth2.uim.services.users.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;

@RestController
public class DefaultUserBaseController implements UserBaseController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected UserService userDetailsService;

    @Override
    public User getSession() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User cache = (User) authentication.getPrincipal();
        User snapshot = userDetailsService.loadUser(cache.getUid());
        logger.debug(String.format("用户: [%s] 访问会话信息", snapshot.getUsername()));
        return snapshot;
    }

    @Override
    public void createSession(String login, String password) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug(String.format("用户: [%s] 创建会话", authentication.getName()));
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
    }

    @Override
    public void deleteSession() {
        logger.debug(String.format("用户: [%s] 销毁会话", SecurityContextHolder.getContext().getAuthentication().getName()));
        SecurityContextHolder.clearContext();
    }

    @Override
    public User getUser(Long uid) {
        boolean flag = SecurityContextHolder.getContext().getAuthentication().getAuthorities().
                contains(new SimpleGrantedAuthority("READ_USER_ANY"));
        User user = null;
        if (flag) {
            user = userDetailsService.loadUser(uid);
        } else {
            Collection<User> collection = userDetailsService.loadPublicUserByUid(Arrays.asList(uid));
            if (collection != null && collection.size() > 0)
                user = collection.iterator().next();
        }
        if (user == null)
            ErrorEnum.USER_NOT_FOUND.throwException();
        logger.debug(String.format("查询用户: [%s] ", user.getUsername()));
        return user;
    }

    @Override
    public void createUser(String username, String password, String email, String code) {
        DefaultUserRole defaultRole = new DefaultUserRole();
        defaultRole.setRoleName("User");
        userDetailsService.createUser(username, password, email, username, 0,
                Arrays.asList(defaultRole), null, null, null,
                true);
        logger.debug(String.format("创建用户: [%s] 邮箱：[%s]", username, email));
    }

    @Override
    public void deleteUser(Long uid) {
        userDetailsService.deleteUsers(Arrays.asList(uid));
        logger.debug(String.format("删除用户: [%s] ", uid));
    }

}
