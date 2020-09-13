package cn.dustlight.oauth2.uim.configurations;

import cn.dustlight.oauth2.uim.handlers.UimUserApprovalHandler;
import cn.dustlight.oauth2.uim.handlers.convert.AccessTokenConverter;
import cn.dustlight.oauth2.uim.services.OAuthClientDetailsService;
import cn.dustlight.oauth2.uim.services.OAuthUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    public OAuthUserDetailService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore redisTokenStore;

    @Autowired
    private OAuthClientDetailsService clientDetailsService;

    @Autowired
    private AccessTokenConverter accessTokenConverter;

    @Autowired
    private UimUserApprovalHandler uimUserApprovalHandler;

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .accessTokenConverter(accessTokenConverter)
                .tokenStore(redisTokenStore);
        uimUserApprovalHandler.setRequestFactory(endpoints.getOAuth2RequestFactory());
        endpoints.userApprovalHandler(uimUserApprovalHandler);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()");
    }
}