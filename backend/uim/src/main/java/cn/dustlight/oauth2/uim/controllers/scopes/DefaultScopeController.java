package cn.dustlight.oauth2.uim.controllers.scopes;

import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.oauth2.uim.entities.v1.scopes.DefaultScope;
import cn.dustlight.oauth2.uim.entities.v1.scopes.Scope;
import cn.dustlight.oauth2.uim.services.scopes.ScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class DefaultScopeController implements ScopeController<DefaultScope> {

    @Autowired
    private ScopeService scopeService;

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
    public Collection<String> getScopeAuthorities(Long id) {
        return scopeService.listScopeAuthorities(id);
    }

    @Override
    public void setScopeAuthorities(Long scopeId, Collection<Long> authorityId) {
        scopeService.createScopeAuthorities(scopeId, authorityId);
    }

    @Override
    public void deleteScopeAuthorities(Long scopeId, Collection<Long> authorityId) {
        scopeService.removeScopeAuthorities(scopeId, authorityId);
    }
}
