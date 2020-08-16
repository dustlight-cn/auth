package cn.dustlight.storage.local;

import cn.dustlight.storage.core.IStorableObject;
import cn.dustlight.storage.core.IStorage;

import java.io.*;

public class LocalStorage implements IStorage {

    protected String root;

    public static final LocalStorage defaultInstance = new LocalStorage();

    public static LocalStorage from(String root) {
        return new LocalStorage(root);
    }

    public LocalStorage() {
        this.root = ".";
    }

    public LocalStorage(String root) {
        this.root = root;
    }

    @Override
    public IStorableObject create(String key) throws IOException {
        LocalStorableObject result = new LocalStorableObject(root, key);
        File file = result.getFile(), parent = file.getParentFile();
        if (!parent.exists())
            parent.mkdirs();
        result.getFile().createNewFile();
        return result;
    }

    @Override
    public IStorableObject get(String key) throws IOException {
        LocalStorableObject result = new LocalStorableObject(root, key);
        if (!(result.getFile().exists() && result.getFile().isFile()))
            throw new FileNotFoundException();
        return result;
    }

    @Override
    public IStorableObject put(String key, IStorableObject source) throws IOException {
        IStorableObject target = new LocalStorableObject(root, key);
        BufferedInputStream in = new BufferedInputStream(source.getInputStream());
        BufferedOutputStream out = new BufferedOutputStream(target.getOutputStream());
        byte[] buff = new byte[1024];
        int b;
        while ((b = in.read(buff, 0, buff.length)) > 0)
            out.write(buff, 0, b);
        in.close();
        out.close();
        return target;
    }

    @Override
    public void remove(String key) throws IOException {
        File file = new File(root, key);
        if (!(file.exists() && file.isFile()))
            throw new FileNotFoundException();
        if (!file.delete())
            throw new IOException("Can not delete file '" + file.getAbsolutePath() + "', key: ' " + key + "'");
    }

    @Override
    public String generateGetUrl(String key, Long expiration) throws IOException {
        throw new IOException("LocalStorage can not generate url.");
    }

    @Override
    public String generatePutUrl(String key, Long expiration) throws IOException {
        throw new IOException("LocalStorage can not generate url.");
    }
}
