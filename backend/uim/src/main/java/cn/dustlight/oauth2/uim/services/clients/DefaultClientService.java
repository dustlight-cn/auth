package cn.dustlight.oauth2.uim.services.clients;

import cn.dustlight.generator.Generator;
import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.oauth2.uim.entities.results.IntQueryResults;
import cn.dustlight.oauth2.uim.entities.v1.clients.Client;
import cn.dustlight.oauth2.uim.entities.v1.clients.DefaultClient;
import cn.dustlight.oauth2.uim.entities.v1.scopes.Scope;
import cn.dustlight.oauth2.uim.entities.v1.types.GrantType;
import cn.dustlight.oauth2.uim.entities.v1.users.User;
import cn.dustlight.oauth2.uim.mappers.ClientMapper;
import cn.dustlight.oauth2.uim.mappers.GrantTypeMapper;
import cn.dustlight.oauth2.uim.mappers.ScopeMapper;
import cn.dustlight.oauth2.uim.utils.OrderBySqlBuilder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public class DefaultClientService implements ClientService {

    private ClientMapper clientMapper;
    private ScopeMapper scopeMapper;
    private GrantTypeMapper grantTypeMapper;

    private UniqueGenerator<String> idGenerator;
    private Generator<String> secretGenerator;

    private OrderBySqlBuilder orderBySqlBuilder = OrderBySqlBuilder.create
            ("uid", "cid", "createdAt", "name", "updatedAt", "accessTokenValidity", "refreshTokenValidity", "status");

    public DefaultClientService(ClientMapper clientMapper, ScopeMapper scopeMapper, GrantTypeMapper grantTypeMapper,
                                UniqueGenerator<String> idGenerator, Generator<String> secretGenerator) {
        this.clientMapper = clientMapper;
        this.scopeMapper = scopeMapper;
        this.grantTypeMapper = grantTypeMapper;
        this.idGenerator = idGenerator;
        this.secretGenerator = secretGenerator;
    }

    @Override
    public Client loadClientByClientId(String s) throws ClientRegistrationException {
        Client details = clientMapper.loadClient(s);
        if (details == null)
            throw new NoSuchClientException("client not found");
        return details;
    }

    @Override
    public IntQueryResults<? extends Client> list(Collection<String> orderBy, Integer offset, Integer limit) {
        IntQueryResults<DefaultClient> results = new IntQueryResults<>();
        results.setData(clientMapper.listClients(orderBySqlBuilder.build(orderBy), offset, limit));
        results.setCount(clientMapper.countClients());
        return results;
    }

    @Override
    public IntQueryResults<? extends Client> list(Long uid, Collection<String> orderBy, Integer offset, Integer limit) {
        IntQueryResults<DefaultClient> results = new IntQueryResults<>();
        results.setData(clientMapper.listUserClients(uid, orderBySqlBuilder.build(orderBy), offset, limit));
        results.setCount(clientMapper.countUserClients(uid));
        return results;
    }

    @Override
    public IntQueryResults<? extends Client> search(String keywords, Collection<String> orderBy, Integer offset, Integer limit) {
        IntQueryResults<DefaultClient> results = new IntQueryResults<>();
        results.setData(clientMapper.searchClients(keywords, orderBySqlBuilder.build(orderBy), offset, limit));
        results.setCount(clientMapper.countSearchClients(keywords));
        return results;
    }

    @Override
    public IntQueryResults<? extends Client> search(String keywords, Long uid, Collection<String> orderBy, Integer offset, Integer limit) {
        IntQueryResults<DefaultClient> results = new IntQueryResults<>();
        results.setData(clientMapper.searchUserClients(uid, keywords, orderBySqlBuilder.build(orderBy), offset, limit));
        results.setCount(clientMapper.countSearchUserClients(uid, keywords));
        return results;
    }

    @Transactional
    @Override
    public Client create(Long uid, String name, String description, String redirectUri, Collection<Long> scopes, Collection<Long> grantTypes,
                         Integer accessTokenValidity, Integer refreshTokenValidity, String additionalInformation, int status) {
        String id = idGenerator.generate();
        String secret = secretGenerator.generate();
        try {
            if (!clientMapper.insertClient(id, uid, secret, name, description, redirectUri,
                    accessTokenValidity, refreshTokenValidity, additionalInformation, status))
                ErrorEnum.CREATE_CLIENT_FAIL.throwException();
            if (!scopeMapper.insertClientScopeByScopeIds(id, scopes, false))
                ErrorEnum.CREATE_SCOPE_FAIL.details("fail to insert client scopes").throwException();
            if (!grantTypeMapper.insertClientGrantTypes(id, grantTypes))
                ErrorEnum.CREATE_GRANT_TYPE_FAIL.details("fail to insert client grant types").throwException();
        } catch (DuplicateKeyException e) {
            ErrorEnum.CLIENT_EXISTS.throwException();
        }
        Client result = clientMapper.loadClient(id);
        if (result == null)
            ErrorEnum.CREATE_CLIENT_FAIL.throwException();
        return result;
    }

    @Transactional
    @Override
    public Client create(Long uid, String name, String description, String redirectUri, Collection<Long> scopes, Collection<Long> grantTypes) {
        String id = idGenerator.generate();
        String secret = secretGenerator.generate();
        try {
            if (!clientMapper.insertClientDefault(id, uid, secret, name, description, redirectUri))
                ErrorEnum.CREATE_CLIENT_FAIL.throwException();
            if (!scopeMapper.insertClientScopeByScopeIds(id, scopes, false))
                ErrorEnum.CREATE_SCOPE_FAIL.details("fail to insert client scopes").throwException();
            if (!grantTypeMapper.insertClientGrantTypes(id, grantTypes))
                ErrorEnum.CREATE_GRANT_TYPE_FAIL.details("fail to insert client grant types").throwException();
        } catch (DuplicateKeyException e) {
            ErrorEnum.CLIENT_EXISTS.throwException();
        }
        Client result = clientMapper.loadClient(id);
        if (result == null)
            ErrorEnum.CREATE_CLIENT_FAIL.throwException();
        return result;
    }

    @Override
    public User getOwner(String cid) {
        return clientMapper.getClientOwner(cid);
    }

    @Override
    public String updateSecret(String cid) {
        String secret = secretGenerator.generate();
        if (!clientMapper.updateClientSecret(cid, secret))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client secret").throwException();
        return secret;
    }

    @Override
    public String updateSecret(String cid, Long uid) {
        String secret = secretGenerator.generate();
        if (!clientMapper.updateUserClientSecret(cid, uid, secret))
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
        if (!clientMapper.updateUserClientName(cid, uid, name))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client name").throwException();
    }

    @Override
    public void updateDescription(String cid, String description) {
        if (!clientMapper.updateClientDescription(cid, description))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client description").throwException();
    }

    @Override
    public void updateDescription(String cid, Long uid, String description) {
        if (!clientMapper.updateUserClientDescription(cid, uid, description))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client description").throwException();
    }

    @Override
    public void updateRedirectUri(String cid, String redirectUri) {
        if (!clientMapper.updateClientRedirectUri(cid, redirectUri))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client redirect uri").throwException();
    }

    @Override
    public void updateRedirectUri(String cid, Long uid, String redirectUri) {
        if (!clientMapper.updateUserClientRedirectUri(cid, uid, redirectUri))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client redirect uri").throwException();
    }

    @Override
    public void updateAccessTokenValidity(String cid, Integer accessTokenValidity) {
        if (!clientMapper.updateClientAccessTokenValidity(cid, accessTokenValidity))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to update client access token validity").throwException();
    }

    @Override
    public void updateRefreshTokenValidity(String cid, Integer refreshTokenValidity) {
        if (!clientMapper.updateClientAccessTokenValidity(cid, refreshTokenValidity))
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
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        return scopeMapper.listClientScopes(cid);
    }

    @Override
    public void addScopes(String cid, Collection<Long> sids) {
        if (!scopeMapper.insertClientScopeByScopeIds(cid, sids, false))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to insert client scopes").throwException();
    }

    @Override
    public void addScopes(String cid, Long uid, Collection<Long> sids) {
        if (!clientMapper.isClientOwner(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
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
        if (!clientMapper.isClientOwner(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        if (!scopeMapper.deleteClientScopes(cid, sids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to delete client scopes").throwException();
    }

    @Override
    public Collection<? extends GrantType> listGrantTypes(String cid) {
        return grantTypeMapper.listClientGrantTypes(cid);
    }

    @Override
    public Collection<? extends GrantType> listGrantTypes(String cid, Long uid) {
        if (!clientMapper.isClientOwner(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        return grantTypeMapper.listClientGrantTypes(cid);
    }

    @Override
    public void addGrantTypes(String cid, Collection<Long> tids) {
        if (!grantTypeMapper.insertClientGrantTypes(cid, tids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to insert client grant types").throwException();
    }

    @Override
    public void addGrantTypes(String cid, Long uid, Collection<Long> tids) {
        if (!clientMapper.isClientOwner(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
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
        if (!clientMapper.isClientOwner(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        if (!grantTypeMapper.deleteClientGrantTypes(cid, tids))
            ErrorEnum.UPDATE_CLIENT_FAIL.details("fail to delete client grant types").throwException();
    }
}
