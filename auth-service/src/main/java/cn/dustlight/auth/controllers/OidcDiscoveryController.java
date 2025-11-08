package cn.dustlight.auth.controllers;

import cn.dustlight.auth.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * OpenID Connect Discovery Endpoint
 * Provides metadata about the OpenID Provider's configuration
 */
@Tag(name = "OIDC Discovery", description = "OpenID Connect Discovery Endpoint")
@RestController
@CrossOrigin(origins = Constants.CrossOrigin.origin, allowCredentials = Constants.CrossOrigin.allowCredentials)
public class OidcDiscoveryController {

    @Value("${dustlight.auth.oidc.issuer:}")
    private String issuer;

    @Operation(summary = "OpenID Connect Discovery", 
               description = "返回 OpenID Provider 配置元数据")
    @GetMapping(value = "/.well-known/openid-configuration", produces = "application/json")
    public Map<String, Object> getConfiguration(HttpServletRequest request) {
        String baseUrl = getBaseUrl(request);
        if (issuer == null || issuer.isEmpty()) {
            issuer = baseUrl;
        }

        Map<String, Object> config = new LinkedHashMap<>();
        
        // Core OpenID Connect Discovery 1.0 fields
        config.put("issuer", issuer);
        config.put("authorization_endpoint", baseUrl + Constants.API_ROOT + "oauth/authorize");
        config.put("token_endpoint", baseUrl + Constants.API_ROOT + "oauth/token");
        config.put("userinfo_endpoint", baseUrl + Constants.API_ROOT + "userinfo");
        config.put("jwks_uri", baseUrl + Constants.API_ROOT + "jwk");
        
        // Supported scopes
        config.put("scopes_supported", Arrays.asList(
            "openid",
            "profile", 
            "email",
            "phone"
        ));
        
        // Supported response types
        config.put("response_types_supported", Arrays.asList(
            "code",
            "token",
            "id_token",
            "code token",
            "code id_token",
            "token id_token",
            "code token id_token"
        ));
        
        // Supported response modes
        config.put("response_modes_supported", Arrays.asList(
            "query",
            "fragment",
            "form_post"
        ));
        
        // Supported grant types
        config.put("grant_types_supported", Arrays.asList(
            "authorization_code",
            "implicit",
            "refresh_token",
            "client_credentials"
        ));
        
        // Subject types
        config.put("subject_types_supported", Arrays.asList("public"));
        
        // ID Token signing algorithms
        config.put("id_token_signing_alg_values_supported", Arrays.asList(
            "RS256"
        ));
        
        // Token endpoint auth methods
        config.put("token_endpoint_auth_methods_supported", Arrays.asList(
            "client_secret_basic",
            "client_secret_post"
        ));
        
        // Claims supported
        config.put("claims_supported", Arrays.asList(
            "sub",
            "name",
            "preferred_username",
            "picture",
            "email",
            "email_verified",
            "phone_number",
            "phone_number_verified",
            "gender",
            "updated_at"
        ));
        
        // PKCE support (OAuth 2.1)
        config.put("code_challenge_methods_supported", Arrays.asList(
            "S256",
            "plain"
        ));
        
        // Additional OAuth 2.0 endpoints
        config.put("revocation_endpoint", baseUrl + Constants.API_ROOT + "token");
        config.put("introspection_endpoint", baseUrl + Constants.API_ROOT + "token/validity");
        
        return config;
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getHeader("X-Forwarded-Proto");
        if (scheme == null) {
            scheme = request.getScheme();
        }
        String host = request.getHeader("X-Forwarded-Host");
        if (host == null) {
            host = request.getHeader("Host");
        }
        if (host == null) {
            host = request.getServerName();
            int port = request.getServerPort();
            if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
                host = host + ":" + port;
            }
        }
        return scheme + "://" + host;
    }
}
