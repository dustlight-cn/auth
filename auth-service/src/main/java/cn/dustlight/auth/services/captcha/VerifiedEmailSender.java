package cn.dustlight.auth.services.captcha;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.captcha.EmailSenderProperties;
import cn.dustlight.captcha.core.Code;
import cn.dustlight.captcha.sender.EmailCodeSender;
import cn.dustlight.captcha.sender.SendCodeException;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Map;

public class VerifiedEmailSender extends EmailCodeSender {

    private UserService userService;

    public VerifiedEmailSender(JavaMailSender javaMailSender,
                               MailProperties mailProperties,
                               EmailSenderProperties properties,
                               TemplateProvider templateProvider,
                               UserService userService) {
        super(javaMailSender, mailProperties, properties, templateProvider);
        this.userService = userService;
    }

    @Override
    public void send(Code<String> code, Map<String, Object> parameters) throws SendCodeException {
        String emailParamName = getEmailParam();
        if (!parameters.containsKey(emailParamName) || parameters.get(emailParamName) == null)
            throw new SendCodeException(String.format("Parameter '%s' not found!", emailParamName));
        String email = parameters.get(emailParamName).toString();
        boolean target = checkExists(parameters);
        if (this.userService.isEmailExists(email) == target)
            super.send(code, parameters);
        else {
            if (target)
                ErrorEnum.EMAIL_NOT_FOUND.throwException();
            else
                ErrorEnum.EMAIL_EXISTS.throwException();
        }
    }

    protected boolean checkExists(Map<String, Object> parameters) {
        return Boolean.parseBoolean(parameters.getOrDefault("CHECK_EXISTS", "true").toString());
    }
}
