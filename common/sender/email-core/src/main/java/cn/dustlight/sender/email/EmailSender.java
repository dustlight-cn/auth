package cn.dustlight.sender.email;

import cn.dustlight.sender.core.ISender;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public abstract class EmailSender implements ISender {


    @Override
    public void send(String template, Map<String, String> data, Map<String, Object> parameters, String... receivers) throws IOException {
        String content = null;
        try {
            content = renderer(template, parameters);
        } catch (TemplateException e) {
            throw new IOException(e);
        }
        String subject = data.getOrDefault("subject", "subject");
        String from = data.getOrDefault("from", null);
        doSend(subject, content, from, receivers);
    }

    public void send(String template, String subject, Map<String, Object> parameters, String... receivers) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("subject", subject);
        send(template, data, parameters, receivers);
    }

    protected String renderer(String template, Map<String, ?> parameters) throws IOException, TemplateException {
        return FreeMarkerTemplateUtils.processTemplateIntoString(new Template("template", new StringReader(template), null), parameters);
    }

    protected abstract void doSend(String subject, String content, String from, String... receivers) throws IOException;
}
