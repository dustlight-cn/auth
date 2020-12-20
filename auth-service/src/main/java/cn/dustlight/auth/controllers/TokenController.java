package cn.dustlight.auth.controllers;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.Client;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.util.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Tag(name = "Token", description = "负责 Token 颁发。")
@SecurityRequirement(name = "Client Credentials")
@RestController
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public class TokenController {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenGranter tokenGranter;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private OAuth2RequestFactory oAuth2RequestFactory;

    @Autowired
    private OAuth2RequestValidator oAuth2RequestValidator;

    private static final BasicAuthenticationConverter basicConverter = new BasicAuthenticationConverter();


    @Operation(summary = "颁发默认令牌")
    @PostMapping(value = "oauth/token_default")
    public ResponseEntity<OAuth2AccessToken> grantDefaultToken(@RequestParam("username") String username,
                                                               @RequestParam("password") String password) {
        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        userAuth = authenticationManager.authenticate(userAuth);
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }
        Client defaultClient = clientService.loadClientByClientId("default");
        TokenRequest tokenRequest = new TokenRequest(null, defaultClient.getClientId(), defaultClient.getScope(), null);
        OAuth2Request request = tokenRequest.createOAuth2Request(defaultClient);
        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(new OAuth2Authentication(request, userAuth));
        return getResponse(token);
    }

    @Operation(summary = "颁发令牌")
    @PostMapping("oauth/token")
    public ResponseEntity<OAuth2AccessToken> grantToken(@RequestParam(value = "code", required = false) String code,
                                                        @RequestParam(value = "grant_type", defaultValue = "authorization_code") String grantType,
                                                        @RequestParam(value = "redirect_uri", required = false) String redirectUri,
                                                        @RequestParam(value = "username", required = false) String username,
                                                        @RequestParam(value = "password", required = false) String password,
                                                        @RequestParam @Parameter(hidden = true) Map<String, String> parameters,
                                                        HttpServletRequest request) {

        UsernamePasswordAuthenticationToken clientPrincipal = basicConverter.convert(request);
        if (clientPrincipal == null)
            ErrorEnum.UNAUTHORIZED.throwException();
        String clientName = clientPrincipal.getName();
        String clientSecret = clientPrincipal.getCredentials() == null ? null : clientPrincipal.getCredentials().toString();

        Client client = clientService.loadClientByClientId(clientName);
        if (client == null)
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        if ((clientSecret == null || client.getClientSecret() == null) && clientSecret != client.getClientSecret() ||
                !passwordEncoder.matches(clientSecret, client.getClientSecret()))
            ErrorEnum.OAUTH_ERROR.details("client secret incorrect").throwException();


        String clientId = clientName;
        ClientDetails authenticatedClient = client;

        TokenRequest tokenRequest = oAuth2RequestFactory.createTokenRequest(parameters, authenticatedClient);

        // 检查 ClientID
        if (clientId != null && !clientId.equals("") && !clientId.equals(tokenRequest.getClientId()))
            throw new InvalidClientException("Given client ID does not match authenticated client");

        // 检查 ClientScope
        if (authenticatedClient != null)
            oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);

        // 检查 GrantType 是否为空
        if (!StringUtils.hasText(tokenRequest.getGrantType()))
            throw new InvalidRequestException("Missing grant type");

        // 检查 GrantType 是否有效
        if (tokenRequest.getGrantType().equals("implicit"))
            throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
        if (isAuthCodeRequest(parameters) && !tokenRequest.getScope().isEmpty()) {
            logger.debug("Clearing scope of incoming token request");
            tokenRequest.setScope(Collections.<String>emptySet());
        }
        if (isRefreshTokenRequest(parameters))
            tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));

        // 授权
        OAuth2AccessToken token = tokenGranter.grant(tokenRequest.getGrantType(), tokenRequest);
        if (token == null)
            throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
        return getResponse(token);
    }

    private ResponseEntity<OAuth2AccessToken> getResponse(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(accessToken, headers, HttpStatus.OK);
    }

    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }

    private boolean isAuthCodeRequest(Map<String, String> parameters) {
        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
    }
}
