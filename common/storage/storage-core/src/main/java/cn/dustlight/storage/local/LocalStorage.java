package cn.dustlight.storage.local;

import cn.dustlight.storage.core.StorableObject;
import cn.dustlight.storage.core.Storage;
import cn.dustlight.storage.core.Permission;

import java.io.*;

public class LocalStorage implements Storage {

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
    public LocalStorableObject create(String key, int permission) throws IOException {
        LocalStorableObject result = new LocalStorableObject(root, key);
        File file = result.getFile(), parent = file.getParentFile();
        file.setReadable(Permission.isReadable(permission));
        file.setWritable(Permission.isWritable(permission));
        if (!parent.exists())
            parent.mkdirs();
        result.getFile().createNewFile();
        return result;
    }

    @Override
    public LocalStorableObject get(String key) throws IOException {
        LocalStorableObject result = new LocalStorableObject(root, key);
        if (!(result.getFile().exists() && result.getFile().isFile()))
            throw new FileNotFoundException();
        return result;
    }

    @Override
    public LocalStorableObject put(String key, StorableObject source) throws IOException {
        return put(key, source, source.getPermission());
    }

    @Override
    public LocalStorableObject put(String key, StorableObject source, int permission) throws IOException {
        LocalStorableObject target = new LocalStorableObject(root, key, permission);
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
    public void setPermission(String key, int permission) throws IOException {
        File file = new File(root, key);
        if (!(file.exists() && file.isFile()))
            throw new FileNotFoundException();
        file.setReadable(Permission.isReadable(permission));
        file.setWritable(Permission.isWritable(permission));
    }

    @Override
    public boolean isExist(String key) throws IOException {
        File file = new File(root, key);
        return file.exists() && file.isFile();
    }

}
