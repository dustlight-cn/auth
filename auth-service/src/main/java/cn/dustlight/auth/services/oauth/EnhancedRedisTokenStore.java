package cn.dustlight.auth.services.oauth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

import java.io.IOException;
import java.util.Set;

public class EnhancedRedisTokenStore implements EnhancedTokenStore {

    private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";

    private static final String USERNAME_TO_ACCESS = "uname_to_access:";

    private RedisConnectionFactory redisConnectionFactory;
    private RedisTokenStore redisTokenStore;
    private String prefix = "";

    private static final Log logger = LogFactory.getLog(EnhancedRedisTokenStore.class);

    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    public EnhancedRedisTokenStore(RedisConnectionFactory connectionFactory, RedisTokenStore redisTokenStore) {
        this.redisTokenStore = redisTokenStore;
        this.redisConnectionFactory = connectionFactory;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    protected byte[] serializeKey(String key) {
        return serializationStrategy.serialize(prefix + key);
    }

    protected String deserializeKey(byte[] bytes) {
        return serializationStrategy.deserializeString(bytes);
    }

    protected <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return serializationStrategy.deserialize(bytes, clazz);
    }

    protected RedisConnection getConnection() {
        return this.redisConnectionFactory.getConnection();
    }

    @Override
    public Long countClientToken(String clientId) {
        try (RedisConnection conn = getConnection()) {
            return conn.setCommands().sCard(serializeKey(CLIENT_ID_TO_ACCESS + clientId));
        }
    }

    @Override
    public void deleteUserToken(String username) throws IOException {
        try (RedisConnection conn = getConnection()) {
            try (Cursor<byte[]> cursor = conn.keyCommands().scan(ScanOptions.scanOptions()
                    .match(USERNAME_TO_ACCESS + "*:" + username)
                    .count(Long.MAX_VALUE)
                    .build())) {
                cursor.forEachRemaining(bytes -> {
                    String key = deserializeKey(bytes);
                    Set<byte[]> tokenSet = conn.sMembers(bytes);
                    if (tokenSet == null)
                        return;
                    tokenSet.forEach(tokenBytes -> {
                        DefaultOAuth2AccessToken accessToken = deserialize(tokenBytes, DefaultOAuth2AccessToken.class);
                        redisTokenStore.removeAccessToken(accessToken);
                        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
                        if (refreshToken != null)
                            redisTokenStore.removeRefreshToken(refreshToken);
                    });
                });
            }
        }
    }
}
