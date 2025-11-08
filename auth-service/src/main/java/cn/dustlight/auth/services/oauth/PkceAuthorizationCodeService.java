package cn.dustlight.auth.services.oauth;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * PKCE-aware Authorization Code Service
 * Stores code challenge and method along with the authorization
 */
public class PkceAuthorizationCodeService extends RedisAuthorizationCodeService {

    private static final String PKCE_KEY_PREFIX = "pkce:";

    public PkceAuthorizationCodeService(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    /**
     * Store authorization code with PKCE parameters
     */
    public void storeWithPkce(String code, OAuth2Authentication authentication, String codeChallenge, String codeChallengeMethod) {
        // Store the normal authorization
        super.store(code, authentication);
        
        // Store PKCE parameters if present
        if (codeChallenge != null) {
            Map<String, String> pkceParams = new HashMap<>();
            pkceParams.put("code_challenge", codeChallenge);
            pkceParams.put("code_challenge_method", codeChallengeMethod != null ? codeChallengeMethod : "plain");
            
            getRedisTemplate().opsForValue().set(
                PKCE_KEY_PREFIX + code,
                pkceParams,
                Duration.ofMillis(getDuration())
            );
        }
    }

    /**
     * Retrieve and remove PKCE parameters for a code
     */
    public Map<String, String> removePkceParams(String code) {
        String key = PKCE_KEY_PREFIX + code;
        @SuppressWarnings("unchecked")
        Map<String, String> pkceParams = (Map<String, String>) getRedisTemplate().opsForValue().get(key);
        getRedisTemplate().delete(key);
        return pkceParams;
    }

    /**
     * Check if code has PKCE parameters
     */
    public boolean hasPkceParams(String code) {
        return Boolean.TRUE.equals(getRedisTemplate().hasKey(PKCE_KEY_PREFIX + code));
    }
}
