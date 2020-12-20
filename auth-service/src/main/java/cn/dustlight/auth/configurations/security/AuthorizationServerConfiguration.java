package cn.dustlight.auth.configurations.security;

import cn.dustlight.auth.configurations.components.StorageConfiguration;
import cn.dustlight.auth.configurations.documents.DocumentConfiguration;
import cn.dustlight.auth.configurations.components.ServicesConfiguration;
import cn.dustlight.auth.configurations.components.TokenConfiguration;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.services.UserService;
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
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

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
    private TokenStore authTokenStore;

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
                .tokenStore(authTokenStore)
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
        return configurer.getTokenGranter();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationServerTokenServices authorizationServerTokenServices(@Autowired AuthorizationServerEndpointsConfigurer configurer) {
        return configurer.getTokenServices();
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
