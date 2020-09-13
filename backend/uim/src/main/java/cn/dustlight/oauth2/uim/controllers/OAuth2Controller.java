package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.entities.IClientDetails;
import cn.dustlight.oauth2.uim.services.ClientDetailsMapper;
import cn.dustlight.oauth2.uim.RestfulResult;
import cn.dustlight.oauth2.uim.entities.IScopeDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
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
    private ClientDetailsMapper mapper;

    @Autowired
    private ApprovalStore approvalStore;

    @Autowired
    private TokenStore redisTokenStore;

    @RequestMapping(
            value = {"/oauth/authorize"},
            method = RequestMethod.GET,
            produces = "application/json;charset=utf-8"
    )
    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters, SessionStatus sessionStatus, Principal principal) {
        Map<String, Object> data = new HashMap<>();
        ModelAndView mv = endpoint.authorize(model, parameters, sessionStatus, principal);

        String clientId = parameters.get("client_id");
        IClientDetails details = mapper.loadClientDescription(clientId);
        String username = principal.getName();

        data.put("clientName", details.getClientName());
        data.put("description", details.getDescription());
        data.put("clientId", details.getClientId());
        data.put("createdAt", details.getCreatedAt());
        data.put("updatedAt", details.getUpdatedAt());
        if (mv.getView() instanceof RedirectView) {
            RedirectView redirectView = (RedirectView) mv.getView();
            data.put("redirect_uri", redirectView.getUrl());
            data.put("isApproved", true);
        } else {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
            Set<String> requestScopes = authorizationRequest.getScope();
            Collection<Approval> approvals = approvalStore.getApprovals(username, clientId);
            Map<String, IScopeDetails> scopeDes = details.getScopeDetails();

            Map<String, Map<String, Object>> scopes = new LinkedHashMap<>();
            for (String s : requestScopes) {
                Map<String, Object> m = new LinkedHashMap<>();
                scopes.put(s, m);
                m.put("details", scopeDes.get(s));
            }
            for (Approval approval : approvals) {
                if (approval.isApproved() && scopes.containsKey(approval.getScope()))
                    scopes.get(approval.getScope()).put("approved", true);
            }
            Collection<OAuth2AccessToken> tokens = redisTokenStore.findTokensByClientId(details.getClientId());
            if (tokens != null)
                data.put("userNumber", tokens.size());
            if (details.getRegisteredRedirectUri() != null && !details.getRegisteredRedirectUri().isEmpty()) {
                String[] nicknameArr = details.getRegisteredRedirectUri().toArray(new String[0]);
                if (nicknameArr != null)
                    data.put("nickname", nicknameArr[0]);
            }
            data.put("username", details.getClientSecret()); // 并不是真的ClientSecret，只是Mapper查询时用username存放于次
            data.put("scopes", scopes);
            data.put("isApproved", false);
        }
        return RestfulResult.success(data).toModelAndView();
    }

    @RequestMapping(
            value = {"/oauth/authorize"},
            method = {RequestMethod.POST},
            params = {"user_oauth_approval"},
            produces = "application/json;charset=utf-8"
    )
    public ModelAndView approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model, SessionStatus sessionStatus, Principal principal) {
        RedirectView view = (RedirectView) endpoint.approveOrDeny(approvalParameters, model, sessionStatus, principal);
        return RestfulResult.success(view.getUrl()).toModelAndView();
    }
}
