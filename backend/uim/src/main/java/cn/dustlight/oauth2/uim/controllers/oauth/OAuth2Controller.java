package cn.dustlight.oauth2.uim.controllers.oauth;

import cn.dustlight.oauth2.uim.entities.v1.clients.UimClient;
import cn.dustlight.oauth2.uim.entities.v1.scopes.ClientScope;
import cn.dustlight.oauth2.uim.services.clients.ClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.*;

/**
 * 覆盖 '/oauth/authorize' ，返回Json数据。
 */
@RestController
@Tag(name = "OAuth2 相关业务", description = "OAuth2授权、Token发放。")
@SessionAttributes({"authorizationRequest", "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST"})
public class OAuth2Controller {

    @Autowired
    private AuthorizationEndpoint endpoint;

    @Autowired
    private ClientService clientDetailsService;

    @Autowired
    private ApprovalStore approvalStore;

    @Autowired
    private TokenStore uimTokenStore;

    @RequestMapping(
            value = {"/oauth/authorize"},
            method = RequestMethod.GET,
            produces = "application/json;charset=utf-8"
    )
    public Map authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters, SessionStatus sessionStatus, Principal principal) {
        Map<String, Object> data = new HashMap<>();
        ModelAndView mv = endpoint.authorize(model, parameters, sessionStatus, principal);

        String clientId = parameters.get("client_id");
        UimClient client = (UimClient) clientDetailsService.loadClientByClientId(clientId);
        String username = principal.getName();

        data.put("clientName", client.getClientName());
        data.put("description", client.getClientDescription());
        data.put("clientId", client.getClientId());
        data.put("createdAt", client.getCreatedAt());
        data.put("updatedAt", client.getUpdatedAt());
        if (mv.getView() instanceof RedirectView) {
            RedirectView redirectView = (RedirectView) mv.getView();
            data.put("redirect_uri", redirectView.getUrl());
            data.put("isApproved", true);
        } else {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
            Set<String> requestScopes = authorizationRequest.getScope();
            Collection<Approval> approvals = approvalStore.getApprovals(username, clientId);
            Collection<ClientScope> scopeDes = client.getScopes();
            Map<String, ClientScope> scopeMap = new LinkedHashMap<>();
            for (ClientScope clientScope : scopeDes)
                scopeMap.put(clientScope.getName(), clientScope);
            Map<String, Map<String, Object>> scopes = new LinkedHashMap<>();
            for (String s : requestScopes) {
                Map<String, Object> m = new LinkedHashMap<>();
                scopes.put(s, m);
                m.put("details", scopeMap.get(s));
            }
            for (Approval approval : approvals) {
                if (approval.isApproved() && scopes.containsKey(approval.getScope()))
                    scopes.get(approval.getScope()).put("approved", true);
            }
            Collection<OAuth2AccessToken> tokens = uimTokenStore.findTokensByClientId(client.getClientId());
            if (tokens != null)
                data.put("userNumber", tokens.size());
            if (client.getRegisteredRedirectUri() != null && !client.getRegisteredRedirectUri().isEmpty()) {
                String[] nicknameArr = client.getRegisteredRedirectUri().toArray(new String[0]);
                if (nicknameArr != null)
                    data.put("nickname", nicknameArr[0]);
            }
            data.put("username", client.getClientSecret()); // 并不是真的ClientSecret，只是Mapper查询时用username存放于次
            data.put("scopes", scopes);
            data.put("isApproved", false);
        }
        return data;
    }

    @RequestMapping(
            value = {"/oauth/authorize"},
            method = {RequestMethod.POST},
            params = {"user_oauth_approval"},
            produces = "application/json;charset=utf-8"
    )
    public String approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model, SessionStatus sessionStatus, Principal principal) {
        RedirectView view = (RedirectView) endpoint.approveOrDeny(approvalParameters, model, sessionStatus, principal);
        return view.getUrl();
    }
}
