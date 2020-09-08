package cn.dustlight.oauth2.uim.client.confgurations;

import cn.dustlight.oauth2.uim.client.UimClientProperties;
import cn.dustlight.oauth2.uim.client.converter.IUimUserConverter;
import cn.dustlight.oauth2.uim.client.entity.IUimUser;
import cn.dustlight.oauth2.uim.client.services.OAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.*;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

@EnableConfigurationProperties(UimClientProperties.class)
public class OAuth2UserServiceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OAuth2UserService oAuth2UserService(@Autowired UimClientProperties properties) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Map<String, Class<? extends IUimUserConverter>> classMap = properties.getCustomConverter();
        Map<String, IUimUserConverter> instanceMap = new LinkedHashMap<>();
        if (classMap != null) {
            for (String key : classMap.keySet()) {
                Class<? extends IUimUserConverter> clazz = classMap.get(key);
                if (clazz == null)
                    continue;
                instanceMap.put(key, clazz.getDeclaredConstructor().newInstance());
            }
        }
        return new OAuth2UserService(instanceMap, properties.getUserDataPath());
    }

    @Bean
    @Scope(
            value = "request",
            proxyMode = ScopedProxyMode.INTERFACES)
    @ConditionalOnMissingBean
    public OAuth2RestOperations oAuth2RestTemplate(@Qualifier("uimOAuth2ClientContext") OAuth2ClientContext clientContext,
                                                   @Autowired OAuth2AuthorizedClient oAuth2AuthorizedClient) {

        ClientRegistration registration = oAuth2AuthorizedClient.getClientRegistration();
        ClientRegistration.ProviderDetails providerDetails = registration.getProviderDetails();

        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setAccessTokenUri(providerDetails.getTokenUri());
        details.setClientId(registration.getClientId());
        details.setClientSecret(registration.getClientSecret());
        details.setGrantType(registration.getAuthorizationGrantType().getValue());
        details.setId(registration.getRegistrationId());
        details.setUserAuthorizationUri(providerDetails.getAuthorizationUri());
        if (registration.getScopes() != null)
            details.setScope(new LinkedList<>(registration.getScopes()));
        details.setUseCurrentUri(false);
        Logger.getGlobal().info("redirect_uri: " + registration.getRedirectUriTemplate() +
                ", token_uri=" + providerDetails.getTokenUri());
        Logger.getGlobal().info("Token: " + oAuth2AuthorizedClient.getAccessToken().getTokenValue() +
                ", RefreshToken=" + (oAuth2AuthorizedClient.getRefreshToken() == null? null : oAuth2AuthorizedClient.getRefreshToken().getTokenValue()));

        return new OAuth2RestTemplate(details, clientContext);
    }

    @Bean
    @Scope(
            value = "request",
            proxyMode = ScopedProxyMode.TARGET_CLASS
    )
    public OAuth2AuthorizedClient oAuth2AuthorizedClient(@Autowired OAuth2AuthorizedClientService service) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        IUimUser user = (IUimUser) authentication.getPrincipal();
        OAuth2AuthorizedClient auth2AuthorizedClient =
                service.loadAuthorizedClient(user.getOAuth2UserRequest().
                                getClientRegistration().
                                getRegistrationId(),
                        user.getName());
        return auth2AuthorizedClient;
    }

    @Bean
    @Scope(
            value = "request",
            proxyMode = ScopedProxyMode.INTERFACES
    )
    public OAuth2ClientContext uimOAuth2ClientContext(@Autowired OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        DefaultOAuth2ClientContext context = new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest());
        IUimUser user = (IUimUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        context.setAccessToken(new UimOAuth2AccessToken(oAuth2AuthorizedClient.getAccessToken(),
                oAuth2AuthorizedClient.getRefreshToken(),
                user.getOAuth2UserRequest().getAdditionalParameters()));
        return context;
    }


    private final static class UimOAuth2AccessToken implements OAuth2AccessToken {

        private org.springframework.security.oauth2.core.OAuth2AccessToken accessToken;
        private org.springframework.security.oauth2.core.OAuth2RefreshToken refreshToken;
        private Map<String, Object> additionalInformation;

        public UimOAuth2AccessToken(org.springframework.security.oauth2.core.OAuth2AccessToken accessToken,
                                    org.springframework.security.oauth2.core.OAuth2RefreshToken refreshToken,
                                    Map<String, Object> additionalInformation) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.additionalInformation = additionalInformation;
        }

        @Override
        public Map<String, Object> getAdditionalInformation() {
            return additionalInformation;
        }

        @Override
        public Set<String> getScope() {
            return accessToken.getScopes();
        }

        @Override
        public OAuth2RefreshToken getRefreshToken() {
            if (refreshToken == null)
                return null;
            if (refreshToken.getExpiresAt() != null)
                return new DefaultExpiringOAuth2RefreshToken(refreshToken.getTokenValue(),
                        new Date(refreshToken.getExpiresAt().toEpochMilli()));
            else
                return new DefaultOAuth2RefreshToken(refreshToken.getTokenValue());
        }

        @Override
        public String getTokenType() {
            return accessToken.getTokenType().getValue();
        }

        @Override
        public boolean isExpired() {
            Logger.getGlobal().info(accessToken.getIssuedAt() + " : " + accessToken.getExpiresAt() + " : " + Instant.now());
            return accessToken.getExpiresAt().isBefore(Instant.now());
        }

        @Override
        public Date getExpiration() {
            return new Date(accessToken.getExpiresAt().toEpochMilli());
        }

        @Override
        public int getExpiresIn() {
            return (int) (accessToken.getExpiresAt().toEpochMilli() - Instant.now().toEpochMilli());
        }

        @Override
        public String getValue() {
            return accessToken.getTokenValue();
        }
    }
}
