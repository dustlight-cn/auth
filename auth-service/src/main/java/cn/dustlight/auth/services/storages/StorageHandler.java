package cn.dustlight.auth.services.storages;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.storage.core.Permission;
import cn.dustlight.storage.core.RestfulStorage;
import cn.dustlight.storage.core.Storage;
import cn.dustlight.storage.local.LocalStorage;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StorageHandler {

    private Collection<Storage> storages;
    private Storage storage;
    private Long expiration = 1000 * 60 * 15L;

    public StorageHandler(Collection<Storage> storages) {
        this.storages = storages;
    }

    public Storage getStorage() {
        if (this.storage == null) {
            if (this.storages != null) {
                for (Storage s : this.storages) {
                    if (s == null)
                        continue;
                    this.storage = s;
                    break;
                }
            }
            if (this.storage == null) {
                this.storage = LocalStorage.defaultInstance;
            }
        }
        return this.storage;
    }

    public void remove(String key) throws IOException {
        if (storage.isExist(key)) {
            storage.remove(key);
        }
    }

    public void handle(String key, HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.handle(key, request, response, "application/octet-stream");
    }

    public void handle(String key, HttpServletRequest request, HttpServletResponse response, String contentType) throws IOException {
        String method = request.getMethod();
        switch (method) {
            case "GET":
                doGet(key, request, response, contentType);
                break;
            case "PUT":
            case "POST":
            default:
                doPut(key, request, response);
                break;
        }
    }

    protected void doGet(String key, HttpServletRequest request, HttpServletResponse response, String contentType) throws IOException {
        Storage storage = getStorage();
        if (!storage.isExist(key))
            ErrorEnum.RESOURCE_NOT_FOUND.throwException();
        if (storage instanceof RestfulStorage) {
            RestfulStorage restfulStorage = (RestfulStorage) storage;
            String redirectUrl = restfulStorage.generateGetUrl(key, expiration);
            response.setStatus(HttpStatus.FOUND.value());
            response.setHeader("Location", redirectUrl);
        } else {
            response.setContentType(contentType);
            try (OutputStream outputStream = new BufferedOutputStream(response.getOutputStream())) {
                try (InputStream inputStream = new BufferedInputStream(storage.get(key).getInputStream())) {
                    int b;
                    byte[] buff = new byte[1024];
                    while ((b = inputStream.read(buff, 0, buff.length)) != -1) {
                        outputStream.write(buff, 0, b);
                    }
                }
            }
        }
    }

    protected void doPut(String key, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Storage storage = getStorage();
        if (storage instanceof RestfulStorage) {
            RestfulStorage restfulStorage = (RestfulStorage) storage;
            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", request.getContentType());
            String redirectUrl = restfulStorage.generatePutUrl(key, Permission.WRITABLE, expiration, header);
            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
            response.setHeader("Location", redirectUrl);
        } else {
            try (OutputStream outputStream = new BufferedOutputStream(storage.create(key, Permission.WRITABLE).getOutputStream())) {
                try (InputStream inputStream = new BufferedInputStream(request.getInputStream())) {
                    int b;
                    byte[] buff = new byte[1024];
                    while ((b = inputStream.read(buff, 0, buff.length)) != -1) {
                        outputStream.write(buff, 0, b);
                    }
                }
            }
        }
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
