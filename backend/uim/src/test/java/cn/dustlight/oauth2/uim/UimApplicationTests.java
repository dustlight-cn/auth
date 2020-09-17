package cn.dustlight.oauth2.uim;

import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootTest
class UimApplicationTests {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ObjectMapper mapper;

    Logger logger = Logger.getLogger(UimApplicationTests.class.getName());

    @Test
    void contextLoads() {
        Logger.getGlobal().log(Level.INFO, encoder.encode("123456"));
    }

    @Test
    public void errorEnumJsonTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, ErrorEnum.NO_ERRORS.getDetails());
        logger.info(new String(outputStream.toByteArray()));
    }
}
