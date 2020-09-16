package cn.dustlight.oauth2.uim;

import cn.dustlight.oauth2.uim.handlers.code.VerificationCodeGenerator;
import cn.dustlight.oauth2.uim.handlers.email.DefaultEmailSenderHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;

@SpringBootTest
public class EmailSenderTest {

    @Autowired
    DefaultEmailSenderHandler sender;

    @Autowired
    private VerificationCodeGenerator codeGenerator;

    @Test
    public void sendTest() throws IOException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("code", codeGenerator.generateCode(6));
        sender.send("邮箱验证", data, "845612500@qq.com");
    }
}
