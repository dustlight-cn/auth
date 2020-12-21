package cn.dustlight.auth.controllers;

import cn.dustlight.auth.entities.*;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.util.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenRequest;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.security.Principal;
import java.util.*;

@Tag(name = "Authorization", description = "OAuth2 应用授权。")
@RestController
@RequestMapping(path = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@SecurityRequirement(name = "AccessToken")
public class AuthorizationController {

    static final String AUTHORIZATION_REQUEST_ATTR_NAME = "authorizationRequest";
    static final String ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME = "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST";

    private static final Log logger = LogFactory.getLog(AuthorizationController.class.getName());

    @Autowired
    private AuthorizationEndpoint authorizationEndpoint;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ApprovalStore authApprovalStore;

    @Autowired
    private TokenStore authTokenStore;

    @Autowired
    private OAuth2RequestFactory oAuth2RequestFactory;

    @Autowired
    private RedirectResolver redirectResolver;

    @Autowired
    private OAuth2RequestValidator oAuth2RequestValidator;

    @Autowired
    private UserApprovalHandler userApprovalHandler;

    @Autowired
    private TokenGranter tokenGranter;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    private Object implicitLock = new Object();

    /**
     * @see AuthorizationEndpoint
     */
    @Operation(summary = "获取应用授权", description = "获取包含应用信息、所属用户信息、回调地址以及是否已授权。")
    @GetMapping("oauth/authorization")
    public AuthorizationResponse getAuthorization(@Parameter(hidden = true) @RequestParam Map<String, String> parameters,
                                                  @RequestParam("client_id") String clientId,
                                                  @RequestParam(value = "response_type", defaultValue = "code") String responseType,
                                                  @RequestParam(value = "redirect_uri", required = false) String redirectUri,
                                                  @RequestParam(value = "scope", required = false) Collection<String> scopes,
                                                  @RequestParam(value = "state", required = false) String state,
                                                  HttpServletRequest httpServletRequest,
                                                  Principal principal) {
        try {
            AuthorizationRequest authorizationRequest = oAuth2RequestFactory.createAuthorizationRequest(parameters);

            Set<String> responseTypes = authorizationRequest.getResponseTypes();

            if (!responseTypes.contains("token") && !responseTypes.contains("code"))
                throw new UnsupportedResponseTypeException("Unsupported response types: " + responseTypes);
            if (authorizationRequest.getClientId() == null)
                throw new InvalidClientException("A client id must be provided");

            if (!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated())
                throw new InsufficientAuthenticationException("User must be authenticated with Spring Security before authorization can be completed.");

            Client client = clientService.loadClientByClientId(authorizationRequest.getClientId());
            PublicUser owner = clientService.getOwnerPublic(clientId);

            String redirectUriParameter = authorizationRequest.getRequestParameters().get(OAuth2Utils.REDIRECT_URI);
            String resolvedRedirect = redirectResolver.resolveRedirect(redirectUriParameter, client);
            if (!StringUtils.hasText(resolvedRedirect))
                throw new RedirectMismatchException("A redirectUri must be either supplied or preconfigured in the ClientDetails");

            authorizationRequest.setRedirectUri(resolvedRedirect);

            oAuth2RequestValidator.validateScope(authorizationRequest, client);

            authorizationRequest = userApprovalHandler.checkForPreApproval(authorizationRequest, (Authentication) principal);

            boolean approved = userApprovalHandler.isApproved(authorizationRequest, (Authentication) principal);
            authorizationRequest.setApproved(approved);

            AuthorizationResponse response = new AuthorizationResponse();
            AuthorizationResponse.AuthorizationClient authorizationClient = new AuthorizationResponse.AuthorizationClient();
            authorizationClient.setClientId(clientId);
            authorizationClient.setLogo(client.getLogo());
            authorizationClient.setCreatedAt(client.getCreatedAt());
            authorizationClient.setName(client.getName());
            authorizationClient.setDescription(client.getDescription());
            response.setRedirect(resolvedRedirect);
            if (authorizationRequest.isApproved()) {
                if (responseTypes.contains("token")) {
                    response.setRedirect(getImplicitGrantRedirect(authorizationRequest));
                } else if (responseTypes.contains("code")) {
                    response.setRedirect(getAuthorizationCodeRedirect(authorizationRequest, (Authentication) principal));
                }
            } else {
                authorizationClient.setScopes(getUserApprovalScopes(authorizationRequest, principal.getName(), client));
                HttpSession session = httpServletRequest.getSession(true);
                session.setAttribute(AUTHORIZATION_REQUEST_ATTR_NAME, authorizationRequest);
                session.setAttribute(ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME, unmodifiableMap(authorizationRequest));
            }
            response.setClient(authorizationClient);
            response.setOwner(owner);
            response.setApproved(approved);

            return response;
        } catch (RuntimeException e) {
            HttpSession session = httpServletRequest.getSession(false);
            if (session != null) {
                session.removeAttribute(AUTHORIZATION_REQUEST_ATTR_NAME);
                session.removeAttribute(ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME);
            }
            throw e;
        }
    }

    @Operation(summary = "创建应用授权")
    @PostMapping("oauth/authorization")
    public AuthorizationResponse createAuthorization(@RequestParam("approved") boolean approved,
                                                     @RequestParam("scope") Set<String> scopes,
                                                     HttpServletRequest httpServletRequest,
                                                     Principal principal) {
        Map<String, String> approvalParameters = new LinkedHashMap<>();
        approvalParameters.put("user_oauth_approval", approved ? "true" : "false");
        for (String scope : scopes)
            if (scope.startsWith("scope."))
                approvalParameters.put(scope, "true");
            else
                approvalParameters.put("scope." + scope, "true");

        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException("User must be authenticated with Spring Security before authorizing an AccessToken.");
        }
        HttpSession session = httpServletRequest.getSession(false);
        AuthorizationRequest authorizationRequest;
        if (session == null ||
                (authorizationRequest = (AuthorizationRequest) session.getAttribute(AUTHORIZATION_REQUEST_ATTR_NAME)) == null) {
            if (session != null) {
                session.removeAttribute(AUTHORIZATION_REQUEST_ATTR_NAME);
                session.removeAttribute(ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME);
            }
            throw new InvalidRequestException("Cannot approve uninitialized authorization request.");
        }

        // Check to ensure the Authorization Request was not modified during the user approval step
        @SuppressWarnings("unchecked")
        Map<String, Object> originalAuthorizationRequest = (Map<String, Object>) session.getAttribute(ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME);
        if (isAuthorizationRequestModified(authorizationRequest, originalAuthorizationRequest)) {
            throw new InvalidRequestException("Changes were detected from the original authorization request.");
        }

        try {
            Set<String> responseTypes = authorizationRequest.getResponseTypes();

            authorizationRequest.setApprovalParameters(approvalParameters);
            authorizationRequest = userApprovalHandler.updateAfterApproval(authorizationRequest, (Authentication) principal);
            boolean isApproved = userApprovalHandler.isApproved(authorizationRequest, (Authentication) principal);
            authorizationRequest.setApproved(isApproved);

            if (authorizationRequest.getRedirectUri() == null) {
                session.removeAttribute(AUTHORIZATION_REQUEST_ATTR_NAME);
                session.removeAttribute(ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME);
                throw new InvalidRequestException("Cannot approve request when no redirect URI is provided.");
            }

            AuthorizationResponse response = new AuthorizationResponse();
            response.setApproved(isApproved);

            if (!authorizationRequest.isApproved()) {
                response.setRedirect(getUnsuccessfulRedirect(authorizationRequest,
                        new UserDeniedAuthorizationException("User denied access"),
                        responseTypes.contains("token")));
            } else if (responseTypes.contains("token")) {
                response.setRedirect(getImplicitGrantRedirect(authorizationRequest));
            } else {
                response.setRedirect(getAuthorizationCodeRedirect(authorizationRequest, (Authentication) principal));
            }
            return response;
        } finally {
            session.removeAttribute(AUTHORIZATION_REQUEST_ATTR_NAME);
            session.removeAttribute(ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME);
        }

    }

    private Map<String, Object> unmodifiableMap(AuthorizationRequest authorizationRequest) {
        Map<String, Object> authorizationRequestMap = new HashMap<String, Object>();

        authorizationRequestMap.put(OAuth2Utils.CLIENT_ID, authorizationRequest.getClientId());
        authorizationRequestMap.put(OAuth2Utils.STATE, authorizationRequest.getState());
        authorizationRequestMap.put(OAuth2Utils.REDIRECT_URI, authorizationRequest.getRedirectUri());
        if (authorizationRequest.getResponseTypes() != null) {
            authorizationRequestMap.put(OAuth2Utils.RESPONSE_TYPE,
                    Collections.unmodifiableSet(new HashSet<String>(authorizationRequest.getResponseTypes())));
        }
        if (authorizationRequest.getScope() != null) {
            authorizationRequestMap.put(OAuth2Utils.SCOPE,
                    Collections.unmodifiableSet(new HashSet<String>(authorizationRequest.getScope())));
        }
        authorizationRequestMap.put("approved", authorizationRequest.isApproved());
        if (authorizationRequest.getResourceIds() != null) {
            authorizationRequestMap.put("resourceIds",
                    Collections.unmodifiableSet(new HashSet<String>(authorizationRequest.getResourceIds())));
        }
        if (authorizationRequest.getAuthorities() != null) {
            authorizationRequestMap.put("authorities",
                    Collections.unmodifiableSet(new HashSet<GrantedAuthority>(authorizationRequest.getAuthorities())));
        }
        return Collections.unmodifiableMap(authorizationRequestMap);
    }

    /**
     * @see AuthorizationEndpoint
     */
    private boolean isAuthorizationRequestModified(
            AuthorizationRequest authorizationRequest, Map<String, Object> originalAuthorizationRequest) {
        if (!ObjectUtils.nullSafeEquals(
                authorizationRequest.getClientId(),
                originalAuthorizationRequest.get(OAuth2Utils.CLIENT_ID))) {
            return true;
        }
        if (!ObjectUtils.nullSafeEquals(
                authorizationRequest.getState(),
                originalAuthorizationRequest.get(OAuth2Utils.STATE))) {
            return true;
        }
        if (!ObjectUtils.nullSafeEquals(
                authorizationRequest.getRedirectUri(),
                originalAuthorizationRequest.get(OAuth2Utils.REDIRECT_URI))) {
            return true;
        }
        if (!ObjectUtils.nullSafeEquals(
                authorizationRequest.getResponseTypes(),
                originalAuthorizationRequest.get(OAuth2Utils.RESPONSE_TYPE))) {
            return true;
        }
        if (!ObjectUtils.nullSafeEquals(
                authorizationRequest.getScope(),
                originalAuthorizationRequest.get(OAuth2Utils.SCOPE))) {
            return true;
        }
        if (!ObjectUtils.nullSafeEquals(
                authorizationRequest.isApproved(),
                originalAuthorizationRequest.get("approved"))) {
            return true;
        }
        if (!ObjectUtils.nullSafeEquals(
                authorizationRequest.getResourceIds(),
                originalAuthorizationRequest.get("resourceIds"))) {
            return true;
        }
        if (!ObjectUtils.nullSafeEquals(
                authorizationRequest.getAuthorities(),
                originalAuthorizationRequest.get("authorities"))) {
            return true;
        }
        return false;
    }

    /**
     * @see AuthorizationEndpoint
     */
    private Collection<AuthorizationResponse.AuthorizationClientScope> getUserApprovalScopes(AuthorizationRequest authorizationRequest, String username, Client client) {
        Set<String> approvedScopes = new LinkedHashSet<>(); // 已授权的Scopes
        Set<String> requestScopes = authorizationRequest.getScope(); // 请求授权的Scope
        Set<AuthorizationResponse.AuthorizationClientScope> resultScopes = new LinkedHashSet<>();

        /* 获取已授权的Scopes */
        Collection<Approval> approvals = authApprovalStore.getApprovals(username, client.getClientId());
        for (Approval approval : approvals)
            if (approval.isCurrentlyActive())
                approvedScopes.add(approval.getScope());

        for (ClientScope clientScope : client.getScopes()) {
            if (clientScope == null)
                continue;
            if (requestScopes.contains(clientScope.getName())) {
                AuthorizationResponse.AuthorizationClientScope oAuth2ClientScope = AuthorizationResponse.AuthorizationClientScope.from(clientScope);
                if (approvedScopes.contains(clientScope.getName()))
                    oAuth2ClientScope.setApproved(true);
                resultScopes.add(oAuth2ClientScope);
            }
        }
        return resultScopes;
    }

    /**
     * @see AuthorizationEndpoint
     */
    private String getAuthorizationCodeRedirect(AuthorizationRequest authorizationRequest, Authentication authUser) {
        AuthorizationResponse response = new AuthorizationResponse();
        try {
            return getSuccessfulRedirect(authorizationRequest, generateCode(authorizationRequest, authUser));
        } catch (OAuth2Exception e) {
            return getUnsuccessfulRedirect(authorizationRequest, e, false);
        }
    }

    /**
     * @see AuthorizationEndpoint
     */
    private String getImplicitGrantRedirect(AuthorizationRequest authorizationRequest) {
        try {
            TokenRequest tokenRequest = oAuth2RequestFactory.createTokenRequest(authorizationRequest, "implicit");
            OAuth2Request storedOAuth2Request = oAuth2RequestFactory.createOAuth2Request(authorizationRequest);
            OAuth2AccessToken accessToken = getAccessTokenForImplicitGrant(tokenRequest, storedOAuth2Request);
            if (accessToken == null) {
                throw new UnsupportedResponseTypeException("Unsupported response type: token");
            }
            return appendAccessToken(authorizationRequest, accessToken);
        } catch (OAuth2Exception e) {
            return getUnsuccessfulRedirect(authorizationRequest, e, true);
        }
    }

    /**
     * @see AuthorizationEndpoint
     */
    private OAuth2AccessToken getAccessTokenForImplicitGrant(TokenRequest tokenRequest, OAuth2Request storedOAuth2Request) {
        OAuth2AccessToken accessToken = null;
        // These 1 method calls have to be atomic, otherwise the ImplicitGrantService can have a race condition where
        // one thread removes the token request before another has a chance to redeem it.
        synchronized (this.implicitLock) {
            accessToken = tokenGranter.grant("implicit", new ImplicitTokenRequest(tokenRequest, storedOAuth2Request));
        }
        return accessToken;
    }

    /**
     * @see AuthorizationEndpoint
     */
    private String appendAccessToken(AuthorizationRequest authorizationRequest, OAuth2AccessToken accessToken) {
        Map<String, Object> vars = new LinkedHashMap<String, Object>();
        Map<String, String> keys = new HashMap<String, String>();
        if (accessToken == null) {
            throw new InvalidRequestException("An implicit grant could not be made");
        }
        vars.put("access_token", accessToken.getValue());
        vars.put("token_type", accessToken.getTokenType());
        String state = authorizationRequest.getState();
        if (state != null) {
            vars.put("state", state);
        }
        Date expiration = accessToken.getExpiration();
        if (expiration != null) {
            long expires_in = (expiration.getTime() - System.currentTimeMillis()) / 1000;
            vars.put("expires_in", expires_in);
        }
        String originalScope = authorizationRequest.getRequestParameters().get(OAuth2Utils.SCOPE);
        if (originalScope == null || !OAuth2Utils.parseParameterList(originalScope).equals(accessToken.getScope())) {
            vars.put("scope", OAuth2Utils.formatParameterList(accessToken.getScope()));
        }
        Map<String, Object> additionalInformation = accessToken.getAdditionalInformation();
        for (String key : additionalInformation.keySet()) {
            Object value = additionalInformation.get(key);
            if (value != null) {
                keys.put("extra_" + key, key);
                vars.put("extra_" + key, value);
            }
        }
        // Do not include the refresh token (even if there is one)
        return append(authorizationRequest.getRedirectUri(), vars, keys, true);
    }

    /**
     * @see AuthorizationEndpoint
     */
    private String generateCode(AuthorizationRequest authorizationRequest, Authentication authentication) throws AuthenticationException {
        try {
            OAuth2Request storedOAuth2Request = oAuth2RequestFactory.createOAuth2Request(authorizationRequest);
            OAuth2Authentication combinedAuth = new OAuth2Authentication(storedOAuth2Request, authentication);
            String code = authorizationCodeServices.createAuthorizationCode(combinedAuth);
            return code;
        } catch (OAuth2Exception e) {
            if (authorizationRequest.getState() != null) {
                e.addAdditionalInformation("state", authorizationRequest.getState());
            }
            throw e;
        }
    }

    /**
     * @see AuthorizationEndpoint
     */
    private String getSuccessfulRedirect(AuthorizationRequest authorizationRequest, String authorizationCode) {
        if (authorizationCode == null) {
            throw new IllegalStateException("No authorization code found in the current request scope.");
        }
        Map<String, String> query = new LinkedHashMap<String, String>();
        query.put("code", authorizationCode);
        String state = authorizationRequest.getState();
        if (state != null) {
            query.put("state", state);
        }
        return append(authorizationRequest.getRedirectUri(), query, false);
    }

    /**
     * @see AuthorizationEndpoint
     */
    private String getUnsuccessfulRedirect(AuthorizationRequest authorizationRequest, OAuth2Exception failure, boolean fragment) {
        if (authorizationRequest == null || authorizationRequest.getRedirectUri() == null) {
            // we have no redirect for the user. very sad.
            throw new UnapprovedClientAuthenticationException("Authorization failure, and no redirect URI.", failure);
        }
        Map<String, String> query = new LinkedHashMap<String, String>();
        query.put("error", failure.getOAuth2ErrorCode());
        query.put("error_description", failure.getMessage());
        if (authorizationRequest.getState() != null) {
            query.put("state", authorizationRequest.getState());
        }
        if (failure.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> additionalInfo : failure.getAdditionalInformation().entrySet()) {
                query.put(additionalInfo.getKey(), additionalInfo.getValue());
            }
        }
        return append(authorizationRequest.getRedirectUri(), query, fragment);
    }

    /**
     * @see AuthorizationEndpoint
     */
    private String append(String base, Map<String, ?> query, boolean fragment) {
        return append(base, query, null, fragment);
    }

    /**
     * @see AuthorizationEndpoint
     */
    private String append(String base, Map<String, ?> query, Map<String, String> keys, boolean fragment) {
        UriComponentsBuilder template = UriComponentsBuilder.newInstance();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(base);
        URI redirectUri;
        try {
            // assume it's encoded to start with (if it came in over the wire)
            redirectUri = builder.build(true).toUri();
        } catch (Exception e) {
            // ... but allow client registrations to contain hard-coded non-encoded values
            redirectUri = builder.build().toUri();
            builder = UriComponentsBuilder.fromUri(redirectUri);
        }
        template.scheme(redirectUri.getScheme()).port(redirectUri.getPort()).host(redirectUri.getHost())
                .userInfo(redirectUri.getUserInfo()).path(redirectUri.getPath());
        if (fragment) {
            StringBuilder values = new StringBuilder();
            if (redirectUri.getFragment() != null) {
                String append = redirectUri.getFragment();
                values.append(append);
            }
            for (String key : query.keySet()) {
                if (values.length() > 0) {
                    values.append("&");
                }
                String name = key;
                if (keys != null && keys.containsKey(key)) {
                    name = keys.get(key);
                }
                values.append(name + "={" + key + "}");
            }
            if (values.length() > 0) {
                template.fragment(values.toString());
            }
            UriComponents encoded = template.build().expand(query).encode();
            builder.fragment(encoded.getFragment());
        } else {
            for (String key : query.keySet()) {
                String name = key;
                if (keys != null && keys.containsKey(key)) {
                    name = keys.get(key);
                }
                template.queryParam(name, "{" + key + "}");
            }
            template.fragment(redirectUri.getFragment());
            UriComponents encoded = template.build().expand(query).encode();
            builder.query(encoded.getQuery());
        }
        return builder.build().toUriString();
    }
}
