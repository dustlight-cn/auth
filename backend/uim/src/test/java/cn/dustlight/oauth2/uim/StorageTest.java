package cn.dustlight.oauth2.uim;

import cn.dustlight.storage.core.Permission;
import cn.dustlight.storage.local.LocalStorableObject;
import cn.dustlight.storage.local.LocalStorage;
import cn.dustlight.storage.tencent.cos.TencentCloudObjectStorage;
import cn.dustlight.storage.tencent.cos.TencentCloudStorableObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.logging.Logger;

@SpringBootTest
public class StorageTest {

    private static byte[] data;

    static {
        try {
            data = "StorageTest".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test1() throws IOException {
        LocalStorage storage = LocalStorage.from("storageDir");
        LocalStorableObject a = storage.create("g/test.txt", Permission.PRIVATE), b, c, d;
        OutputStream out = a.getOutputStream();
        out.write(data);
        out.flush();
        out.close();
        b = storage.put("g/testb.txt", a);
        c = storage.put("g/testc.txt", b);
        d = storage.put("g/testd.txt", b);
        print(a);
        print(b);
        print(c);
        print(d);
        storage.remove("g/testd.txt");
    }

    private static void print(LocalStorableObject obj) {
        File file = obj.getFile();
        Logger.getGlobal().info("File: " + file.getAbsolutePath() + ", readable: " + file.canRead() + ", writable: " + file.canWrite());
    }

    @Autowired
    TencentCloudObjectStorage tencentCloudObjectStorage;

    @Test
    void test2() throws IOException {
        TencentCloudStorableObject a = tencentCloudObjectStorage.create("test.txt", Permission.READABLE);
        OutputStream out = a.getOutputStream();
        out.write(data);
        out.close();
    }

    @Test
    void test3() throws IOException {
        tencentCloudObjectStorage.remove("test.txt");
    }

    @Test
    void test4() throws IOException {
        String p, g, d;
        p = tencentCloudObjectStorage.generatePutUrl("test.txt", Permission.READABLE, 10L * 1000L * 60);
        g = tencentCloudObjectStorage.generateGetUrl("test.txt", 10L * 1000L * 60);
        d = tencentCloudObjectStorage.generateRemoveUrl("test.txt", 10L * 1000L * 60);
        Logger.getGlobal().info(p);
        Logger.getGlobal().info(g);
        Logger.getGlobal().info(d);
    }

}
