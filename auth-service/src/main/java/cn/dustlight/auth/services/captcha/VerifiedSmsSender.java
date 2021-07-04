package cn.dustlight.auth.services.captcha;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.captcha.core.Code;
import cn.dustlight.captcha.sender.CodeSender;
import cn.dustlight.captcha.sender.SendCodeException;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 短信发送器（可选验证号码是否存在）
 */
public class VerifiedSmsSender implements CodeSender<String> {

    private UserService userService;
    private CodeSender<String> smsSender;
    private String phoneParam;

    public VerifiedSmsSender(UserService userService, CodeSender<String> smsSender, String phoneParam) {
        this.userService = userService;
        this.phoneParam = phoneParam;
        this.smsSender = smsSender;

        if (!StringUtils.hasText(this.phoneParam))
            this.phoneParam = "phone";
    }

    @Override
    public void send(Code<String> code, Map<String, Object> parameters) throws SendCodeException {
        if (smsSender == null)
            throw new SendCodeException("Sms sender is null");
        if (!parameters.containsKey(phoneParam) || parameters.get(phoneParam) == null)
            throw new SendCodeException(String.format("Parameter '%s' not found!", phoneParam));
        String phone = parameters.get(phoneParam).toString();
        boolean target = checkExists(parameters);
        if (this.userService.isPhoneExists(phone) == target)
            smsSender.send(code, parameters);
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
