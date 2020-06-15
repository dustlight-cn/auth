package cn.dustlight.sender.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ITemplateManager {

    List<String> getTemplatesName() throws IOException;

    String getTemplate(String templateName) throws IOException;

    void setTemplate(String templateName, String templateContent) throws IOException;

    Map<String, String> getTemplates() throws IOException;

    void setTemplates(Map<String, String> templates) throws IOException;
}
