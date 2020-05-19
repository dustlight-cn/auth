package cn.dustlight.uim;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootTest
class UimApplicationTests {

    @Autowired
    PasswordEncoder encoder;

    @Test
    void contextLoads() {
        Logger.getGlobal().log(Level.INFO,encoder.encode("order-secret-8888"));
    }

}
