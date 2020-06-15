package cn.dustlight.sender.email;

import cn.dustlight.sender.core.ISender;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.IOException;
import java.util.Map;

public abstract class EmailSender implements ISender {

    @Autowired
    private SpringTemplateEngine engine;

    private ITemplateResolver templateResolver;

    @Override
    public void send(String template, Map<String, String> data, Map<String, Object> parameters, String... receivers) throws IOException {
        if (templateResolver == null) {
            templateResolver = new StringTemplateResolver();
            engine.setTemplateResolver(templateResolver);
        }
        Context context = new Context();
        context.setVariables(parameters);
        String content = engine.process(template, context);
        String subject = data.getOrDefault("subject", "subject");
        String from = data.getOrDefault("from", "from");
        doSend(subject, content, from, receivers);
    }

    protected abstract void doSend(String subject, String content, String from, String... receivers) throws IOException;
}
