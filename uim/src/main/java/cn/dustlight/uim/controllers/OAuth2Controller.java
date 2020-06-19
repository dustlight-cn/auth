package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * 覆盖 '/oauth/authorize' ，返回Json数据。
 */
@Controller
@SessionAttributes({"authorizationRequest", "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST"})
public class OAuth2Controller {

    @Autowired
    AuthorizationEndpoint endpoint;

    @Autowired
    AuthorizationServerEndpointsConfiguration authorizationServerEndpointsConfiguration;

    @RequestMapping({"/oauth/authorize"})
    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters, SessionStatus sessionStatus, Principal principal) {
        Map<String, Object> data = new HashMap<>();
        ModelAndView mv = endpoint.authorize(model, parameters, sessionStatus, principal);
        if (mv.getView() instanceof RedirectView) {
            RedirectView redirectView = (RedirectView) mv.getView();
            data.put("redirect_uri", redirectView.getUrl());
        } else {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
            data.put("clientId", authorizationRequest.getClientId());
            data.put("scopes", authorizationRequest.getScope());
            data.put("all", authorizationRequest);
        }
        return RestfulResult.success(data).toModelAndView();
    }

    @RequestMapping(
            value = {"/oauth/authorize"},
            method = {RequestMethod.POST},
            params = {"user_oauth_approval"}
    )
    public ModelAndView approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model, SessionStatus sessionStatus, Principal principal) {
        RedirectView view = (RedirectView) endpoint.approveOrDeny(approvalParameters, model, sessionStatus, principal);
        return RestfulResult.success(view.getUrl()).toModelAndView();
    }
}
