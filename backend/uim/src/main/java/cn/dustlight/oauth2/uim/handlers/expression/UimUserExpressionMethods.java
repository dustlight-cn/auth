package cn.dustlight.oauth2.uim.handlers.expression;

import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

public class UimUserExpressionMethods {
    private final Authentication authentication;


    public UimUserExpressionMethods(Authentication authentication) {
        this.authentication = authentication;
    }

    public boolean matchUid(Long uid) {
        return obtainUser().getUid().equals(uid);
    }

    public boolean matchUsername(String username) {
        return obtainUser().getUsername().equals(username);
    }

    private UimUser obtainUser() {
        if (this.authentication == null || authentication.getPrincipal() == null ||
                !(this.authentication.getPrincipal() instanceof UimUser))
            throw new AccessDeniedException("Access Denied");
        return (UimUser) this.authentication.getPrincipal();
    }
}
