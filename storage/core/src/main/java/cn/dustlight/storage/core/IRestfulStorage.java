package cn.dustlight.storage.core;

import java.io.IOException;

public interface IRestfulStorage {

    String generateGetUrl(String key, Long expiration) throws IOException;

    String generatePutUrl(String key, int permission, Long expiration) throws IOException;

    String generateRemoveUrl(String key, Long expiration) throws IOException;
}
