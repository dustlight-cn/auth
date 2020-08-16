package cn.dustlight.uim;

import cn.dustlight.storage.core.IStorableObject;
import cn.dustlight.storage.core.IStorage;
import cn.dustlight.storage.local.LocalStorage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

@SpringBootTest
public class StorageTest {

    @Test
    void test1() throws IOException {
        IStorage storage = LocalStorage.from("storageDir");
        IStorableObject a = storage.create("g/test.txt"), b, c, d;
        OutputStream out = a.getOutputStream();
        out.write("StorageTest".getBytes("UTF-8"));
        out.flush();
        out.close();
        b = storage.put("g/testb.txt", a);
        c = storage.put("g/testc.txt", b);
        d = storage.put("g/testd.txt", b);
        storage.remove("g/testd.txt");
        Logger.getGlobal().info(c.getKey());
    }
}
