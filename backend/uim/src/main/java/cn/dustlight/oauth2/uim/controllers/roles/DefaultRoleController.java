package cn.dustlight.oauth2.uim.controllers.roles;

import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.oauth2.uim.entities.v1.roles.DefaultRole;
import cn.dustlight.oauth2.uim.entities.v1.roles.DefaultUserRole;
import cn.dustlight.oauth2.uim.entities.v1.roles.Role;
import cn.dustlight.oauth2.uim.services.roles.RoleService;
import cn.dustlight.oauth2.uim.services.users.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class DefaultRoleController implements RoleController<DefaultRole> {

    @Autowired
    private RoleService roleService;

    @Autowired
    protected UserService userService;

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
    public Collection<String> getUserRoles(Long uid) {
        return userService.getRoles(uid);
    }

    @Override
    public void setUserRoles(Long uid, @RequestBody Collection<DefaultUserRole> roles) {
        userService.addRoles(uid, roles);
    }

    @Override
    public void deleteUserRoles(Long uid, Collection<Long> id) {
        userService.removeRoles(uid, id);
    }
}
