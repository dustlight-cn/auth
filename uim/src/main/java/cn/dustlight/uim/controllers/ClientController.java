package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulConstants;
import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.models.*;
import cn.dustlight.uim.services.*;
import cn.dustlight.uim.utils.Snowflake;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

@RestController
public class ClientController implements IClientController {

    @Autowired
    private ClientDetailsMapper clientMapper;

    @Autowired
    private ScopeDetailsMapper scopeDetailsMapper;

    @Autowired
    private AuthorityDetailsMapper authorityDetailsMapper;

    @Autowired
    private RoleDetailsMapper roleDetailsMapper;

    @Autowired
    private GrantTypeMapper grantTypeMapper;

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private IVerificationCodeGenerator verificationCodeGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorizationEndpoint authorizationEndpoint;

    private final static Base32 base32 = new Base32();

    protected String sha1(String str) {
        return DigestUtils.sha1Hex(str);
    }

    @Transactional
    @Override
    public RestfulResult createClient(String name, String redirectUri, String description, List<Long> scopes, List<Long> grantTypes, Authentication authentication) {
        IUserDetails userDetails = (IUserDetails) authentication.getPrincipal();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Long id = snowflake.getNextId();
        try (ObjectOutputStream os = new ObjectOutputStream(out)) {
            os.writeLong(id);
            os.writeByte(0);
        } catch (IOException e) {
            return RestfulResult.error(e.getMessage());
        }
        String appKey = base32.encodeToString(out.toByteArray());//Base64.getEncoder().encodeToString(out.toByteArray());
        String appSecret = sha1(authentication.getName() + id + verificationCodeGenerator.generatorCode(128));
        boolean flag = clientMapper.insertClient(appKey, userDetails.getUid(), passwordEncoder.encode(appSecret), name, redirectUri,
                null, null, null, true, description);
        flag = flag & clientMapper.insertClientScopes(appKey, scopes);
        flag = flag & clientMapper.insertClientGrantTypes(appKey, grantTypes);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("appKey", appKey);
        data.put("appSecret", appSecret);
        return flag ? RestfulResult.success(data) : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult deleteClient(String appKey, Authentication authentication) {
        boolean flag = AuthorityUtils.authorityListToSet(authentication.getAuthorities()).contains("DELETE_CLIENT_ANY") ?
                clientMapper.deleteClient(appKey) :
                clientMapper.deleteClientWithUid(appKey, ((IUserDetails) authentication.getPrincipal()).getUid());
        return flag ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult<String> resetClientSecret(String appKey, Authentication authentication) {
        Long id = snowflake.getNextId();
        String appSecret = sha1(authentication.getName() + id + verificationCodeGenerator.generatorCode(128));
        boolean flag = AuthorityUtils.authorityListToSet(authentication.getAuthorities()).contains("DELETE_CLIENT_ANY") ?
                clientMapper.updateClientSecret(appKey, passwordEncoder.encode(appSecret)) :
                clientMapper.updateClientSecretWithUid(appKey, passwordEncoder.encode(appSecret), ((IUserDetails) authentication.getPrincipal()).getUid());
        return flag ? RestfulResult.success(appSecret) : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult updateClientName(String appKey, String name, Authentication authentication) {
        boolean flag = AuthorityUtils.authorityListToSet(authentication.getAuthorities()).contains("UPDATE_CLIENT_ANY") ?
                clientMapper.updateClientName(appKey, name) :
                clientMapper.updateClientNameWithUid(appKey, name, ((IUserDetails) authentication.getPrincipal()).getUid());
        return flag ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult updateClientDescription(String appKey, String description, Authentication authentication) {
        boolean flag = AuthorityUtils.authorityListToSet(authentication.getAuthorities()).contains("UPDATE_CLIENT_ANY") ?
                clientMapper.updateClientDescription(appKey, description) :
                clientMapper.updateClientDescriptionWithUid(appKey, description, ((IUserDetails) authentication.getPrincipal()).getUid());
        return flag ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult updateClientRedirectUri(String appKey, String redirectUri, Authentication authentication) {
        boolean flag = AuthorityUtils.authorityListToSet(authentication.getAuthorities()).contains("UPDATE_CLIENT_ANY") ?
                clientMapper.updateClientRedirectUri(appKey, redirectUri) :
                clientMapper.updateClientRedirectUriWithUid(appKey, redirectUri, ((IUserDetails) authentication.getPrincipal()).getUid());
        return flag ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult insertRole(String name, String description) {
        return roleDetailsMapper.insertRoleDetails(snowflake.getNextId(), name, description) ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult updateRole(Long id, String name, String description) {
        return roleDetailsMapper.updateRoleDetails(id, name, description) ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult deleteRole(Long id) {
        return roleDetailsMapper.deleteRoleDetails(id) ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult removeRoleAuthority(Long roleId, Long authorityId) {
        return authorityDetailsMapper.removeRoleAuthority(roleId, authorityId) ?
                RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult insertAuthority(Long roleId, Long authorityId) {
        return authorityDetailsMapper.insertRoleAuthority(roleId, authorityId) ?
                RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult<List<RoleDetails>> getRoles() {
        return RestfulResult.success(roleDetailsMapper.getAllRoleDetails());
    }

    @Override
    public RestfulResult updateAuthority(Long id, String name, String description) {
        return authorityDetailsMapper.updateAuthority(id, name, description) ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult deleteAuthority(Long id) {
        return authorityDetailsMapper.deleteAuthority(id) ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult insertAuthority(String name, String description) {
        return authorityDetailsMapper.insertAuthority(snowflake.getNextId(), name, description) ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult<List<ScopeDetails>> getScopeDetails() {
        return RestfulResult.success(scopeDetailsMapper.getScopes());
    }

    @Override
    public RestfulResult<List<AuthorityDetails>> getAuthorityDetails() {
        return RestfulResult.success(authorityDetailsMapper.getAuthorities());
    }

    @Override
    public RestfulResult<List<GrantType>> getGrantTypes() {
        return RestfulResult.success(grantTypeMapper.getGrantTypes());
    }

    @Override
    public RestfulResult<List<ClientDetails>> getCurrentUserClientDetails(Authentication authentication) {
        IUserDetails userDetails = (IUserDetails) authentication.getPrincipal();
        return RestfulResult.success(clientMapper.loadClientsByUserId(userDetails.getUid()));
    }

    @Override
    public RestfulResult<List<ClientDetails>> getUserClientDetails(Long userId) {
        return RestfulResult.success(clientMapper.loadClientsByUserId(userId));
    }

    @Override
    public RestfulResult<List<AuthorityDetails>> getRoleAuthorities(Long roleId) {
        return RestfulResult.success(authorityDetailsMapper.getRoleAuthorities(roleId));
    }

    @Override
    public RestfulResult<List<AuthorityDetails>> getScopeAuthorities(Long scopeId) {
        return RestfulResult.success(authorityDetailsMapper.getScopeAuthorities(scopeId));
    }
}
