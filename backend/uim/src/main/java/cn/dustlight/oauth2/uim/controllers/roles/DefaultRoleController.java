package cn.dustlight.oauth2.uim.controllers.roles;

import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.oauth2.uim.entities.v1.roles.DefaultRole;
import cn.dustlight.oauth2.uim.entities.v1.roles.Role;
import cn.dustlight.oauth2.uim.services.roles.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class DefaultRoleController implements RoleController<DefaultRole> {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UniqueGenerator<Long> idGenerator;

    @Override
    public Collection<? extends Role> getRoles(Collection<Long> id) {
        if (id == null)
            return roleService.listRoles();
        return roleService.getRoles(id);
    }

    @Override
    public void setRoles(Collection<DefaultRole> roles) {
        for (DefaultRole role : roles)
            if (role.getRid() == null)
                role.setRid(idGenerator.generate());
        roleService.createRoles(roles);
    }

    @Override
    public void deleteRoles(Collection<Long> id) {
        roleService.removeRoles(id);
    }

    @Override
    public Collection<String> getRoleAuthorities(Long id) {
        return roleService.listRoleAuthorities(id);
    }

    @Override
    public void setRoleAuthorities(Long roleId, Collection<Long> authorityId) {
        roleService.createRoleAuthorities(roleId, authorityId);
    }

    @Override
    public void deleteRoleAuthorities(Long roleId, Collection<Long> authorityId) {
        roleService.removeRoleAuthorities(roleId, authorityId);
    }
}
