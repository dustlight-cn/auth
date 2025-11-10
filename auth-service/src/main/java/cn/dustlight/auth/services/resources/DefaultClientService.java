package cn.dustlight.auth.services.resources;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.*;
import cn.dustlight.auth.generator.Generator;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.mappers.AuthorityMapper;
import cn.dustlight.auth.mappers.ClientMapper;
import cn.dustlight.auth.mappers.GrantTypeMapper;
import cn.dustlight.auth.mappers.ScopeMapper;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.util.OrderBySqlBuilder;
import cn.dustlight.auth.util.QueryResults;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public class DefaultClientService implements ClientService<DefaultClient> {

    private final ClientMapper clientMapper;
    private final ScopeMapper scopeMapper;
    private final GrantTypeMapper grantTypeMapper;
    private final AuthorityMapper authorityMapper;
    private final PasswordEncoder passwordEncoder;

    private final UniqueGenerator<String> idGenerator;
    private final Generator<String> secretGenerator;

    private final OrderBySqlBuilder orderBySqlBuilder = OrderBySqlBuilder.create
            ("uid", "cid", "createdAt", "name", "updatedAt", "accessTokenValidity", "refreshTokenValidity", "status");

    public DefaultClientService(ClientMapper clientMapper, AuthorityMapper authorityMapper, ScopeMapper scopeMapper, GrantTypeMapper grantTypeMapper,
                                UniqueGenerator<String> idGenerator, Generator<String> secretGenerator,
                                PasswordEncoder passwordEncoder) {
        this.clientMapper = clientMapper;
        this.authorityMapper = authorityMapper;
        this.scopeMapper = scopeMapper;
        this.grantTypeMapper = grantTypeMapper;
        this.idGenerator = idGenerator;
        this.secretGenerator = secretGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public DefaultClient loadClientByClientId(String cid) {
        DefaultClient details = clientMapper.loadClient(cid);
        if (details == null)
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        return details;
    }

    @Override
    public DefaultClient loadClientWithoutSecret(String cid) {
        DefaultClient details = clientMapper.loadClient(cid);
        if (details == null)
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        details.setClientSecret(null);
        return details;
    }

    @Override
    public QueryResults<DefaultClient> list(Collection<String> orderBy, Integer offset, Integer limit) {
        QueryResults<DefaultClient> results = new QueryResults<>();
        results.setData(clientMapper.listClients(orderBySqlBuilder.build(orderBy), offset, limit));
        results.setCount(clientMapper.countClients());
        return results;
    }

    @Override
    public QueryResults<DefaultClient> list(Long uid, Collection<String> orderBy, Integer offset, Integer limit) {
        QueryResults<DefaultClient> results = new QueryResults<>();
        results.setData(clientMapper.listUserClients(uid, orderBySqlBuilder.build(orderBy), offset, limit));
        results.setCount(clientMapper.countUserClients(uid));
        return results;
    }

    @Override
    public QueryResults<DefaultClient> search(String keywords, Collection<String> orderBy, Integer offset, Integer limit) {
        QueryResults<DefaultClient> results = new QueryResults<>();
        results.setData(clientMapper.searchClients(keywords, orderBySqlBuilder.build(orderBy), offset, limit));
        results.setCount(clientMapper.countSearchClients(keywords));
        return results;
    }

    @Override
    public QueryResults<DefaultClient> search(String keywords, Long uid, Collection<String> orderBy, Integer offset, Integer limit) {
        QueryResults<DefaultClient> results = new QueryResults<>();
        results.setData(clientMapper.searchUserClients(uid, keywords, orderBySqlBuilder.build(orderBy), offset, limit));
        results.setCount(clientMapper.countSearchUserClients(uid, keywords));
        return results;
    }

    @Transactional
    @Override
    public DefaultClient create(Long uid, String name, String description, String redirectUri, Collection<Long> scopes, Collection<Long> grantTypes,
                                Integer accessTokenValidity, Integer refreshTokenValidity, String additionalInformation, int status) {
        String id = idGenerator.generate();
        String secret = secretGenerator.generate();
        try {
            if (!clientMapper.insertClient(id, uid, passwordEncoder.encode(secret), name, description, redirectUri,
                    accessTokenValidity, refreshTokenValidity, additionalInformation, status))
                ErrorEnum.CREATE_CLIENT_FAIL.throwException();
            
            // Auto-add default scopes if not provided
            Collection<Long> scopesToAdd = enhanceScopesWithDefaults(scopes);
            if (scopesToAdd != null && scopesToAdd.size() > 0 &&
                    !scopeMapper.insertClientScopeByScopeIds(id, scopesToAdd, false))
                ErrorEnum.CREATE_SCOPE_FAIL.details("fail to insert client scopes").throwException();
            
            // Auto-add default grant types if not provided
            Collection<Long> grantTypesToAdd = enhanceGrantTypesWithDefaults(grantTypes);
            if (grantTypesToAdd != null && grantTypesToAdd.size() > 0
                    && !grantTypeMapper.insertClientGrantTypes(id, grantTypesToAdd))
                ErrorEnum.CREATE_GRANT_TYPE_FAIL.details("fail to insert client grant types").throwException();
        } catch (DuplicateKeyException e) {
            ErrorEnum.CLIENT_EXISTS.throwException();
        }
        DefaultClient result = clientMapper.loadClient(id);
        if (result == null)
            ErrorEnum.CREATE_CLIENT_FAIL.throwException();
        result.setClientSecret(secret);
        return result;
    }

    @Transactional
    @Override
    public DefaultClient create(Long uid, String name, String description, String redirectUri, Collection<Long> scopes, Collection<Long> grantTypes) {
        String id = idGenerator.generate();
        String secret = secretGenerator.generate();
        try {
            if (!clientMapper.insertClientDefault(id, uid, passwordEncoder.encode(secret), name, description, redirectUri))
                ErrorEnum.CREATE_CLIENT_FAIL.throwException();
            
            // Auto-add default scopes if not provided
            Collection<Long> scopesToAdd = enhanceScopesWithDefaults(scopes);
            if (scopesToAdd != null && scopesToAdd.size() > 0 &&
                    !scopeMapper.insertClientScopeByScopeIds(id, scopesToAdd, false))
                ErrorEnum.CREATE_SCOPE_FAIL.details("fail to insert client scopes").throwException();
            
            // Auto-add default grant types if not provided
            Collection<Long> grantTypesToAdd = enhanceGrantTypesWithDefaults(grantTypes);
            if (grantTypesToAdd != null && grantTypesToAdd.size() > 0
                    && !grantTypeMapper.insertClientGrantTypes(id, grantTypesToAdd))
                ErrorEnum.CREATE_GRANT_TYPE_FAIL.details("fail to insert client grant types").throwException();
        } catch (DuplicateKeyException e) {
            ErrorEnum.CLIENT_EXISTS.throwException();
        }
        DefaultClient result = clientMapper.loadClient(id);
        if (result == null)
            ErrorEnum.CREATE_CLIENT_FAIL.throwException();
        result.setClientSecret(secret);
        return result;
    }

    @Override
    public User getOwner(String cid) {
        return clientMapper.getClientOwner(cid);
    }

    @Override
    public PublicUser getOwnerPublic(String cid) {
        return clientMapper.getClientOwnerPublic(cid);
    }

    @Override
    public boolean isOwner(String cid, Long uid) {
        return clientMapper.isClientOwner(cid, uid);
    }

    @Override
    public boolean isOwnerOrMember(String cid, Long uid) {
        return clientMapper.isClientOwnerOrMember(cid, uid);
    }

    @Override
    public String updateSecret(String cid) {
        String secret = secretGenerator.generate();
        if (!clientMapper.updateClientSecret(cid, passwordEncoder.encode(secret)))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client secret").throwException();
        return secret;
    }

    @Override
    public String updateSecret(String cid, Long uid) {
        if (!isOwnerOrMember(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        String secret = secretGenerator.generate();
        if (!clientMapper.updateClientSecret(cid, passwordEncoder.encode(secret)))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client secret").throwException();
        return secret;
    }

    @Override
    public void updateName(String cid, String name) {
        if (!clientMapper.updateClientName(cid, name))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client name").throwException();
    }

    @Override
    public void updateName(String cid, Long uid, String name) {
        if (!isOwnerOrMember(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        if (!clientMapper.updateClientName(cid, name))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client name").throwException();
    }

    @Override
    public void updateDescription(String cid, String description) {
        if (!clientMapper.updateClientDescription(cid, description))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client description").throwException();
    }

    @Override
    public void updateDescription(String cid, Long uid, String description) {
        if (!isOwnerOrMember(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        if (!clientMapper.updateClientDescription(cid, description))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client description").throwException();
    }

    @Override
    public void updateRedirectUri(String cid, String redirectUri) {
        if (!clientMapper.updateClientRedirectUri(cid, redirectUri))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client redirect uri").throwException();
    }

    @Override
    public void updateRedirectUri(String cid, Long uid, String redirectUri) {
        if (!isOwnerOrMember(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        if (!clientMapper.updateClientRedirectUri(cid, redirectUri))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client redirect uri").throwException();
    }

    @Override
    public void updateAccessTokenValidity(String cid, Integer accessTokenValidity) {
        if (!clientMapper.updateClientAccessTokenValidity(cid, accessTokenValidity))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client access token validity").throwException();
    }

    @Override
    public void updateRefreshTokenValidity(String cid, Integer refreshTokenValidity) {
        if (!clientMapper.updateClientRefreshTokenValidity(cid, refreshTokenValidity))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client refresh token validity").throwException();
    }

    @Override
    public void updateAdditionalInformation(String cid, String additionalInformation) {
        if (!clientMapper.updateClientAdditionalInformation(cid, additionalInformation))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client additional information").throwException();
    }

    @Override
    public void updateStatus(String cid, Integer status) {
        if (!clientMapper.updateClientStatus(cid, status))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client status").throwException();
    }

    @Override
    public void delete(Collection<String> cids) {
        if (!clientMapper.deleteClient(cids))
            ErrorEnum.DELETE_CLIENT_FAIL.throwException();
    }

    @Override
    public void delete(Long uid, Collection<String> cids) {
        if (!clientMapper.deleteUserClient(uid, cids))
            ErrorEnum.DELETE_CLIENT_FAIL.throwException();
    }

    @Override
    public Collection<? extends Scope> listScopes(String cid) {
        return scopeMapper.listClientScopes(cid);
    }

    @Override
    public Collection<? extends Scope> listScopes(String cid, Long uid) {
        if (!clientMapper.isClientOwner(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        return scopeMapper.listClientScopes(cid);
    }

    @Override
    public void addScopes(String cid, Collection<Long> sids) {
        if (!scopeMapper.insertClientScopeByScopeIds(cid, sids, false))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to insert client scopes").throwException();
    }

    @Override
    public void addScopes(String cid, Long uid, Collection<Long> sids) {
        if (!clientMapper.isClientOwnerOrMember(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        if (!scopeMapper.insertClientScopeByScopeIds(cid, sids, false))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to insert client scopes").throwException();
    }

    @Override
    public void removeScopes(String cid, Collection<Long> sids) {
        if (!scopeMapper.deleteClientScopes(cid, sids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to delete client scopes").throwException();
    }

    @Override
    public void removeScopes(String cid, Long uid, Collection<Long> sids) {
        if (!clientMapper.isClientOwnerOrMember(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        if (!scopeMapper.deleteClientScopes(cid, sids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to delete client scopes").throwException();
    }

    @Override
    public Collection<? extends GrantType> listGrantTypes(String cid) {
        return grantTypeMapper.listClientGrantTypes(cid);
    }

    @Override
    public Collection<? extends GrantType> listGrantTypes(String cid, Long uid) {
        if (!clientMapper.isClientOwnerOrMember(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        return grantTypeMapper.listClientGrantTypes(cid);
    }

    @Override
    public void addGrantTypes(String cid, Collection<Long> tids) {
        if (!grantTypeMapper.insertClientGrantTypes(cid, tids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to insert client grant types").throwException();
    }

    @Override
    public void addGrantTypes(String cid, Long uid, Collection<Long> tids) {
        if (!clientMapper.isClientOwnerOrMember(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        if (!grantTypeMapper.insertClientGrantTypes(cid, tids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to insert client grant types").throwException();
    }

    @Override
    public void removeGrantTypes(String cid, Collection<Long> tids) {
        if (!grantTypeMapper.deleteClientGrantTypes(cid, tids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to delete client grant types").throwException();
    }

    @Override
    public void removeGrantTypes(String cid, Long uid, Collection<Long> tids) {
        if (!clientMapper.isClientOwnerOrMember(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        if (!grantTypeMapper.deleteClientGrantTypes(cid, tids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to delete client grant types").throwException();
    }

    @Override
    public Collection<String> listAuthorities(String cid) {
        return authorityMapper.listClientAuthorities(cid);
    }

    @Override
    public Collection<String> listAuthorities(String cid, Long uid) {
        if (!clientMapper.isClientOwnerOrMember(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        return authorityMapper.listClientAuthorities(cid);
    }

    @Override
    public void addAuthorities(String cid, Collection<Long> aids) {
        if (!authorityMapper.insertClientAuthorities(cid, aids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to insert client authorities").throwException();
    }

    @Override
    public void addAuthorities(String cid, Long uid, Collection<Long> aids) {
        if (!clientMapper.isClientOwnerOrMember(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        if (!authorityMapper.insertClientAuthorities(cid, aids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to insert client authorities").throwException();
    }

    @Override
    public void removeAuthorities(String cid, Collection<Long> aids) {
        if (!authorityMapper.deleteClientAuthorities(cid, aids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to delete client authorities").throwException();
    }

    @Override
    public void removeAuthorities(String cid, Long uid, Collection<Long> aids) {
        if (!clientMapper.isClientOwnerOrMember(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        if (!authorityMapper.deleteClientAuthorities(cid, aids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to delete client authorities").throwException();
    }

    @Override
    public void addMembers(String cid, Collection<Long> uids) {
        if (!clientMapper.addClientMembers(cid, uids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to insert client authorities").throwException();
    }

    @Override
    public void addMembers(String cid, Long uid, Collection<Long> uids) {
        if (!clientMapper.isClientOwner(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        if (!clientMapper.addClientMembers(cid, uids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to insert client authorities").throwException();
    }

    @Override
    public void removeMembers(String cid, Collection<Long> uids) {
        if (!clientMapper.removeClientMembers(cid, uids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to delete client authorities").throwException();
    }

    @Override
    public void removeMembers(String cid, Long uid, Collection<Long> uids) {
        if (!clientMapper.isClientOwner(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        if (!clientMapper.removeClientMembers(cid, uids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to delete client authorities").throwException();
    }

    /**
     * Enhance the provided scopes with default OIDC scopes if they exist in the system
     * @param scopes User-provided scopes (can be null or empty)
     * @return Enhanced collection of scope IDs
     */
    private Collection<Long> enhanceScopesWithDefaults(Collection<Long> scopes) {
        java.util.Set<Long> result = new java.util.HashSet<>();
        
        // Add user-provided scopes first
        if (scopes != null && !scopes.isEmpty()) {
            result.addAll(scopes);
        }
        
        // Query for default OIDC scopes
        try {
            Collection<String> defaultScopeNames = java.util.Arrays.asList("openid", "profile", "email");
            Collection<cn.dustlight.auth.entities.DefaultScope> defaultScopes = scopeMapper.selectScopesByNames(defaultScopeNames);
            if (defaultScopes != null && !defaultScopes.isEmpty()) {
                for (cn.dustlight.auth.entities.DefaultScope scope : defaultScopes) {
                    result.add(scope.getSid());
                }
            }
        } catch (Exception e) {
            // If querying default scopes fails, just continue with user-provided scopes
        }
        
        return result;
    }

    /**
     * Enhance the provided grant types with default authorization_code and refresh_token if they exist in the system
     * @param grantTypes User-provided grant types (can be null or empty)
     * @return Enhanced collection of grant type IDs
     */
    private Collection<Long> enhanceGrantTypesWithDefaults(Collection<Long> grantTypes) {
        java.util.Set<Long> result = new java.util.HashSet<>();
        
        // Add user-provided grant types first
        if (grantTypes != null && !grantTypes.isEmpty()) {
            result.addAll(grantTypes);
        }
        
        // Query for default grant types
        try {
            Collection<String> defaultGrantTypeNames = java.util.Arrays.asList("authorization_code", "refresh_token");
            Collection<cn.dustlight.auth.entities.DefaultGrantType> defaultGrantTypes = grantTypeMapper.selectGrantTypesByNames(defaultGrantTypeNames);
            if (defaultGrantTypes != null && !defaultGrantTypes.isEmpty()) {
                for (cn.dustlight.auth.entities.DefaultGrantType grantType : defaultGrantTypes) {
                    result.add(grantType.getTid());
                }
            }
        } catch (Exception e) {
            // If querying default grant types fails, just continue with user-provided grant types
        }
        
        return result;
    }
}
