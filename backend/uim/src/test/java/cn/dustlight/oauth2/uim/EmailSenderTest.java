package cn.dustlight.oauth2.uim;

import cn.dustlight.oauth2.uim.services.EmailSenderService;
import cn.dustlight.oauth2.uim.services.IVerificationCodeGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;

@SpringBootTest
public class EmailSenderTest {

    @Autowired
    EmailSenderService sender;

    @Autowired
    private IVerificationCodeGenerator codeGenerator;

    @Test
    public void sendTest() throws IOException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("code", codeGenerator.generatorCode(6));

        sender.send("registerVerificationCode", data, "845612500@qq.com");
    }
}
