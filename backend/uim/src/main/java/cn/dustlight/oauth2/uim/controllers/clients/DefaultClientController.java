package cn.dustlight.oauth2.uim.controllers.clients;

import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.oauth2.uim.entities.results.QueryResults;
import cn.dustlight.oauth2.uim.entities.v1.clients.Client;
import cn.dustlight.oauth2.uim.services.clients.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;

@RestController
public class DefaultClientController implements ClientController {

    @Autowired
    private ClientService clientService;

    @Override
    public QueryResults<? extends Client, ? extends Number> getClients(String keywords, Collection<String> order, Integer offset, Integer limit) {
        if (keywords == null || keywords.length() == 0)
            return clientService.list(order, offset, limit);
        return clientService.search(keywords, order, offset, limit);
    }

    @Override
    public void removeClients(Collection<String> cids) {
        clientService.delete(cids);
    }

    @Override
    public Client getClient(String cid) {
        return clientService.loadClientByClientId(cid);
    }

    @Override
    public void removeClient(String cid) {
        clientService.delete(Arrays.asList(cid));
    }

    @Override
    public Client createClient(Long uid, String name, String description, String redirectUri, Collection<Long> scopes, Collection<Long> grantTypes, Integer accessTokenValidity, Integer refreshTokenValidity, String additionalInformation, Integer status) {
        return clientService.create(uid, name, description, redirectUri, scopes, grantTypes, accessTokenValidity, refreshTokenValidity, additionalInformation, status);
    }

    @Override
    public String updateClientSecret(String cid) {
        return clientService.updateSecret(cid);
    }

    @Override
    public void updateClientName(String cid, String name) {
        clientService.updateName(cid, name);
    }

    @Override
    public void updateClientDescription(String cid, String description) {
        clientService.updateDescription(cid, description);
    }

    @Override
    public void updateClientRedirectUri(String cid, String redirectUri) {
        clientService.updateRedirectUri(cid, redirectUri);
    }

    @Override
    public void updateClientStatus(String cid, Integer status) {
        clientService.updateStatus(cid, status);
    }

    @Override
    public void updateClientAccessTokenValidity(String cid, Integer accessTokenValidity) {
        clientService.updateAccessTokenValidity(cid, accessTokenValidity);
    }

    @Override
    public void updateClientRefreshTokenValidity(String cid, Integer refreshTokenValidity) {
        clientService.updateRefreshTokenValidity(cid, refreshTokenValidity);
    }

    @Override
    public QueryResults<? extends Client, ? extends Number> getClients(Long uid, String keywords, Collection<String> order, Integer offset, Integer limit) {
        if (keywords == null || keywords.length() == 0)
            return clientService.list(uid, order, offset, limit);
        return clientService.search(keywords, uid, order, offset, limit);
    }

    @Override
    public void removeClients(Long uid, Collection<String> cids) {
        clientService.delete(uid, cids);
    }

    @Override
    public Client getClient(Long uid, String cid) {
        Client result = clientService.loadClientByClientId(cid);
        if (result == null || !uid.equals(result.getUid()))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        return result;
    }

    @Override
    public void removeClient(Long uid, String cid) {
        clientService.delete(uid, Arrays.asList(cid));
    }

    @Override
    public Client createClient(Long uid, String name, String description, String redirectUri, Collection<Long> scopes, Collection<Long> grantTypes) {
        return clientService.create(uid, name, description, redirectUri, scopes, grantTypes);
    }

    @Override
    public String updateClientSecret(Long uid, String cid) {
        return clientService.updateSecret(cid, uid);
    }

    @Override
    public void updateClientName(Long uid, String cid, String name) {
        clientService.updateName(cid, uid, name);
    }

    @Override
    public void updateClientDescription(Long uid, String cid, String description) {
        clientService.updateDescription(cid, uid, description);
    }

    @Override
    public void updateClientRedirectUri(Long uid, String cid, String redirectUri) {
        clientService.updateRedirectUri(cid, uid, redirectUri);
    }
}
