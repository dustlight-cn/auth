package cn.dustlight.oauth2.uim.controllers.clients;

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
}
