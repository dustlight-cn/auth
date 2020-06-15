package cn.dustlight.sender.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SenderService implements ISender, ITemplateManager {

    private ISender sender;

    private ITemplateManager templateManager;

    public SenderService(ISender sender, ITemplateManager templateManager) {
        setSender(sender);
        setTemplateManager(templateManager);
    }

    public ISender getSender() {
        return this.sender;
    }

    public void setSender(ISender sender) {
        this.sender = sender;
    }

    public ITemplateManager getTemplateManger() {
        return this.templateManager;
    }

    public void setTemplateManager(ITemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    @Override
    public void send(String template, Map<String, String> data, Map<String, Object> parameters, String... receivers) throws IOException {
        getSender().send(template, data, parameters, receivers);
    }

    public void sendByTemplateName(String templateName, Map<String, String> data, Map<String, Object> parameters, String... receivers) throws IOException {
        String template = getTemplateManger().getTemplate(templateName);
        getSender().send(template, data, parameters, receivers);
    }

    @Override
    public List<String> getTemplatesName() throws IOException {
        return getTemplateManger().getTemplatesName();
    }

    @Override
    public String getTemplate(String templateName) throws IOException {
        return getTemplateManger().getTemplate(templateName);
    }

    @Override
    public void setTemplate(String templateName, String templateContent) throws IOException {
        getTemplateManger().setTemplate(templateName, templateContent);
    }

    @Override
    public Map<String, String> getTemplates() throws IOException {
        return getTemplateManger().getTemplates();
    }

    @Override
    public void setTemplates(Map<String, String> templates) throws IOException {
        getTemplateManger().setTemplates(templates);
    }
}
