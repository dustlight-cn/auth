package cn.dustlight.uim.configurations;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, Object> result = (Map<String, Object>) super.convertAccessToken(token, authentication);
        Object authorities = result.get("authorities");
        Set<String> scopeAuthorities = AuthorityUtils.authorityListToSet(authentication.getOAuth2Request().getAuthorities());
        if (authorities == null)
            result.put("authorities", scopeAuthorities);
        else {
            ((Set<String>) authorities).addAll(scopeAuthorities);
        }
        return result;
    }

}
