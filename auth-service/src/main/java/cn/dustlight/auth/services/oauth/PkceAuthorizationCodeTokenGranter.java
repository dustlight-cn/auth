package cn.dustlight.auth.services.oauth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

/**
 * PKCE-aware Authorization Code Token Granter for OAuth 2.1
 * Validates code_verifier against code_challenge when PKCE is used
 */
public class PkceAuthorizationCodeTokenGranter extends AuthorizationCodeTokenGranter {

    private static final Log logger = LogFactory.getLog(PkceAuthorizationCodeTokenGranter.class);

    private final PkceService pkceService;
    private final PkceAuthorizationCodeService pkceAuthorizationCodeService;

    public PkceAuthorizationCodeTokenGranter(
            AuthorizationServerTokenServices tokenServices,
            AuthorizationCodeServices authorizationCodeServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory,
            PkceService pkceService,
            PkceAuthorizationCodeService pkceAuthorizationCodeService) {
        super(tokenServices, authorizationCodeServices, clientDetailsService, requestFactory);
        this.pkceService = pkceService;
        this.pkceAuthorizationCodeService = pkceAuthorizationCodeService;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = tokenRequest.getRequestParameters();
        String authorizationCode = parameters.get("code");
        String redirectUri = parameters.get("redirect_uri");

        if (authorizationCode == null) {
            throw new InvalidGrantException("An authorization code must be supplied.");
        }

        // Check if this authorization code has PKCE parameters
        Map<String, String> pkceParams = pkceAuthorizationCodeService.removePkceParams(authorizationCode);
        
        if (pkceParams != null && !pkceParams.isEmpty()) {
            // PKCE was used, validate it
            String codeChallenge = pkceParams.get("code_challenge");
            String codeChallengeMethod = pkceParams.get("code_challenge_method");
            String codeVerifier = parameters.get("code_verifier");

            if (codeVerifier == null) {
                throw new InvalidGrantException("PKCE code_verifier is required when code_challenge was provided.");
            }

            if (!pkceService.validatePkce(codeVerifier, codeChallenge, codeChallengeMethod)) {
                throw new InvalidGrantException("Invalid PKCE code_verifier.");
            }

            logger.debug("PKCE validation successful for authorization code");
        }

        // Continue with standard OAuth2 authentication
        return super.getOAuth2Authentication(client, tokenRequest);
    }
}
