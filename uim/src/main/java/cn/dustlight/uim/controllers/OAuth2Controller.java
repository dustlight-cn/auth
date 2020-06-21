package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.models.ClientDetails;
import cn.dustlight.uim.services.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.*;

/**
 * 覆盖 '/oauth/authorize' ，返回Json数据。
 */
@Controller
@SessionAttributes({"authorizationRequest", "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST"})
public class OAuth2Controller {

    @Autowired
    private AuthorizationEndpoint endpoint;

    @Autowired
    private ClientMapper mapper;

    @Autowired
    private ApprovalStore approvalStore;

    @RequestMapping(value = {"/oauth/authorize"}, method = RequestMethod.GET)
    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters, SessionStatus sessionStatus, Principal principal) {
        Map<String, Object> data = new HashMap<>();
        ModelAndView mv = endpoint.authorize(model, parameters, sessionStatus, principal);
        if (mv.getView() instanceof RedirectView) {
            RedirectView redirectView = (RedirectView) mv.getView();
            data.put("redirect_uri", redirectView.getUrl());
        } else {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
            String clientId = authorizationRequest.getClientId();
            ClientDetails details = mapper.loadClientDescription(clientId);
            String username = principal.getName();

            Set<String> requestScopes = authorizationRequest.getScope();

            Collection<Approval> approvals = approvalStore.getApprovals(username, clientId);


            Map<String, String> scopeDes = details.getScopeDescriptions();

            Map<String, Map<String, Object>> scopes = new LinkedHashMap<>();
            for (String s : requestScopes) {
                Map<String, Object> m = new LinkedHashMap<>();
                scopes.put(s, m);
                m.put("description", scopeDes.get(s));
            }
            for (Approval approval : approvals) {
                if (approval.isApproved() && scopes.containsKey(approval.getScope()))
                    scopes.get(approval.getScope()).put("approved", true);
            }
            data.put("clientName", details.getClientName());
            data.put("description", details.getDescription());
            data.put("scopes", scopes);
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