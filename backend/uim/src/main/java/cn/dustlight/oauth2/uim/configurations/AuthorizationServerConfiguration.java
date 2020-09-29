package cn.dustlight.oauth2.uim.configurations;

import cn.dustlight.oauth2.uim.handlers.UimUserApprovalHandler;
import cn.dustlight.oauth2.uim.handlers.convert.UimAccessTokenConverter;
import cn.dustlight.oauth2.uim.services.clients.ClientService;
import cn.dustlight.oauth2.uim.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    public UserService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore uimTokenStore;

    @Autowired
    private ClientService clientDetailsService;

    private UimAccessTokenConverter uimAccessTokenConverter = new UimAccessTokenConverter();

    @Autowired
    private UimUserApprovalHandler uimUserApprovalHandler;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .accessTokenConverter(uimAccessTokenConverter)
                .tokenStore(uimTokenStore)
                .authorizationCodeServices(authorizationCodeServices)
        ;
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