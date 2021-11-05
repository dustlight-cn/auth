package cn.dustlight.auth.util;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.User;
import cn.dustlight.auth.services.ClientService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.StringUtils;

public class UserExpressionMethods {

    private final OAuth2Authentication authentication;
    private ClientService clientService;

    public UserExpressionMethods(OAuth2Authentication authentication,
                                 ClientService clientService) {
        this.authentication = authentication;
        this.clientService = clientService;
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

    public boolean isClientOwner(String clientId, Long uid) {
        return clientService.isOwner(clientId, uid);
    }

    public boolean isClientOwner(String clientId) {
        User user = obtainUser();
        if (user == null)
            return false;
        return clientService.isOwner(clientId, user.getUid());
    }

    public boolean isClientOwnerOrMember(String clientId, Long uid) {
        if (!StringUtils.hasText(clientId))
            clientId = authentication.getOAuth2Request().getClientId();
        return clientService.isOwnerOrMember(clientId, uid);
    }

    public boolean isClientOwnerOrMember(String clientId) {
        if (!StringUtils.hasText(clientId))
            clientId = authentication.getOAuth2Request().getClientId();
        User user = obtainUser();
        if (user == null)
            return false;
        return clientService.isOwnerOrMember(clientId, user.getUid());
    }

    public boolean isOrganization() {
        User user = obtainUser();
        if (user == null)
            return false;
        return user.isOrganization();
    }

    private User obtainUser() {
        if (this.authentication == null || authentication.getPrincipal() == null ||
                !(this.authentication.getPrincipal() instanceof User))
            return null;
        return (User) this.authentication.getPrincipal();
    }

    public static User obtainUser(OAuth2Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null ||
                !(authentication.getPrincipal() instanceof User))
            throw ErrorEnum.UNAUTHORIZED.details("OAuth2Authentication is null or principal not user").getException();
        return (User) authentication.getPrincipal();
    }
}
