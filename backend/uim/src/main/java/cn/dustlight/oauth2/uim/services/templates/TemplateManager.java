package cn.dustlight.oauth2.uim.services.templates;

import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.oauth2.uim.entities.v1.users.User;
import cn.dustlight.oauth2.uim.mappers.TemplateMapper;
import cn.dustlight.sender.core.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class TemplateManager implements cn.dustlight.sender.core.TemplateManager {

    @Autowired
    protected TemplateMapper managerMapper;

    @Autowired
    protected UniqueGenerator<Long> snowflake;

    @Autowired
    User user;

    @Override
    public Collection<String> getNames() throws IOException {
        return managerMapper.getTemplatesName();
    }

    @Override
    public Template getTemplate(String name) throws IOException {
        return managerMapper.getTemplate(name);
    }


    @Override
    public void setTemplate(String name, String title, String content) throws IOException {
        managerMapper.setTemplate(name, user.getUid(), title, content);
    }

    @Override
    public void deleteTemplate(String... templateNames) throws IOException {
        managerMapper.deleteTemplate(Arrays.asList(templateNames));
    }

    @Override
    public Collection<? extends Template> getTemplates() throws IOException {
        return managerMapper.getTemplates();
    }

    @Override
    public void setTemplates(Collection<? extends Template> templates) throws IOException {
        managerMapper.setTemplates((Collection<cn.dustlight.oauth2.uim.entities.v1.templates.Template>) templates);
    }

}
