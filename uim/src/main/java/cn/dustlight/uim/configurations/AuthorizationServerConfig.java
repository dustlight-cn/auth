package cn.dustlight.uim.configurations;

import cn.dustlight.uim.services.ClientMapper;
import cn.dustlight.uim.services.OAuthClientDetailsService;
import cn.dustlight.uim.services.OAuthUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public OAuthUserDetailService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore redisTokenStore;

    @Autowired
    private OAuthClientDetailsService clientDetailsService;

    @Autowired
    private ApprovalStore approvalStore;

    private UserApproveHandler userApproveHandler = new UserApproveHandler();

    @Autowired
    private ClientMapper clientMapper;

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(redisTokenStore);

        userApproveHandler.setApprovalStore(approvalStore);
        userApproveHandler.setRequestFactory(endpoints.getOAuth2RequestFactory());
        userApproveHandler.setClientDetailsService(clientDetailsService);
        userApproveHandler.setMapper(clientMapper);

        endpoints.userApprovalHandler(userApproveHandler);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }
}