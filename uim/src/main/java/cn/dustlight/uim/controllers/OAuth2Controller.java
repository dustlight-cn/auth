package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OAuth2Controller implements IOAuth2Controller {

    @Override
    public RestfulResult confirmAccess(Map<String, Object> model, HttpServletResponse response, HttpServletRequest request) throws IOException {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        Map<String, Object> data = new HashMap<>();
        data.put("clientId", authorizationRequest.getClientId());
        data.put("scopes", authorizationRequest.getScope());
        return RestfulResult.success(data);
    }

}
