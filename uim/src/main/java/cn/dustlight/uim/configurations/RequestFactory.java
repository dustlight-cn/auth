package cn.dustlight.uim.configurations;

import cn.dustlight.uim.services.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;

@Component
public class RequestFactory implements OAuth2RequestFactory {

    private final ClientDetailsService clientDetailsService;
    private SecurityContextAccessor securityContextAccessor = new DefaultSecurityContextAccessor();
    private boolean checkUserScopes = false;
    @Autowired
    ClientMapper mapper;

    public RequestFactory(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    public void setSecurityContextAccessor(SecurityContextAccessor securityContextAccessor) {
        this.securityContextAccessor = securityContextAccessor;
    }

    public void setCheckUserScopes(boolean checkUserScopes) {
        this.checkUserScopes = checkUserScopes;
    }

    public AuthorizationRequest createAuthorizationRequest(Map<String, String> authorizationParameters) {
        String clientId = (String) authorizationParameters.get("client_id");
        String state = (String) authorizationParameters.get("state");
        String redirectUri = (String) authorizationParameters.get("redirect_uri");
        Set<String> responseTypes = OAuth2Utils.parseParameterList((String) authorizationParameters.get("response_type"));
        Set<String> scopes = this.extractScopes(authorizationParameters, clientId);
        AuthorizationRequest request = new AuthorizationRequest(authorizationParameters, Collections.emptyMap(), clientId, scopes, (Set) null, (Collection) null, false, state, redirectUri, responseTypes);
        ClientDetails clientDetails = this.clientDetailsService.loadClientByClientId(clientId);
//         request.setResourceIdsAndAuthoritiesFromClientDetails(clientDetails);
        request.setResourceIds(clientDetails.getResourceIds());
        request.setAuthorities(getClientAuthorities(scopes, clientDetails));
        return request;
    }

    public OAuth2Request createOAuth2Request(AuthorizationRequest request) {
        return request.createOAuth2Request();
    }

    public TokenRequest createTokenRequest(Map<String, String> requestParameters, ClientDetails authenticatedClient) {
        String clientId = (String) requestParameters.get("client_id");
        if (clientId == null) {
            clientId = authenticatedClient.getClientId();
        } else if (!clientId.equals(authenticatedClient.getClientId())) {
            throw new InvalidClientException("Given client ID does not match authenticated client");
        }

        String grantType = (String) requestParameters.get("grant_type");
        Set<String> scopes = this.extractScopes(requestParameters, clientId);
        TokenRequest tokenRequest = new TokenRequest(requestParameters, clientId, scopes, grantType);
        Logger.getLogger(getClass().getName()).info(scopes.toString());
        return tokenRequest;
    }

    public TokenRequest createTokenRequest(AuthorizationRequest authorizationRequest, String grantType) {
        TokenRequest tokenRequest = new TokenRequest(authorizationRequest.getRequestParameters(), authorizationRequest.getClientId(), authorizationRequest.getScope(), grantType);
        return tokenRequest;
    }

    public OAuth2Request createOAuth2Request(ClientDetails client, TokenRequest tokenRequest) {
        return tokenRequest.createOAuth2Request(client);
    }

    private Collection<GrantedAuthority> getClientAuthorities(Set<String> scopes, ClientDetails clientDetails) {
        Collection<GrantedAuthority> grantedAuthorities = clientDetails.getAuthorities();
        if (scopes == null || scopes.isEmpty())
            return grantedAuthorities;
        String[] arr = mapper.getAuthoritiesByScopeNames(scopes.toArray(new String[scopes.size()]));
        if (arr != null && arr.length > 0) {
            if (grantedAuthorities == null)
                grantedAuthorities = new LinkedHashSet<>();
            grantedAuthorities.addAll(AuthorityUtils.createAuthorityList(arr));
        }
        return grantedAuthorities;
    }

    private Set<String> extractScopes(Map<String, String> requestParameters, String clientId) {
        Set<String> scopes = OAuth2Utils.parseParameterList((String) requestParameters.get("scope"));
        ClientDetails clientDetails = this.clientDetailsService.loadClientByClientId(clientId);
        if (scopes == null || scopes.isEmpty()) {
            scopes = clientDetails.getScope();
        }

        if (this.checkUserScopes) {
            scopes = this.checkUserScopes(scopes, clientDetails);
        }

        return scopes;
    }

    private Set<String> checkUserScopes(Set<String> scopes, ClientDetails clientDetails) {
        if (!this.securityContextAccessor.isUser()) {
            return scopes;
        } else {
            Set<String> result = new LinkedHashSet();
            Set<String> authorities = AuthorityUtils.authorityListToSet(this.securityContextAccessor.getAuthorities());
            Iterator var5 = scopes.iterator();

            while (true) {
                String scope;
                do {
                    if (!var5.hasNext()) {
                        return result;
                    }

                    scope = (String) var5.next();
                } while (!authorities.contains(scope) && !authorities.contains(scope.toUpperCase()) && !authorities.contains("ROLE_" + scope.toUpperCase()));

                result.add(scope);
            }
        }
    }
}
