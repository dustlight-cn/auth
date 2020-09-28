package cn.dustlight.oauth2.uim.controllers.code;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultVerificationCodeController implements VerificationCodeController {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void createCommonCode(String code) {
        logger.debug("生成公共验证码：" + code);
    }

    @Override
    public void createRegisterCode(String targetCode, String code, String email) {
        logger.debug(String.format("图像验证码：%s\t注册验证码：%s\t邮箱：%s", targetCode, code, email));
    }

    @Override
    public void createEmailCode(String targetCode, String code, String email) {
        logger.debug(String.format("图像验证码：%s\t更新邮箱验证码：%s\t邮箱：%s", targetCode, code, email));
    }

}
