package cn.dustlight.auth.configurations.security;

import cn.dustlight.auth.configurations.components.StorageConfiguration;
import cn.dustlight.auth.configurations.documents.DocumentConfiguration;
import cn.dustlight.auth.configurations.components.ServicesConfiguration;
import cn.dustlight.auth.configurations.components.TokenConfiguration;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.services.oauth.AuthTokenService;
import cn.dustlight.auth.services.oauth.granters.AuthImplicitTokenGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.ArrayList;
import java.util.List;

@EnableAuthorizationServer
@Import({TokenConfiguration.class,
        ServicesConfiguration.class,
        AuthMethodSecurityConfiguration.class,
        SecurityConfiguration.class,
        DocumentConfiguration.class,
        ResourceServerConfiguration.class,
        HandlerConfiguration.class,
        StorageConfiguration.class})
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserService<?, ?> userService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private AccessTokenConverter accessTokenConverter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.checkTokenAccess("isFullyAuthenticated()")
                .tokenKeyAccess("isFullyAuthenticated()")
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.userDetailsService(userService)
                .tokenServices(authTokenService)
                .accessTokenConverter(accessTokenConverter)
                .authorizationCodeServices(authorizationCodeServices)
                .authenticationManager(authenticationManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer(@Autowired AuthorizationServerEndpointsConfiguration configuration) {
        return configuration.getEndpointsConfigurer();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenGranter tokenGranter(@Autowired AuthorizationServerEndpointsConfigurer configurer) {

        ClientDetailsService clientDetails = configurer.getClientDetailsService();
        AuthorizationServerTokenServices tokenServices = configurer.getTokenServices();
        AuthorizationCodeServices authorizationCodeServices = configurer.getAuthorizationCodeServices();
        OAuth2RequestFactory requestFactory = configurer.getOAuth2RequestFactory();

        List<TokenGranter> tokenGranters = new ArrayList<>();
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails,
                requestFactory));
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
        ImplicitTokenGranter implicit = new AuthImplicitTokenGranter(tokenServices, clientDetails, requestFactory);
        tokenGranters.add(implicit);
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory));
        if (authenticationManager != null) {
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
                    clientDetails, requestFactory));
        }
        return new CompositeTokenGranter(tokenGranters);
    }

    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices(@Autowired AuthorizationServerEndpointsConfigurer configurer) {
        return configurer.getTokenServices();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceServerTokenServices resourceServerTokenServices(@Autowired AuthorizationServerEndpointsConfigurer configurer) {
        return configurer.getResourceServerTokenServices();
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2RequestFactory oAuth2RequestFactory(@Autowired AuthorizationServerEndpointsConfigurer configurer) {
        return configurer.getOAuth2RequestFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public RedirectResolver redirectResolver(@Autowired AuthorizationServerEndpointsConfigurer configurer) {
        return configurer.getRedirectResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2RequestValidator oAuth2RequestValidator(@Autowired AuthorizationServerEndpointsConfigurer configurer) {
        return configurer.getOAuth2RequestValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserApprovalHandler userApprovalHandler(@Autowired AuthorizationServerEndpointsConfigurer configurer) {
        return configurer.getUserApprovalHandler();
    }
}
