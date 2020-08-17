package cn.dustlight.storage.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IStorableObject {

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    String getKey();

    int getPermission();

//    Long getContentLength();
}
