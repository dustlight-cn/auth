package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulConstants;
import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.models.AppDetails;
import cn.dustlight.uim.models.IAppDetails;
import cn.dustlight.uim.models.IUserDetails;
import cn.dustlight.uim.services.ClientMapper;
import cn.dustlight.uim.services.IVerificationCodeGenerator;
import cn.dustlight.uim.utils.Snowflake;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@RestController
public class ClientController implements IClientController {

    @Autowired
    ClientMapper mapper;

    @Autowired
    Snowflake snowflake;

    @Autowired
    IVerificationCodeGenerator verificationCodeGenerator;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthorizationEndpoint authorizationEndpoint;

    @Override
    public RestfulResult<IAppDetails> createApp(String appName, String scope, String redirectUri, Authentication authentication) {
        IUserDetails userDetails = (IUserDetails) authentication.getPrincipal();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Long id = snowflake.getNextId();
        try (ObjectOutputStream os = new ObjectOutputStream(out)) {
            os.writeLong(id);
            os.writeByte(0);
        } catch (IOException e) {
            return RestfulResult.error(e.getMessage());
        }
        String appKey = Base64.getEncoder().encodeToString(out.toByteArray());
        String appSecret = sha1(authentication.getName() + id + verificationCodeGenerator.generatorCode(128));
        if (!mapper.insertClient(appKey,
                userDetails.getUid(),
                appName,
                "",
                passwordEncoder.encode(appSecret),
                scope,
                "authorization_code,refresh_token,password,implicit",
                redirectUri,
                "",
                null,
                "1")) {
            return RestfulConstants.ERROR_UNKNOWN;
        }
        AppDetails appDetails = new AppDetails();
        appDetails.setUid(userDetails.getUid());
        appDetails.setAppKey(appKey);
        appDetails.setAppName(appName);
        appDetails.setAppSecret(appSecret);
        appDetails.setRedirectUri(redirectUri);
        appDetails.setScope(scope);
        return RestfulResult.success(appDetails);
    }

    @Override
    public RestfulResult<IAppDetails> getApp(String appKey, Authentication authentication) {
        return RestfulResult.success(checkRole(appKey, authentication));
    }

    @Override
    public RestfulResult deleteApp(String appKey, Authentication authentication) {
        checkRole(appKey, authentication);
        if (mapper.deleteClient(appKey))
            return RestfulConstants.SUCCESS;
        return RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult<String> resetAppSecret(String appKey, Authentication authentication) {
        AppDetails client = checkRole(appKey, authentication);
        client.setAppSecret(sha1(authentication.getName() +
                client.getUid() +
                verificationCodeGenerator.generatorCode(128)));
        boolean flag = mapper.updateSecret(appKey, passwordEncoder.encode(client.getAppSecret()));
        return flag ? RestfulResult.success(client.getAppSecret()) : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult updateAppName(String appKey, String appName, Authentication authentication) {
        checkRole(appKey, authentication);
        boolean flag = mapper.updateName(appKey, appName.trim());
        return flag ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult updateAppScope(String appKey, String scope, Authentication authentication) {
        checkRole(appKey, authentication);
        boolean flag = mapper.updateScope(appKey, scope.trim());
        return flag ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult updateAppRedirectUri(String appKey, String redirectUri, Authentication authentication) {
        checkRole(appKey, authentication);
        boolean flag = mapper.updateRedirectUri(appKey, redirectUri.trim());
        return flag ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult<List<AppDetails>> getAllApps() {
        return RestfulResult.success(mapper.getApps());
    }

    @Override
    public RestfulResult<List<AppDetails>> getCurrentUserApps(Principal principal) {
        if (principal instanceof IUserDetails)
            return RestfulResult.success(mapper.getAppsByUid(((IUserDetails) principal).getUid()));
        else
            return RestfulResult.success(mapper.getAppsByUsername(principal.getName()));
    }

    @Override
    public RestfulResult<List<AppDetails>> getUserApps(String username) {
        return RestfulResult.success(mapper.getAppsByUsername(username));
    }

    protected String sha1(String str) {
        return DigestUtils.sha1Hex(str);
    }

    protected AppDetails checkRole(String appKey, Authentication authentication) throws AccessDeniedException {
        IUserDetails userDetails = (IUserDetails) authentication.getPrincipal();
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        AppDetails client = mapper.getClient(appKey);
        if (client == null)
            throw new NullPointerException("Client not found");
        if (!(roles.contains("ROLE_ROOT") || roles.contains("ROLE_ADMIN")) &&
                client.getUid() != userDetails.getUid())
            throw new AccessDeniedException("Access Denied");
        return client;
    }
}
