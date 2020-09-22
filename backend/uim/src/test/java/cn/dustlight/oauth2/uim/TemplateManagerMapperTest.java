//package cn.dustlight.oauth2.uim;
//
//import cn.dustlight.generator.snowflake.SnowflakeIdGenerator;
//import cn.dustlight.oauth2.uim.entities.Template;
//import cn.dustlight.oauth2.uim.services.TemplateManagerMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Date;
//import java.util.List;
//import java.util.logging.Logger;
//
//@SpringBootTest
//public class TemplateManagerMapperTest {
//
//    @Autowired
//    TemplateManagerMapper mapper;
//
//    @Autowired
//    SnowflakeIdGenerator snowflake;
//
//    Logger logger = Logger.getLogger(getClass().getName());
//
//    @Test
//    public void getAllTemplates() {
//        List<Template> result = mapper.getTemplates();
//        logger.info(result.toString());
//    }
//
//    @Test
//    public void getTemplateNames() {
//        List<String> result = mapper.getTemplatesName();
//        logger.info(result.toString());
//    }
//
//    @Test
//    public void createTemplate() {
//        String name = getClass().getName();
//        String text = new Date().toString() + " - " + System.currentTimeMillis();
//        mapper.setTemplate(snowflake.generate(), name, text);
//        logger.info(mapper.getTemplate(name));
//    }
//
//    @Test
//    public void deleteAll() {
////        List<String> list = mapper.getTemplatesName();
////        mapper.deleteTemplate(list);
////        getTemplateNames();
//    }
//
//    @Test
//    public void snowflakeTest() {
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++)
//            logger.info(snowflake.generate() + "");
//        logger.info((System.currentTimeMillis() - start) + "ms");
//    }
//}
