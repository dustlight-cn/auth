package cn.dustlight.uim.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class EmailSender implements IEmailSender {

    @Override
    public void Send(String templateName, Map<String, String> parameters, String... receivers) {
        Logger.getLogger(getClass().getName()).info("Send Email: template: \"" + templateName + "\", " +
                "parameters: " + parameters + ", "
                + "receivers: " + Arrays.toString(receivers));
    }
}
