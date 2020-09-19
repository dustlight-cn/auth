package cn.dustlight.oauth2.uim.controllers.users;

import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.oauth2.uim.entities.v1.roles.DefaultUserRole;
import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import cn.dustlight.oauth2.uim.services.users.UimUserDetailsService;
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
    protected UimUserDetailsService userDetailsService;

    @Override
    public UimUser getSession() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UimUser cache = (UimUser) authentication.getPrincipal();
        UimUser snapshot = userDetailsService.loadUser(cache.getUid());
        return snapshot;
    }

    @Override
    public void createSession(String login, String password) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
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
        SecurityContextHolder.clearContext();
    }

    @Override
    public UimUser getUser(Long uid) {
        boolean flag = SecurityContextHolder.getContext().getAuthentication().getAuthorities().
                contains(new SimpleGrantedAuthority("READ_USER_ANY"));
        UimUser user = null;
        if (flag) {
            user = userDetailsService.loadUser(uid);
        } else {
            Collection<UimUser> collection = userDetailsService.loadPublicUserByUid(Arrays.asList(uid));
            if (collection != null && collection.size() > 0)
                user = collection.iterator().next();
        }
        if (user == null)
            ErrorEnum.USER_NOT_FOUND.throwException();
        return user;
    }

    @Override
    public void createUser(String username, String password, String email) {
        DefaultUserRole defaultRole = new DefaultUserRole();
        defaultRole.setRoleName("User");
        userDetailsService.createUser(username, password, email, username, 0,
                Arrays.asList(defaultRole), null, null, null,
                true);
    }

    @Override
    public void deleteUser(Long uid) {
        userDetailsService.deleteUsers(Arrays.asList(uid));
    }

}
