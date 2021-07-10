package cn.dustlight.auth.services.storages;

import cn.dustlight.storage.core.RestfulStorage;
import cn.dustlight.storage.core.StorableObject;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class CdnStorageProxy implements RestfulStorage {

    private RestfulStorage storage;
    private String baseUrl;
    private String prefix;
    private boolean simpleUrl;

    public CdnStorageProxy(RestfulStorage storage, String baseUrl) {
        this.storage = storage;
        this.baseUrl = baseUrl;
        this.prefix = "";
    }

    @Override
    public StorableObject create(String key, int permission) throws IOException {
        return storage.create(prefix + key, permission);
    }

    @Override
    public StorableObject get(String key) throws IOException {
        return storage.get(prefix + key);
    }

    @Override
    public StorableObject put(String key, StorableObject source) throws IOException {
        return storage.put(prefix + key, source);
    }

    @Override
    public StorableObject put(String key, StorableObject source, int permission) throws IOException {
        return storage.put(prefix + key, source, permission);
    }

    @Override
    public void remove(String key) throws IOException {
        storage.remove(prefix + key);
    }

    @Override
    public void setPermission(String key, int permission) throws IOException {
        storage.setPermission(prefix + key, permission);
    }

    @Override
    public boolean isExist(String key) throws IOException {
        return storage.isExist(prefix + key);
    }

    @Override
    public String generateGetUrl(String key, Long expiration) throws IOException {
        if (baseUrl == null || baseUrl.length() == 0) {
            URI uri = URI.create(storage.generateGetUrl(prefix + key, expiration));
            return simpleUrl ?
                    uri.getScheme() + "://" + uri.getAuthority() + uri.getRawPath() :
                    uri.toASCIIString();
        }
        return simpleUrl ? baseUrl + prefix + key :
                baseUrl + URI.create(storage.generateGetUrl(prefix + key, expiration)).getRawPath();
    }

    @Override
    public String generatePutUrl(String key, int permission, Long expiration) throws IOException {
        return storage.generatePutUrl(prefix + key, permission, expiration);
    }

    @Override
    public String generateRemoveUrl(String key, Long expiration) throws IOException {
        return storage.generateRemoveUrl(prefix + key, expiration);
    }

    @Override
    public String generatePutUrl(String key, int permission, Long expiration, Map<String, String> headers) throws IOException {
        return storage.generatePutUrl(key, permission, expiration, headers);
    }

    public RestfulStorage getStorage() {
        return storage;
    }

    public void setStorage(RestfulStorage storage) {
        this.storage = storage;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setSimpleUrl(boolean simpleUrl) {
        this.simpleUrl = simpleUrl;
    }

    public boolean isSimpleUrl() {
        return simpleUrl;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
