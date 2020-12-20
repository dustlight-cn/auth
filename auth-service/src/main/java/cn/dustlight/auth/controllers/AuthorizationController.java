package cn.dustlight.auth.controllers;

import cn.dustlight.auth.entities.Client;
import cn.dustlight.auth.entities.ClientScope;
import cn.dustlight.auth.entities.OAuth2Client;
import cn.dustlight.auth.entities.OAuth2ClientScope;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.util.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Tag(name = "OAuth", description = "负责 OAuth2 应用授权。")
@RestController
@RequestMapping(path = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@SecurityRequirement(name = "Access Token")
@SessionAttributes({"authorizationRequest", "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST"})
public class AuthorizationController {

    private static final Log logger = LogFactory.getLog(AuthorizationController.class.getName());

    @Autowired
    private AuthorizationEndpoint authorizationEndpoint;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ApprovalStore authApprovalStore;

    @Autowired
    private TokenStore authTokenStore;

    @Operation(summary = "预授权", description = "获取应用信息")
    @PostMapping("oauth/authorization")
    public OAuth2Client authorize(@Parameter(hidden = true) @RequestParam Map<String, String> parameters,
                                  @RequestParam("client_id") String clientId,
                                  @RequestParam(value = "response_type", defaultValue = "code") String responseType,
                                  @RequestParam(value = "redirect_uri", required = false) String redirectUri,
                                  @RequestParam(value = "scope", required = false) Collection<String> scopes,
                                  @RequestParam(value = "state", required = false) String state,
                                  Map<String, Object> model,
                                  SessionStatus sessionStatus,
                                  Principal principal) {

        ModelAndView mv = authorizationEndpoint.authorize(model, parameters, sessionStatus, principal);
        Client client = clientService.loadClientWithoutSecret(clientId);
        OAuth2Client result = new OAuth2Client();
        String username = principal.getName();

        result.setName(client.getName());
        result.setClientId(client.getClientId());
        result.setDescription(client.getDescription());
        result.setCreatedAt(client.getCreatedAt());
        result.setUpdatedAt(client.getUpdatedAt());
        result.setStatus(client.getStatus());
        result.setLogo(client.getLogo());
        result.setUser(clientService.getOwnerPublic(clientId));

        if (mv.getView() instanceof RedirectView) {
            RedirectView redirectView = (RedirectView) mv.getView();
            result.setApproved(true);
            result.setRedirect(redirectView.getUrl());
        } else {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");

            Set<String> approvedScopes = new LinkedHashSet<>(); // 已授权的Scopes
            Set<String> requestScopes = authorizationRequest.getScope(); // 请求授权的Scope
            Set<OAuth2ClientScope> resultScopes = new LinkedHashSet<>();

            /* 获取已授权的Scopes */
            Collection<Approval> approvals = authApprovalStore.getApprovals(username, clientId);
            for (Approval approval : approvals)
                if (approval.isCurrentlyActive())
                    approvedScopes.add(approval.getScope());

            for (ClientScope clientScope : client.getScopes()) {
                if (clientScope == null)
                    continue;
                if (requestScopes.contains(clientScope.getName())) {
                    OAuth2ClientScope oAuth2ClientScope = OAuth2ClientScope.from(clientScope);
                    if (approvedScopes.contains(clientScope.getName()))
                        oAuth2ClientScope.setApproved(true);
                    resultScopes.add(oAuth2ClientScope);
                }
            }

            result.setScopes(resultScopes);
            result.setApproved(false);
        }
        return result;
    }

    @Operation(summary = "授权")
    @DeleteMapping("oauth/authorization")
    public String approveOrDeny(@RequestParam("approved") boolean approved,
                                @RequestParam("scope") Set<String> scopes,
                                Map<String, Object> model,
                                SessionStatus sessionStatus,
                                Principal principal) {
        Map<String, String> approvalParameters = new LinkedHashMap<>();
        approvalParameters.put("user_oauth_approval", approved ? "true" : "false");
        for (String scope : scopes)
            if (scope.startsWith("scope."))
                approvalParameters.put(scope, "true");
            else
                approvalParameters.put("scope." + scope, "true");

        RedirectView view = (RedirectView) authorizationEndpoint.approveOrDeny(approvalParameters, model, sessionStatus, principal);
        return view.getUrl();
    }
}
