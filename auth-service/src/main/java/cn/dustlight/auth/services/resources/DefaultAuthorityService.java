package cn.dustlight.auth.services.resources;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.Authority;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.mappers.AuthorityMapper;
import cn.dustlight.auth.services.AuthorityService;

import java.util.Collection;

public class DefaultAuthorityService implements AuthorityService {

    private final AuthorityMapper authorityMapper;
    private final UniqueGenerator<Long> idGenerator;

    public DefaultAuthorityService(AuthorityMapper authorityMapper,
                                   UniqueGenerator<Long> idGenerator) {
        this.authorityMapper = authorityMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public Collection<? extends Authority> listAuthorities() {
        return authorityMapper.listAuthorities();
    }

    @Override
    public Collection<? extends Authority> listAuthorities(String cid) {
        return authorityMapper.listAuthoritiesWithClientId(cid);
    }

    @Override
    public Collection<? extends Authority> getAuthorities(Collection<Long> aids) {
        return authorityMapper.selectAuthorities(aids);
    }

    @Override
    public Authority getAuthority(Long aid) {
        return authorityMapper.selectAuthority(aid);
    }

    @Override
    public void createAuthority(String name, String description, String cid) {
        try {
            if (!authorityMapper.insertAuthority(idGenerator.generate(), name, description, cid))
                ErrorEnum.CREATE_AUTHORITY_FAIL.throwException();
        } catch (Exception e) {
            ErrorEnum.CREATE_AUTHORITY_FAIL.details(e.getMessage()).throwException();
        }
    }

    @Override
    public void createAuthorities(Collection<? extends Authority> authorities,
                                  String clientId) {
        try {
            if (!authorityMapper.insertAuthorities(authorities, clientId))
                ErrorEnum.CREATE_AUTHORITY_FAIL.throwException();
        } catch (Exception e) {
            ErrorEnum.CREATE_AUTHORITY_FAIL.details(e.getMessage()).throwException();
        }

    }

    @Override
    public void removeAuthority(Long aid) {
        try {
            if (!authorityMapper.deleteAuthority(aid))
                ErrorEnum.DELETE_AUTHORITY_FAIL.throwException();
        } catch (Exception e) {
            ErrorEnum.DELETE_AUTHORITY_FAIL.details(e.getMessage()).throwException();
        }
    }

    @Override
    public void removeAuthority(Long aid, String cid) {
        try {
            if (!authorityMapper.deleteAuthorityWithClientId(aid, cid))
                ErrorEnum.DELETE_AUTHORITY_FAIL.throwException();
        } catch (Exception e) {
            ErrorEnum.DELETE_AUTHORITY_FAIL.details(e.getMessage()).throwException();
        }
    }

    @Override
    public void removeAuthorities(Collection<Long> aids) {
        try {
            if (!authorityMapper.deleteAuthorities(aids))
                ErrorEnum.DELETE_AUTHORITY_FAIL.throwException();
        } catch (Exception e) {
            ErrorEnum.DELETE_AUTHORITY_FAIL.details(e.getMessage()).throwException();
        }
    }

    @Override
    public void removeAuthorities(Collection<Long> aids, String cid) {
        try {
            if (!authorityMapper.deleteAuthoritiesWithClientId(aids, cid))
                ErrorEnum.DELETE_AUTHORITY_FAIL.throwException();
        } catch (Exception e) {
            ErrorEnum.DELETE_AUTHORITY_FAIL.details(e.getMessage()).throwException();
        }
    }
}
