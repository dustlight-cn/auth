package cn.dustlight.auth.configurations;

import cn.dustlight.auth.services.AuthApprovalHandler;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.util.AuthAccessTokenConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@EnableAuthorizationServer
@Import({TokenStoreConfiguration.class,
        GeneratorConfiguration.class,
        ServicesConfiguration.class,
        AuthMethodSecurityConfiguration.class,
        PasswordConfiguration.class})
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserService<?, ?> userService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthApprovalHandler authApprovalHandler;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private TokenStore authTokenStore;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.userDetailsService(userService)
                .accessTokenConverter(AuthAccessTokenConverter.instance)
                .tokenStore(authTokenStore)
                .authorizationCodeServices(authorizationCodeServices)
                .userApprovalHandler(authApprovalHandler);
    }
}
