package cn.dustlight.oauth2.uim.controllers.users;

import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.oauth2.uim.entities.results.QueryResults;
import cn.dustlight.oauth2.uim.entities.v1.roles.DefaultUserRole;
import cn.dustlight.oauth2.uim.entities.v1.users.User;
import cn.dustlight.oauth2.uim.services.users.UserService;
import cn.dustlight.storage.core.Permission;
import cn.dustlight.storage.core.RestfulStorage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RestController
public class DefaultUserController implements UserController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected UserService userService;

    @Autowired
    protected RestfulStorage storage;

    @Override
    public User getSession() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User cache = (User) authentication.getPrincipal();
        User snapshot = userService.loadUser(cache.getUid());
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

    @Override
    public void createUser(String username, String password, String email, String code) {
        DefaultUserRole defaultRole = new DefaultUserRole();
        defaultRole.setRoleName("User");
        userService.createUser(username, password, email, username, 0,
                Arrays.asList(defaultRole), null, null, null,
                true);
        logger.debug(String.format("创建用户: [%s] 邮箱：[%s]", username, email));
    }

    @Override
    public void deleteUser(Long uid) {
        userService.deleteUsers(Arrays.asList(uid));
        logger.debug(String.format("删除用户: [%s] ", uid));
    }

    @Override
    public QueryResults<? extends User, Integer> getUsers(String query, Integer offset, Integer limit, Collection<String> order) {
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

    @Override
    public void updatePassword(Long uid, String password) {
        userService.updatePassword(uid, password);
    }

    @Override
    public void updateEmail(Long uid, String code, String email) {
        userService.updateEmail(uid, email);
    }

    @Override
    public void updateGender(Long uid, int gender) {
        userService.updateGender(uid, gender);
    }

    @Override
    public void updateAvatar(Long uid) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();

            String key = generateAvatarKey(uid);
            String redirectUrl = storage.generatePutUrl(key, Permission.WRITABLE, 1000 * 60 * 60L); // 生成签名上传URL
            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
            response.setHeader("Location", redirectUrl);
            logger.info("Redirect avatar");
        } catch (IOException e) {
            ErrorEnum.UPDATE_RESOURCE_FAIL.details(e.getMessage()).throwException();
        }
    }

    @Override
    public void getAvatar(Long uid, Integer size, Long t) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();

            String key = generateAvatarKey(uid);
            if (!storage.isExist(key))
                ErrorEnum.RESOURCE_NOT_FOUND.throwException();
            String redirectUrl = storage.generateGetUrl(key, 1000 * 60 * 60L); // 生成签名下载URL
            response.setStatus(HttpStatus.FOUND.value());
            response.setHeader("Location", redirectUrl);
        } catch (IOException e) {
            ErrorEnum.RESOURCE_NOT_FOUND.details(e.getMessage()).throwException();
        }
    }

    protected String generateAvatarKey(Object id) {
        return String.format("uim-test/upload/%s/avatar", id);
    }
}
