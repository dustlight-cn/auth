package cn.dustlight.oauth2.uim.handlers.email;

import cn.dustlight.sender.email.EmailSender;
import cn.dustlight.validator.core.Code;
import cn.dustlight.validator.sender.CodeSender;
import cn.dustlight.validator.sender.SendCodeException;

import java.io.IOException;
import java.util.Map;

public class EmailCodeSender implements CodeSender<String> {

    private EmailSender sender;

    public EmailCodeSender(EmailSender sender) {
        this.sender = sender;
    }

    @Override
    public void send(Code<String> code, Map<String, Object> parameters) throws SendCodeException {
        try {
            String title = parameters.getOrDefault("title", "untitled").toString();
            String content = parameters.getOrDefault("content", "no content").toString();
            String email = parameters.getOrDefault("email", "").toString();
            sender.send(content, title, parameters, email);
        } catch (Exception e) {
            throw new SendCodeException("Fail to send email code", e);
        }
    }
}
