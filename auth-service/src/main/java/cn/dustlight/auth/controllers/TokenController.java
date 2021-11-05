package cn.dustlight.auth.controllers;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.configurations.components.TokenConfiguration;
import cn.dustlight.auth.entities.Client;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.util.Constants;
import cn.dustlight.captcha.annotations.Store;
import cn.dustlight.captcha.annotations.Verifier;
import cn.dustlight.captcha.annotations.VerifyCode;
import com.nimbusds.jose.jwk.JWKSet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@Tag(name = "Token", description = "Token 颁发。")
@RestController
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@CrossOrigin(origins = Constants.CrossOrigin.origin, allowCredentials = Constants.CrossOrigin.allowCredentials)
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
    private TokenStore tokenStore;

    @Autowired
    private TokenConfiguration.Jwt jwt;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private ResourceServerTokenServices resourceServerTokenServices;

    @Autowired
    private AccessTokenConverter accessTokenConverter;

    @Autowired
    private OAuth2RequestFactory oAuth2RequestFactory;

    @Autowired
    private OAuth2RequestValidator oAuth2RequestValidator;

    private static final BasicAuthenticationConverter basicConverter = new BasicAuthenticationConverter();

    @VerifyCode(store = @Store("reCaptchaStore"), verifier = @Verifier("reCaptchaVerifier"))
    @Operation(summary = "颁发默认令牌")
    @PostMapping(value = "token")
    public ResponseEntity<OAuth2AccessToken> grantToken(@RequestParam("username") String username,
                                                        @RequestParam("password") String password,
                                                        @RequestParam("g-recaptcha-response") String recaptchaResponse) {
        try {
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
        } catch (Throwable e) {
            throw ErrorEnum.SIGN_IN_FAIL.details(e).getException();
        }
    }

    @Operation(summary = "删除令牌", security = @SecurityRequirement(name = "AccessToken"))
    @DeleteMapping(value = "token")
    public void deleteToken(OAuth2Authentication oAuth2Authentication) {
        if (oAuth2Authentication == null)
            ErrorEnum.UNAUTHORIZED.throwException();
        OAuth2AccessToken accessToken = authorizationServerTokenServices.getAccessToken(oAuth2Authentication);
        if (accessToken != null) {
            if (accessToken.getRefreshToken() != null)
                tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            tokenStore.removeAccessToken(accessToken);
        }
    }

    @Operation(summary = "检查令牌有效性")
    @GetMapping(value = "token/validity")
    public Map<String, ?> checkOAuthToken(@RequestParam("token") String value) {
        OAuth2AccessToken token = this.resourceServerTokenServices.readAccessToken(value);
        if (token == null) {
            throw new InvalidTokenException("Token was not recognised");
        } else if (token.isExpired()) {
            throw new InvalidTokenException("Token has expired");
        } else {
            OAuth2Authentication authentication = this.resourceServerTokenServices.loadAuthentication(token.getValue());
            return this.accessTokenConverter.convertAccessToken(token, authentication);
        }
    }

    @Operation(summary = "检查令牌有效性")
    @PostMapping(value = "token/validity", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Map<String, ?> checkOAuthTokenPost(TokenForm tokenForm) {
        return checkOAuthToken(tokenForm.getToken());
    }

    @Operation(summary = "颁发 OAuth2 令牌", security = @SecurityRequirement(name = "ClientCredentials"))
    @PostMapping("oauth/token")
    public ResponseEntity<OAuth2AccessToken> grantOAuthToken(@RequestParam(value = "code", required = false) String code,
                                                             @Parameter(schema = @Schema(allowableValues = {"authorization_code", "refresh_token", "implicit", "client_credentials", "password"}, defaultValue = "authorization_code"))
                                                             @RequestParam(value = "grant_type", defaultValue = "authorization_code") String grantType,
                                                             @RequestParam(value = "redirect_uri", required = false) String redirectUri,
                                                             @RequestParam(value = "username", required = false) String username,
                                                             @RequestParam(value = "password", required = false) String password,
                                                             @RequestParam(value = "refresh_token", required = false) String refreshToken,
                                                             @RequestParam @Parameter(hidden = true) Map<String, String> parameters,
                                                             HttpServletRequest request) {
        Client client = getClient(request);

        String clientId = client.getClientId();
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

    @Operation(summary = "颁发签名 JWT（JWS）", security = @SecurityRequirement(name = "ClientCredentials"))
    @PostMapping("jws")
    public ResponseEntity<OAuth2AccessToken> grantJws(@RequestParam(value = "code", required = false) String code,
                                                      @Parameter(schema = @Schema(allowableValues = {"authorization_code", "refresh_token", "implicit", "client_credentials", "password"}, defaultValue = "authorization_code"))
                                                      @RequestParam(value = "grant_type", defaultValue = "authorization_code") String grantType,
                                                      @RequestParam(value = "redirect_uri", required = false) String redirectUri,
                                                      @RequestParam(value = "username", required = false) String username,
                                                      @RequestParam(value = "password", required = false) String password,
                                                      @RequestParam(value = "refresh_token", required = false) String refreshToken,
                                                      @RequestParam @Parameter(hidden = true) Map<String, String> parameters,
                                                      HttpServletRequest request) {
        ResponseEntity<OAuth2AccessToken> response = grantOAuthToken(code, grantType, redirectUri, username, password,refreshToken, parameters, request);
        OAuth2AccessToken token = response.getBody();
        OAuth2Authentication auth = resourceServerTokenServices.loadAuthentication(token.getValue());
        return getResponse(jwt.convert(token, auth));
    }

    @Operation(summary = "获取签名 JWT（JWS）", security = @SecurityRequirement(name = "AccessToken"))
    @GetMapping(value = "jws")
    public OAuth2AccessToken getJws(OAuth2Authentication authentication) {
        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(new OAuth2Authentication(authentication.getOAuth2Request(), authentication));
        return jwt.convert(token, authentication);
    }

    @Operation(summary = "获取 JWT 公钥（JWK）")
    @GetMapping(value = "jwk", produces = {JWKSet.MIME_TYPE, MediaType.APPLICATION_JSON_VALUE})
    public Object getJwk() {
        return jwt.keys().toJSONObject();
    }

    protected Client getClient(HttpServletRequest request) {
        UsernamePasswordAuthenticationToken clientPrincipal = basicConverter.convert(request);
        if (clientPrincipal == null)
            ErrorEnum.UNAUTHORIZED.throwException();
        String clientId = clientPrincipal.getName();
        String clientSecret = clientPrincipal.getCredentials() == null ? null : clientPrincipal.getCredentials().toString();

        Client client = clientService.loadClientByClientId(clientId);
        if (client == null)
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        if ((clientSecret == null || client.getClientSecret() == null) && clientSecret != client.getClientSecret() ||
                !passwordEncoder.matches(clientSecret, client.getClientSecret()))
            ErrorEnum.OAUTH_ERROR.details("client secret incorrect").throwException();
        return client;
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

    public static class TokenForm {

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
