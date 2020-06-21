package cn.dustlight.uim.services;

import cn.dustlight.sender.core.ITemplateManager;
import cn.dustlight.uim.models.TemplateNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class TemplateManager implements ITemplateManager {

    @Autowired
    protected TemplateManagerMapper managerMapper;

    @Override
    public List<String> getTemplatesName() throws IOException {
        return managerMapper.getTemplatesName();
    }

    @Override
    public String getTemplate(String templateName) throws IOException {
        return managerMapper.getTemplate(templateName);
    }

    @Override
    public void setTemplate(String templateName, String templateContent) throws IOException {
        managerMapper.setTemplate(templateName, templateContent);
    }

    @Override
    public void deleteTemplate(String... templateNames) throws IOException {
        managerMapper.deleteTemplate(Arrays.asList(templateNames));
    }

    @Override
    public Map<String, String> getTemplates() throws IOException {
        Map<String, String> result = new HashMap<>();
        List<TemplateNode> list = managerMapper.getTemplates();
        for (TemplateNode node : list) {
            result.put(node.name, node.text);
        }
        return result;
    }

    @Override
    public void setTemplates(Map<String, String> templates) throws IOException {
        List<TemplateNode> list = new ArrayList<>(templates.size());
        Set<Map.Entry<String, String>> set = templates.entrySet();
        Iterator<Map.Entry<String, String>> iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> kv = iter.next();
            list.add(new TemplateNode(kv.getKey(), kv.getValue()));
        }
        managerMapper.setTemplates(list);
    }
}