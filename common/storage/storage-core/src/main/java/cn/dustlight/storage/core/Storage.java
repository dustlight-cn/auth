package cn.dustlight.storage.core;

import java.io.IOException;

public interface Storage {

    StorableObject create(String key, int permission) throws IOException;

    StorableObject get(String key) throws IOException;

    StorableObject put(String key, StorableObject source) throws IOException;

    StorableObject put(String key, StorableObject source, int permission) throws IOException;

    void remove(String key) throws IOException;

    void setPermission(String key, int permission) throws IOException;

    boolean isExist(String key) throws IOException;
}
