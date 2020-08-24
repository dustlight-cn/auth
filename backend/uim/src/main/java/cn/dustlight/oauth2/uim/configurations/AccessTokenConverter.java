package cn.dustlight.oauth2.uim.configurations;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, Object> result = (Map<String, Object>) super.convertAccessToken(token, authentication);
        if (authentication.isClientOnly())
            result.remove("authorities");
        result.put("client_authorities", AuthorityUtils.authorityListToSet(authentication.getOAuth2Request().getAuthorities()));
        return result;
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication oAuth2Authentication = super.extractAuthentication(map);
        if (oAuth2Authentication == null || oAuth2Authentication.getOAuth2Request() == null)
            return oAuth2Authentication;
        OAuth2Request oldRequest = oAuth2Authentication.getOAuth2Request();
        Collection<GrantedAuthority> authorities = new LinkedHashSet<>();
        if (map.containsKey("client_authorities")) {
            String[] roles = (String[]) ((Collection) map.get("client_authorities")).toArray(new String[0]);
            Collection<? extends GrantedAuthority> append = AuthorityUtils.createAuthorityList(roles);
            authorities.addAll(append);
        }
        OAuth2Request request = new OAuth2Request(oldRequest.getRequestParameters(),
                oldRequest.getClientId(),
                authorities,
                true,
                oldRequest.getScope(),
                oldRequest.getResourceIds(),
                oldRequest.getRedirectUri(),
                oldRequest.getResponseTypes(),
                oldRequest.getExtensions());
        return new OAuth2Authentication(request, oAuth2Authentication.getUserAuthentication());
    }
}
