package cn.dustlight.storage.core;

import java.io.IOException;

public interface IStorage {

    IStorableObject create(String key) throws IOException;

    IStorableObject get(String key) throws IOException;

    IStorableObject put(String key, IStorableObject source) throws IOException;

    void remove(String key) throws IOException;

    String generateGetUrl(String key, Long expiration) throws IOException;

    String generatePutUrl(String key, Long expiration) throws IOException;
}
