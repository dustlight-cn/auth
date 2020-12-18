package cn.dustlight.auth.controllers.scopes;

import cn.dustlight.auth.entities.DefaultScope;
import cn.dustlight.auth.entities.Scope;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.services.ScopeService;
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
