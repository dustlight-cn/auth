package cn.dustlight.storage.core;

import java.io.IOException;

public interface IStorage {

    IStorableObject create(String key, int permission) throws IOException;

    IStorableObject get(String key) throws IOException;

    IStorableObject put(String key, IStorableObject source) throws IOException;

    IStorableObject put(String key, IStorableObject source, int permission) throws IOException;

    void remove(String key) throws IOException;

    void setPermission(String key, int permission) throws IOException;
}
