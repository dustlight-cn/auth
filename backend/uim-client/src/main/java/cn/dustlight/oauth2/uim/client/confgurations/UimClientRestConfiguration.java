package cn.dustlight.oauth2.uim.client.confgurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class UimClientRestConfiguration {

    @Autowired
    private UimClientProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationRequestResolver oAuth2AuthorizationRequestResolver(@Autowired ClientRegistrationRepository registrationRepository) {
        return new DefaultOAuth2AuthorizationRequestResolver(registrationRepository, properties.getAuthorizationRequestBaseUri());
    }

    @Bean
    @ConditionalOnMissingBean
    public UimClientSecurityConfiguration.RestOAuth2AuthorizationRequestFilter restOAuth2AuthorizationRequestFilter(@Autowired OAuth2AuthorizationRequestResolver requestResolver,
                                                                                                                    @Autowired AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository,
                                                                                                                    @Autowired ObjectMapper objectMapper) {
        return new UimClientSecurityConfiguration.RestOAuth2AuthorizationRequestFilter(requestResolver, authorizationRequestRepository, objectMapper);
    }
}
