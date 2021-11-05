package cn.dustlight.auth.services.oauth.granters;

import cn.dustlight.auth.entities.User;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.Assert;

public class AuthImplicitTokenGranter extends ImplicitTokenGranter {


    public AuthImplicitTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest clientToken) {

        Authentication userAuth = SecurityContextHolder.getContext().getAuthentication();
        if (userAuth == null || !userAuth.isAuthenticated() || !(userAuth instanceof OAuth2Authentication)) {
            throw new InsufficientAuthenticationException("There is no currently logged in user");
        }

        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) userAuth;

        UsernamePasswordAuthenticationToken usrAuth = new UsernamePasswordAuthenticationToken(userAuth.getPrincipal(), userAuth.getCredentials(), ((User) userAuth.getPrincipal()).getAuthorities());

        OAuth2Authentication newAuthentication = new OAuth2Authentication(oAuth2Authentication.getOAuth2Request(), usrAuth);

        Assert.state(clientToken instanceof ImplicitTokenRequest, "An ImplicitTokenRequest is required here. Caller needs to wrap the TokenRequest.");
        OAuth2Request requestForStorage = ((ImplicitTokenRequest) clientToken).getOAuth2Request();

        return new OAuth2Authentication(requestForStorage, newAuthentication);
    }
}
