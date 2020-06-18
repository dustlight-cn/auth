package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class OAuth2Controller implements IOAuth2Controller {

    @Autowired
    AuthorizationEndpoint endpoint;

    @Autowired
    AuthorizationServerEndpointsConfiguration authorizationServerEndpointsConfiguration;

    AuthorizationCodeServices authorizationCodeServices = new InMemoryAuthorizationCodeServices();

    @Override
    public RestfulResult confirmAccess(Map<String, Object> model) {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        Map<String, Object> data = new HashMap<>();
        data.put("clientId", authorizationRequest.getClientId());
        data.put("scopes", authorizationRequest.getScope());
        return RestfulResult.success(data);
    }

    @RequestMapping({"/oauth/authorize"})
    public RestfulResult authorize(Map<String, Object> model, Map<String, String> parameters, SessionStatus sessionStatus, Principal principal) {
        OAuth2RequestFactory factroy = authorizationServerEndpointsConfiguration.getEndpointsConfigurer().getOAuth2RequestFactory();
        AuthorizationRequest authorizationRequest = factroy.createAuthorizationRequest(parameters);
        if (authorizationRequest.isApproved()) {

            Set<String> responseTypes = authorizationRequest.getResponseTypes();
//            if (responseTypes.contains("token")) {
//                return this.getImplicitGrantResponse(authorizationRequest);
//            }

            if (responseTypes.contains("code")) {

                OAuth2Request storedOAuth2Request = factroy.createOAuth2Request(authorizationRequest);
                OAuth2Authentication combinedAuth = new OAuth2Authentication(storedOAuth2Request, (Authentication) principal);
                String code = this.authorizationCodeServices.createAuthorizationCode(combinedAuth);
                return RestfulResult.success(code);
            }
            //return new ModelAndView(this.getAuthorizationCodeResponse(authorizationRequest, (Authentication) principal));
        }
        endpoint.authorize(model, parameters, sessionStatus, principal);
        return confirmAccess(model);
    }
}
