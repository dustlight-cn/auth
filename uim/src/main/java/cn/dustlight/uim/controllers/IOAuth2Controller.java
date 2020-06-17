package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@SessionAttributes("authorizationRequest")
public interface IOAuth2Controller {

    @GetMapping("/oauth/confirm_access")
    RestfulResult confirmAccess(Map<String, Object> model, HttpServletResponse response, HttpServletRequest request) throws IOException;

}
