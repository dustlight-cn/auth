package cn.dustlight.auth.util;

import cn.dustlight.auth.entities.User;
import org.springframework.security.core.Authentication;

public class UserExpressionMethods {

    private final Authentication authentication;

    public UserExpressionMethods(Authentication authentication) {
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
