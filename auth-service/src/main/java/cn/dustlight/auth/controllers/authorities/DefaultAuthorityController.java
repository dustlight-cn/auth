package cn.dustlight.auth.controllers.authorities;

import cn.dustlight.auth.entities.Authority;
import cn.dustlight.auth.entities.DefaultAuthority;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.services.AuthorityService;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class DefaultAuthorityController implements AuthorityController<DefaultAuthority> {

    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UniqueGenerator<Long> idGenerator;

    @Override
    public Collection<? extends Authority> getAuthorities(Collection<Long> id) {
        if (id == null)
            return authorityService.listAuthorities();
        return authorityService.getAuthorities(id);
    }

    @Override
    public void setAuthorities(Collection<DefaultAuthority> authorities) {
        for (DefaultAuthority authority : authorities)
            if (authority.getAid() == null)
                authority.setAid(idGenerator.generate());
        authorityService.createAuthorities(authorities);
    }

    @Override
    public void deleteAuthorities(Collection<Long> id) {
        authorityService.removeAuthorities(id);
    }

    @Override
    public Collection<String> getClientAuthorities(String cid) {
        return clientService.listAuthorities(cid);
    }

    @Override
    public void setClientAuthorities(String cid, Collection<Long> authorityId) {
        clientService.addAuthorities(cid, authorityId);
    }

    @Override
    public void deleteClientAuthorities(String cid, Collection<Long> authorityId) {
        clientService.removeAuthorities(cid, authorityId);
    }

    @Override
    public Collection<String> getRoleAuthorities(Long rid) {
        return roleService.listRoleAuthorities(rid);
    }

    @Override
    public void setRoleAuthorities(Long rid, Collection<Long> authorityId) {
        roleService.createRoleAuthorities(rid, authorityId);
    }

    @Override
    public void deleteRoleAuthorities(Long rid, Collection<Long> authorityId) {
        roleService.removeRoleAuthorities(rid, authorityId);
    }

}
