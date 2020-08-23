package cn.dustlight.template.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

@SpringBootTest
public class ClientTemplateTest {

    private final static Logger logger = Logger.getLogger(ClientTemplateTest.class.getName());

    @Test
    public void test(){
        logger.info("Client Template Unit Test");
    }
}
