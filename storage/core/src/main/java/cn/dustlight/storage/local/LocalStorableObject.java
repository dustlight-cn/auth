package cn.dustlight.storage.local;

import cn.dustlight.storage.core.IStorableObject;

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

    public File getFile() {
        return file;
    }
}
