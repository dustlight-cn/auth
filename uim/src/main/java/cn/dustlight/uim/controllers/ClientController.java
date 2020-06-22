package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.models.*;
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
import java.util.*;
import java.util.logging.Logger;

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

    public RestfulResult createApp(String appName, Set<String> scope, Set<String> redirectUri, Authentication authentication) {
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
//        if (!mapper.insertClient(appKey,
//                userDetails.getUid(),
//                appName,
//                "",
//                passwordEncoder.encode(appSecret),
//                scope,
//                "authorization_code,refresh_token,password,implicit",
//                redirectUri,
//                "",
//                null,
//                "1")) {
//            return RestfulConstants.ERROR_UNKNOWN;
//        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("appKey", appKey);
        data.put("appSecret", appSecret);
        return RestfulResult.success(data);
    }

    protected String sha1(String str) {
        return DigestUtils.sha1Hex(str);
    }

    protected ClientDetails checkRole(String appKey, Authentication authentication) throws AccessDeniedException {
        IUserDetails userDetails = (IUserDetails) authentication.getPrincipal();
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        ClientDetails client = mapper.loadClientByClientId(appKey);
        if (client == null)
            throw new NullPointerException("Client not found");
        if (!(roles.contains("ROLE_ROOT") || roles.contains("ROLE_ADMIN")) &&
                client.getUid() != userDetails.getUid())
            throw new AccessDeniedException("Access Denied");
        return client;
    }

    @Override
    public RestfulResult<List<ScopeDetails>> getScopeDetails() {
        return RestfulResult.success(mapper.getScopes());
    }

    @Override
    public RestfulResult<List<AuthorityDetails>> getAuthorityDetails() {
        return RestfulResult.success(mapper.getAuthorities());
    }

    @Override
    public RestfulResult<List<ClientDetails>> getCurrentUserClientDetails(Authentication authentication) {

        Logger.getLogger(getClass().getName()).info(authentication.getPrincipal().toString()
        );
        IUserDetails userDetails = (IUserDetails) authentication.getPrincipal();
        Logger.getLogger(getClass().getName()).info(userDetails.getUid() + "!!");
        return RestfulResult.success(mapper.loadClientsByUserId(userDetails.getUid()));
    }

    @Override
    public RestfulResult<List<ClientDetails>> getUserClientDetails(Long userId) {
        return RestfulResult.success(mapper.loadClientsByUserId(userId));
    }

    @Override
    public RestfulResult<List<AuthorityDetails>> getRoleAuthorities(Long roleId) {
        return RestfulResult.success(mapper.getRoleAuthorities(roleId));
    }

    @Override
    public RestfulResult<List<AuthorityDetails>> getScopeAuthorities(Long scopeId) {
        return RestfulResult.success(mapper.getScopeAuthorities(scopeId));
    }
}
