package cn.dustlight.oauth2.uim.client.services;

import cn.dustlight.oauth2.uim.client.confgurations.UimClientProperties;
import cn.dustlight.oauth2.uim.client.converter.DefaultUserConverter;
import cn.dustlight.oauth2.uim.client.converter.IUimUserConverter;
import cn.dustlight.oauth2.uim.client.entity.IUimUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class OAuth2UserService implements org.springframework.security.oauth2.client.userinfo.OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";
    private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";
    private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";
    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<Map<String, Object>>() {
    };
    private Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
    private RestOperations restOperations;
    private Map<String, IUimUserConverter> customConverter;
    private IUimUserConverter converter;
    private Map<String, String> userDataPath;
    private Map<String, UimClientProperties.UserDetailsMapping> userDetailsMappingMap;

    public OAuth2UserService(Map<String, IUimUserConverter> customConverter,
                             Map<String, String> userDataPath,
                             Map<String, UimClientProperties.UserDetailsMapping> userDetailsMapping) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        this.restOperations = restTemplate;
        this.customConverter = customConverter;
        this.converter = new DefaultUserConverter();
        this.userDataPath = userDataPath;
        this.userDetailsMappingMap = userDetailsMapping;
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");
        if (!StringUtils.hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
            OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_INFO_URI_ERROR_CODE, "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), (String) null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        } else {
            String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
            if (!StringUtils.hasText(userNameAttributeName)) {
                OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE, "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), (String) null);
                throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
            } else {
                RequestEntity request = (RequestEntity) this.requestEntityConverter.convert(userRequest);

                ResponseEntity response;
                OAuth2Error oauth2Error;
                try {
                    response = this.restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
                } catch (OAuth2AuthorizationException var10) {
                    oauth2Error = var10.getError();
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("Error details: [");
                    errorDetails.append("UserInfo Uri: ").append(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
                    errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
                    if (oauth2Error.getDescription() != null) {
                        errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
                    }

                    errorDetails.append("]");
                    oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE, "An error occurred while attempting to retrieve the UserInfo Resource: " + errorDetails.toString(), (String) null);
                    throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var10);
                } catch (RestClientException var11) {
                    oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE, "An error occurred while attempting to retrieve the UserInfo Resource: " + var11.getMessage(), (String) null);
                    throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var11);
                }

                String clientName = userRequest.getClientRegistration().getClientName();
                Map<String, Object> userAttributes = (Map) response.getBody();
                if (userDataPath != null && userDataPath.get(clientName) != null) {
                    String[] path = userDataPath.get(clientName).split("/");
                    for (int i = 0, len = path.length; i < len; i++) {
                        String k = path[i];
                        if (userAttributes == null || userAttributes.get(k) == null
                                || !(userAttributes.get(k) instanceof Map))
                            break;
                        userAttributes = (Map<String, Object>) userAttributes.get(k);
                    }
                }
                String clientUser = userAttributes == null || userNameAttributeName == null || userAttributes.get(userNameAttributeName) == null ?
                        null : userAttributes.get(userNameAttributeName).toString();
                IUimUserConverter converter = customConverter != null ? customConverter.getOrDefault(clientName, this.converter) : this.converter;
                IUimUser user = converter.convert(clientName,
                        clientUser,
                        userAttributes,
                        userRequest,
                        userDetailsMappingMap.get(userRequest.getClientRegistration().getRegistrationId()));
                return user;
            }
        }
    }

    public final void setRequestEntityConverter(Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter) {
        Assert.notNull(requestEntityConverter, "requestEntityConverter cannot be null");
        this.requestEntityConverter = requestEntityConverter;
    }

    public final void setRestOperations(RestOperations restOperations) {
        Assert.notNull(restOperations, "restOperations cannot be null");
        this.restOperations = restOperations;
    }
}
