package cn.dustlight.storage.local;

import cn.dustlight.storage.core.IStorableObject;
import cn.dustlight.storage.core.Permission;

import java.io.*;

public class LocalStorableObject implements IStorableObject {

    protected String path;
    protected String fileName;
    protected File file;

    LocalStorableObject(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
        file = new File(path, fileName);
    }

    LocalStorableObject(String path, String fileName, int permission) {
        this.path = path;
        this.fileName = fileName;
        file = new File(path, fileName);
        file.setWritable(Permission.isWritable(permission));
        file.setReadable(Permission.isReadable(permission));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(file);
    }

    @Override
    public String getKey() {
        return fileName;
    }

    @Override
    public int getPermission() {
        return Permission.compute(file.canRead(), file.canWrite());
    }

//    @Override
//    public Long getContentLength() {
//        return file.length();
//    }

    public File getFile() {
        return file;
    }
}
