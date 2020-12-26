package cn.dustlight.auth.services.oauth;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

public class EnhancedRedisTokenStore {

    private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    private RedisConnectionFactory redisConnectionFactory;
    private String prefix = "";

    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    public EnhancedRedisTokenStore(RedisConnectionFactory connectionFactory) {
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

    protected RedisConnection getConnection() {
        return this.redisConnectionFactory.getConnection();
    }

    public Long countClientToken(String clientId) {
        try (RedisConnection conn = getConnection()) {
            return conn.setCommands().sCard(serializeKey(CLIENT_ID_TO_ACCESS + clientId));
        }
    }
}
