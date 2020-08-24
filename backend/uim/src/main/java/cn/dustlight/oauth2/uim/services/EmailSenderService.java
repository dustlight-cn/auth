package cn.dustlight.oauth2.uim.services;

import cn.dustlight.sender.core.ITemplateManager;
import cn.dustlight.sender.email.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class EmailSenderService implements IEmailSender {

    @Autowired
    private EmailSender sender;

    @Autowired
    private ITemplateManager templateManager;

    @Override
    public void send(String templateName, Map<String, Object> parameters, String... receivers) throws IOException {
        Logger.getLogger(getClass().getName()).info("Send Email: template: \"" + templateName + "\", " +
                "parameters: " + parameters + ", "
                + "receivers: " + Arrays.toString(receivers));
        String template = templateManager.getTemplate(templateName);
        Object subject_ = parameters.remove("subject");
        String subject = subject_ == null ? templateName : subject_.toString();
        sender.send(template, subject, parameters, receivers);
    }
}
