package cn.dustlight.sender.core;

import java.io.IOException;
import java.util.Collection;

public interface TemplateManager {

    Collection<String> getNames() throws IOException;

    Template getTemplate(String name) throws IOException;

    void setTemplate(String name, String title, String content) throws IOException;

    void deleteTemplate(String... names) throws IOException;

    Collection<? extends Template> getTemplates() throws IOException;

    void setTemplates(Collection<? extends Template> templates) throws IOException;
}
