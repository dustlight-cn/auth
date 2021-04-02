package cn.dustlight.auth.services.oauth;

import java.io.IOException;

public class EnhancedJwtTokenStore implements EnhancedTokenStore {

    @Override
    public Long countClientToken(String clientId) {
        return 0L;
    }

    @Override
    public void deleteUserToken(String username) throws IOException {

    }
}
