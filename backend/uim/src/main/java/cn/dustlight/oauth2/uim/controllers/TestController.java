package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.Constants;
import cn.dustlight.validator.annotation.SendCode;
import cn.dustlight.validator.annotation.VerifyCode;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@RestController
@RequestMapping(Constants.V1.API_ROOT + "test/")
public class TestController {

    private static final Logger logger = Logger.getLogger("Test");

    @GetMapping("code")
    @SendCode(parameter = "code")
    @Operation
    public void email(@Parameter(hidden = true) String code) {
        logger.info(code );
    }


}
