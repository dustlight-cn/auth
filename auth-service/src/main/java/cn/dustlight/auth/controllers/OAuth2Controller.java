package cn.dustlight.auth.controllers;

import cn.dustlight.auth.ErrorEnum;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

/**
 * 覆盖 '/oauth/authorize' ，返回Json数据。
 */
@RestController
@RequestMapping(path = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@Tag(name = "OAuth2 相关业务", description = "负责OAuth2 应用授权、Token颁发等。")
@SessionAttributes({"authorizationRequest", "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST"})
public class OAuth2Controller {

    private static final Log logger = LogFactory.getLog(OAuth2Controller.class.getName());

    private static final BasicAuthenticationConverter basicConverter = new BasicAuthenticationConverter();

    @Autowired
    private AuthorizationEndpoint authorizationEndpoint;

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ApprovalStore authApprovalStore;

    @Autowired
    private TokenStore authTokenStore;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Operation(summary = "颁发令牌", security = @SecurityRequirement(name = "Client Credentials"))
    @PostMapping("oauth/token")
    public ResponseEntity<OAuth2AccessToken> grantToken(@RequestParam(value = "code", required = false) String code,
                                                        @RequestParam(value = "grant_type", defaultValue = "authorization_code") String grantType,
                                                        @RequestParam(value = "redirect_uri", required = false) String redirectUri,
                                                        @RequestParam(value = "username", required = false) String username,
                                                        @RequestParam(value = "password", required = false) String password,
                                                        @RequestParam @Parameter(hidden = true) Map<String, String> parameters,
                                                        HttpServletRequest request) throws HttpRequestMethodNotSupportedException {
        UsernamePasswordAuthenticationToken clientPrincipal = basicConverter.convert(request);
        if (clientPrincipal == null)
            ErrorEnum.UNAUTHORIZED.throwException();
        String clientName = clientPrincipal.getName();
        String clientSecret = clientPrincipal.getCredentials() == null ? null : clientPrincipal.getCredentials().toString();

        Client client = clientService.loadClientByClientId(clientName);
        if (client == null)
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        if ((clientSecret == null || client.getClientSecret() == null) && clientSecret != client.getClientSecret() ||
                !passwordEncoder.matches(clientSecret, client.getClientSecret()))
            ErrorEnum.OAUTH_ERROR.details("client secret incorrect").throwException();

        OAuth2Request oAuth2Request = new OAuth2Request(parameters, clientName, null, true, null, null, null, null, null);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, null);

        return tokenEndpoint.postAccessToken(oAuth2Authentication, parameters);
    }
}
