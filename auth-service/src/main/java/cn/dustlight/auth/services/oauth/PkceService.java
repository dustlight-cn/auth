package cn.dustlight.auth.services.oauth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * PKCE (Proof Key for Code Exchange) Service for OAuth 2.1
 * Implements RFC 7636 for public clients
 */
@Service
public class PkceService {

    private static final Log logger = LogFactory.getLog(PkceService.class);

    /**
     * Validates the code verifier against the code challenge
     * 
     * @param codeVerifier The code verifier from token request
     * @param codeChallenge The code challenge from authorization request
     * @param codeChallengeMethod The challenge method (S256 or plain)
     * @return true if valid, false otherwise
     */
    public boolean validatePkce(String codeVerifier, String codeChallenge, String codeChallengeMethod) {
        if (!StringUtils.hasText(codeVerifier) || !StringUtils.hasText(codeChallenge)) {
            return false;
        }

        // Validate code verifier format (43-128 characters from [A-Z] / [a-z] / [0-9] / "-" / "." / "_" / "~")
        if (!isValidCodeVerifier(codeVerifier)) {
            logger.warn("Invalid code verifier format");
            return false;
        }

        String computedChallenge;
        if ("S256".equals(codeChallengeMethod)) {
            computedChallenge = computeS256Challenge(codeVerifier);
        } else if ("plain".equals(codeChallengeMethod) || codeChallengeMethod == null) {
            computedChallenge = codeVerifier;
        } else {
            logger.warn("Unsupported code challenge method: " + codeChallengeMethod);
            return false;
        }

        return codeChallenge.equals(computedChallenge);
    }

    /**
     * Computes S256 code challenge from verifier
     */
    private String computeS256Challenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Validates code verifier format according to RFC 7636
     * Must be 43-128 characters long and contain only [A-Za-z0-9-._~]
     */
    private boolean isValidCodeVerifier(String codeVerifier) {
        if (codeVerifier == null) {
            return false;
        }
        
        int length = codeVerifier.length();
        if (length < 43 || length > 128) {
            return false;
        }

        return codeVerifier.matches("^[A-Za-z0-9\\-._~]+$");
    }

    /**
     * Checks if PKCE parameters are present in the request
     */
    public boolean isPkceRequest(String codeChallenge) {
        return StringUtils.hasText(codeChallenge);
    }
}
