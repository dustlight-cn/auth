package cn.dustlight.oauth2.uim.handlers.email;

import cn.dustlight.validator.core.Code;
import cn.dustlight.validator.sender.CodeSender;
import cn.dustlight.validator.sender.SendCodeException;

import java.util.Map;

public class EmailCodeSender implements CodeSender<String> {

    private EmailSenderHandler sender;

    public EmailCodeSender(EmailSenderHandler sender) {
        this.sender = sender;
    }

    @Override
    public void send(Code<String> code, Map<String, Object> parameters) throws SendCodeException {
        try {
            String template = parameters.get("template").toString();
            String email = parameters.get("email").toString();
            sender.send(template, parameters, email);
        } catch (Exception e) {
            throw new SendCodeException("Fail to send email code", e);
        }
    }
}
