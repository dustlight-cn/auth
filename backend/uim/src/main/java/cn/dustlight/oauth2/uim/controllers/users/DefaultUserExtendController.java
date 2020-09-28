package cn.dustlight.oauth2.uim.controllers.users;

import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.oauth2.uim.entities.results.QueryResults;
import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import cn.dustlight.oauth2.uim.services.users.UimUserDetailsService;
import cn.dustlight.storage.core.Permission;
import cn.dustlight.storage.core.RestfulStorage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@RestController
public class DefaultUserExtendController implements UserExtendController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    protected UimUserDetailsService userDetailsService;

    @Autowired
    protected RestfulStorage storage;

    @Override
    public QueryResults<? extends UimUser, Integer> getUsers(String query, Integer offset, Integer limit, Collection<String> order) {
        boolean flag = SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("READ_USER_ANY"));
        if (flag) {
            if (query != null && query.length() > 0)
                return userDetailsService.searchUsers(query, order, offset, limit);
            else
                return userDetailsService.listUsers(order, offset, limit);
        }
        return userDetailsService.searchPublicUsers(query, order, offset, limit);
    }

    @Override
    public void updatePassword(Long uid, String password) {
        userDetailsService.updatePassword(uid, password);
    }

    @Override
    public void updateEmail(Long uid, String code, String email) {
        userDetailsService.updateEmail(uid, email);
    }

    @Override
    public void updateGender(Long uid, int gender) {
        userDetailsService.updateGender(uid, gender);
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
