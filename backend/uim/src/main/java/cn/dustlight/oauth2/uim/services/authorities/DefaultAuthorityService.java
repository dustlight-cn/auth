package cn.dustlight.oauth2.uim.services.authorities;

import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.oauth2.uim.entities.v1.authorities.Authority;
import cn.dustlight.oauth2.uim.mappers.AuthorityMapper;
import cn.dustlight.oauth2.uim.mappers.RoleMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

public class DefaultAuthorityService implements AuthorityService {

    private AuthorityMapper authorityMapper;
    private UniqueGenerator<Long> idGenerator;

    public DefaultAuthorityService(AuthorityMapper authorityMapper,
                                   RoleMapper roleMapper,
                                   PasswordEncoder passwordEncoder,
                                   UniqueGenerator<Long> idGenerator) {
        this.authorityMapper = authorityMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public Collection<? extends Authority> listAuthorities() {
        return authorityMapper.listAuthorities();
    }

    @Override
    public Collection<? extends Authority> selectAuthorities(Collection<Long> aids) {
        return authorityMapper.selectAuthorities(aids);
    }

    @Override
    public Authority selectAuthority(Long aid) {
        return authorityMapper.selectAuthority(aid);
    }

    @Override
    public void insertAuthority(String name, String description) {
        if (!authorityMapper.insertAuthority(idGenerator.generate(), name, description))
            ErrorEnum.CREATE_AUTHORITY_FAIL.throwException();
    }

    @Override
    public void insertAuthorities(Collection<Authority> authorities) {
        if (!authorityMapper.insertAuthorities(authorities))
            ErrorEnum.CREATE_AUTHORITY_FAIL.throwException();
    }

    @Override
    public void deleteAuthority(Long aid) {
        if (!authorityMapper.deleteAuthority(aid))
            ErrorEnum.DELETE_AUTHORITY_FAIL.throwException();
    }

    @Override
    public void deleteAuthorities(Collection<Long> aids) {
        if (!authorityMapper.deleteAuthorities(aids))
            ErrorEnum.DELETE_AUTHORITY_FAIL.throwException();
    }
}
