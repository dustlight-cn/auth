package cn.dustlight.sender.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class SimpleEmailSenderTest {

    @Autowired
    SimpleEmailSender sender;

    @Test
    public void send() throws IOException {
        Map<String, String> data = new HashMap<>();
        Map<String, Object> parameters = new HashMap<>();
        String template = "<p th:text=\"${text}\"></p>";

        parameters.put("text", "Hello World");
        data.put("subject", "sender test");


        sender.send(template, data, parameters, "845612500@qq.com");
    }
}
