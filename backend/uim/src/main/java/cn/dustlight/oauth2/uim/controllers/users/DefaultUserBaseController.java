package cn.dustlight.oauth2.uim.controllers.users;

import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.oauth2.uim.entities.v1.roles.DefaultUserRole;
import cn.dustlight.oauth2.uim.entities.v1.users.PublicUimUser;
import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import cn.dustlight.oauth2.uim.services.users.UimUserDetailsService;
import cn.dustlight.storage.core.UrlStorage;
import cn.dustlight.storage.core.Permission;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RestController
public class DefaultUserBaseController implements UserBaseController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private UimUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UrlStorage storage;

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
    public PublicUimUser getUser(Long uid) {
        Collection<PublicUimUser> collection = userDetailsService.loadPublicUserByUid(Arrays.asList(uid));
        if (collection == null || collection.size() == 0)
            ErrorEnum.USER_NOT_FOUND.throwException();
        return collection.iterator().next();
    }

    @Override
    public void createUser(String username, String password, String email) {
        DefaultUserRole defaultRole = new DefaultUserRole();
        defaultRole.setRoleName("User");
        userDetailsService.createUser(username,
                password,
                email,
                username,
                0,
                Arrays.asList(defaultRole),
                null,
                null,
                null,
                true);
    }

    @Override
    public void deleteUser(Long uid) {
        userDetailsService.deleteUsers(Arrays.asList(uid));
    }

    @Override
    public void updatePassword(Long uid, String password) {
        userDetailsService.updatePassword(uid, password);
    }

    @Override
    public void updateEmail(Long uid, String email) {
        userDetailsService.updateEmail(uid, email);
    }

    @Override
    public void updateGender(Long uid, int gender) {
        userDetailsService.updateGender(uid, gender);
    }

    @Override
    public void updateAvatar(Long uid, MultipartFile file) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();
            String redirectUrl = storage.generatePutUrl("uim-test/upload/" + uid + "/avatar", Permission.WRITABLE, 1000 * 60 * 60L);
            response.setStatus(307);
            response.setHeader("Location", redirectUrl);
        } catch (IOException e) {
            ErrorEnum.UPDATE_RESOURCE_FAIL.details(e.getMessage()).throwException();
        }
    }

    @Override
    public void getAvatar(Long uid, Integer size, Long t) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();
            String redirectUrl = storage.generateGetUrl("uim-test/upload/" + uid + "/avatar", 1000 * 60 * 60L);
            response.setStatus(302);
            response.setHeader("Location", redirectUrl);
        } catch (IOException e) {
            ErrorEnum.RESOURCE_NOT_FOUND.details(e.getMessage()).throwException();
        }
    }
}
