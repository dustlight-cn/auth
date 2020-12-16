package cn.dustlight.auth.services;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.Scope;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.mappers.AuthorityMapper;
import cn.dustlight.auth.mappers.ScopeMapper;

import java.util.Collection;

public class DefaultScopeService implements ScopeService {

    private final UniqueGenerator<Long> idGenerator;
    private final ScopeMapper scopeMapper;
    private final AuthorityMapper authorityMapper;

    public DefaultScopeService(UniqueGenerator<Long> idGenerator,
                               ScopeMapper scopeMapper,
                               AuthorityMapper authorityMapper) {
        this.idGenerator = idGenerator;
        this.scopeMapper = scopeMapper;
        this.authorityMapper = authorityMapper;
    }

    @Override
    public Collection<? extends Scope> listScopes() {
        return scopeMapper.listScopes();
    }

    @Override
    public Collection<? extends Scope> getScopes(Collection<Long> sids) {
        return scopeMapper.selectScopes(sids);
    }

    @Override
    public Scope getScope(Long sid) {
        return scopeMapper.selectScope(sid);
    }

    @Override
    public void createScope(String name, String subtitle, String description) {
        if (!scopeMapper.insertScope(idGenerator.generate(), name, subtitle, description))
            ErrorEnum.CREATE_SCOPE_FAIL.throwException();
    }

    @Override
    public void createScopes(Collection<? extends Scope> scopes) {
        if (!scopeMapper.insertScopes(scopes))
            ErrorEnum.CREATE_SCOPE_FAIL.throwException();
    }

    @Override
    public void removeScope(Long sid) {
        if (!scopeMapper.deleteScope(sid))
            ErrorEnum.DELETE_SCOPE_FAIL.throwException();
    }

    @Override
    public void removeScopes(Collection<Long> sids) {
        if (!scopeMapper.deleteScopes(sids))
            ErrorEnum.DELETE_SCOPE_FAIL.throwException();
    }

    @Override
    public Collection<String> listScopeAuthorities(Long sid) {
        return authorityMapper.listScopeAuthorities(sid);
    }

    @Override
    public Collection<String> listScopeAuthorities(Collection<String> scopeNames) {
        return authorityMapper.listScopeAuthoritiesByScopeNames(scopeNames);
    }

    @Override
    public void createScopeAuthorities(Long sid, Collection<Long> aids) {
        if (!authorityMapper.insertScopeAuthorities(sid, aids))
            ErrorEnum.CREATE_SCOPE_FAIL.throwException();
    }

    @Override
    public void removeScopeAuthorities(Long sid, Collection<Long> aids) {
        if (!authorityMapper.deleteScopeAuthorities(sid, aids))
            ErrorEnum.DELETE_SCOPE_FAIL.throwException();
    }
}
