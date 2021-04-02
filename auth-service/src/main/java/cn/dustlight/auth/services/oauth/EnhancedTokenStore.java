package cn.dustlight.auth.services.oauth;

import java.io.IOException;

public interface EnhancedTokenStore {
    Long countClientToken(String clientId);

    void deleteUserToken(String username) throws IOException;
}
