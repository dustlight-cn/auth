package cn.dustlight.oauth2.uim.controllers.scopes;

import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.oauth2.uim.entities.v1.scopes.DefaultScope;
import cn.dustlight.oauth2.uim.entities.v1.scopes.Scope;
import cn.dustlight.oauth2.uim.services.clients.ClientService;
import cn.dustlight.oauth2.uim.services.scopes.ScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class DefaultScopeController implements ScopeController<DefaultScope> {

    @Autowired
    private ScopeService scopeService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private UniqueGenerator<Long> idGenerator;

    @Override
    public Collection<? extends Scope> getScopes(Collection<Long> id) {
        if (id == null)
            return scopeService.listScopes();
        return scopeService.getScopes(id);
    }

    @Override
    public void setScopes(Collection<DefaultScope> scopes) {
        for (DefaultScope scope : scopes)
            if (scope.getSid() == null)
                scope.setSid(idGenerator.generate());
        scopeService.createScopes(scopes);
    }

    @Override
    public void deleteScopes(Collection<Long> id) {
        scopeService.removeScopes(id);
    }

    @Override
    public Collection<? extends Scope> getClientScopes(String cid) {
        return clientService.listScopes(cid);
    }

    @Override
    public void addClientScopes(String cid, Collection<Long> sid) {
        clientService.addScopes(cid, sid);
    }

    @Override
    public void removeClientScopes(String cid, Collection<Long> sid) {
        clientService.removeScopes(cid, sid);
    }

    @Override
    public Collection<? extends Scope> getUserClientScopes(Long uid, String cid) {
        return clientService.listScopes(cid, uid);
    }

    @Override
    public void addUserClientScopes(Long uid, String cid, Collection<Long> sid) {
        clientService.addScopes(cid, uid, sid);
    }

    @Override
    public void removeUserClientScopes(Long uid, String cid, Collection<Long> sid) {
        clientService.removeScopes(cid, uid, sid);
    }
}
