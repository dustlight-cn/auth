package cn.dustlight.auth.services.oauth;

import cn.dustlight.auth.entities.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.*;

/**
 * OpenID Connect Token Enhancer
 * Adds ID Token claims when 'openid' scope is requested
 */
public class OidcTokenEnhancer implements TokenEnhancer {

    private String issuer;

    public OidcTokenEnhancer(String issuer) {
        this.issuer = issuer;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (authentication.getOAuth2Request().getScope().contains("openid")) {
            Map<String, Object> additionalInfo = new HashMap<>(accessToken.getAdditionalInformation());
            
            // Add ID Token as additional information
            String idToken = createIdToken(authentication);
            if (idToken != null) {
                additionalInfo.put("id_token", idToken);
            }
            
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        }
        
        return accessToken;
    }

    private String createIdToken(OAuth2Authentication authentication) {
        if (authentication.getUserAuthentication() == null) {
            return null;
        }

        Object principal = authentication.getUserAuthentication().getPrincipal();
        if (!(principal instanceof User)) {
            return null;
        }

        User user = (User) principal;
        Set<String> scopes = authentication.getOAuth2Request().getScope();
        
        Map<String, Object> claims = new HashMap<>();
        
        // Standard claims
        claims.put("iss", issuer != null ? issuer : "https://accounts.dustlight.cn");
        claims.put("sub", String.valueOf(user.getUid()));
        claims.put("aud", authentication.getOAuth2Request().getClientId());
        claims.put("exp", System.currentTimeMillis() / 1000 + 3600); // 1 hour
        claims.put("iat", System.currentTimeMillis() / 1000);
        
        // Add nonce if present in the request
        Map<String, String> requestParams = authentication.getOAuth2Request().getRequestParameters();
        if (requestParams.containsKey("nonce")) {
            claims.put("nonce", requestParams.get("nonce"));
        }
        
        // Profile scope claims
        if (scopes.contains("profile")) {
            if (user.getNickname() != null) {
                claims.put("name", user.getNickname());
                claims.put("preferred_username", user.getUsername());
            }
            if (user.getAvatar() != null) {
                claims.put("picture", user.getAvatar());
            }
            if (user.getGender() != 0) {
                String gender = user.getGender() == 1 ? "male" : user.getGender() == 2 ? "female" : "other";
                claims.put("gender", gender);
            }
            if (user.getUpdatedAt() != null) {
                claims.put("updated_at", user.getUpdatedAt().getTime() / 1000);
            }
        }
        
        // Email scope claims
        if (scopes.contains("email") && user.getEmail() != null) {
            claims.put("email", user.getEmail());
            claims.put("email_verified", true);
        }
        
        // Phone scope claims
        if (scopes.contains("phone") && user.getPhone() != null) {
            claims.put("phone_number", user.getPhone());
            claims.put("phone_number_verified", true);
        }
        
        // For now, return null as ID tokens should be JWT signed
        // The actual JWT signing will be handled by the JwtAccessTokenConverter
        // This is a placeholder that will be properly integrated with JWT infrastructure
        return null;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
