package cn.dustlight.oauth2.uim.handlers.expression;

import cn.dustlight.oauth2.uim.entities.v1.users.User;
import org.springframework.security.core.Authentication;

/**
 * 用户相关表达式方法
 */
public class UimUserExpressionMethods {

    private final Authentication authentication;

    public UimUserExpressionMethods(Authentication authentication) {
        this.authentication = authentication;
    }

    /**
     * 判断当前用户是否匹配
     *
     * @param uid 目标用户uid
     * @return
     */
    public boolean matchUid(Long uid) {
        User user = obtainUser();
        if (user == null)
            return false;
        return user.getUid().equals(uid);
    }

    /**
     * 判断当前用户是否匹配
     *
     * @param username 目标用户username
     * @return
     */
    public boolean matchUsername(String username) {
        User user = obtainUser();
        if (user == null)
            return false;
        return user.getUsername().equals(username);
    }

    private User obtainUser() {
        if (this.authentication == null || authentication.getPrincipal() == null ||
                !(this.authentication.getPrincipal() instanceof User))
            return null;
        return (User) this.authentication.getPrincipal();
    }
}
