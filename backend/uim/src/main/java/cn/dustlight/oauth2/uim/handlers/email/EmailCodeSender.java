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
            parameters.put("code", code.getValue());
            sender.send("您的验证码是${code}", "Email Register", parameters, parameters.get("email").toString());
        } catch (IOException e) {
            throw new SendCodeException("?", e);
        }
    }
}
