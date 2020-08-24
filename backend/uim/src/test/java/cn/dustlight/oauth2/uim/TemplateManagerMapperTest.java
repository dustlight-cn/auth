package cn.dustlight.oauth2.uim;

import cn.dustlight.oauth2.uim.models.TemplateNode;
import cn.dustlight.oauth2.uim.services.TemplateManagerMapper;
import cn.dustlight.oauth2.uim.utils.Snowflake;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@SpringBootTest
public class TemplateManagerMapperTest {

    @Autowired
    TemplateManagerMapper mapper;

    @Autowired
    Snowflake snowflake;

    Logger logger = Logger.getLogger(getClass().getName());

    @Test
    public void getAllTemplates() {
        List<TemplateNode> result = mapper.getTemplates();
        logger.info(result.toString());
    }

    @Test
    public void getTemplateNames() {
        List<String> result = mapper.getTemplatesName();
        logger.info(result.toString());
    }

    @Test
    public void createTemplate() {
        String name = getClass().getName();
        String text = new Date().toString() + " - " + System.currentTimeMillis();
        mapper.setTemplate(snowflake.getNextId(), name, text);
        logger.info(mapper.getTemplate(name));
    }

    @Test
    public void deleteAll() {
        List<String> list = mapper.getTemplatesName();
        mapper.deleteTemplate(list);
        getTemplateNames();
    }
}
