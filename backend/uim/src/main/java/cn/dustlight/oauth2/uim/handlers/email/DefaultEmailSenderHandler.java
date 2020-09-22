package cn.dustlight.oauth2.uim.handlers.email;

import cn.dustlight.sender.core.Template;
import cn.dustlight.sender.core.TemplateManager;
import cn.dustlight.sender.email.EmailSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Service
public class DefaultEmailSenderHandler implements EmailSenderHandler {

    @Autowired
    private EmailSender sender;

    @Autowired
    private TemplateManager templateManager;

    private final Log logger = LogFactory.getLog(this.getClass().getName());

    @Override
    public void send(String templateName, Map<String, Object> parameters, String... receivers) throws IOException {
        logger.debug("Send Email: template: \"" + templateName + "\", " +
                "parameters: " + parameters + ", "
                + "receivers: " + Arrays.toString(receivers));
        Template template = templateManager.getTemplate(templateName);
        sender.send(template.getContent(), template.getTitle(), parameters, receivers);
    }
}
