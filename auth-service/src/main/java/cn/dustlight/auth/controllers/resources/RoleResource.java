package cn.dustlight.auth.controllers.resources;

import cn.dustlight.auth.entities.DefaultRole;
import cn.dustlight.auth.entities.DefaultUserRole;
import cn.dustlight.auth.entities.Role;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.services.RoleService;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.util.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "Roles", description = "角色的增删改查。")
@SecurityRequirement(name = "Access Token")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public class RoleResource {

    @Autowired
    private RoleService roleService;

    @Autowired
    protected UserService userService;

    @Autowired
    private UniqueGenerator<Long> idGenerator;

    @GetMapping("roles")
    @Operation(summary = "获取角色")
    public Collection<? extends Role> getRoles(@RequestParam(required = false) Collection<Long> id) {
        if (id == null)
            return roleService.listRoles();
        return roleService.getRoles(id);
    }

    @PreAuthorize("hasAnyAuthority('WRITE_ROLE')")
    @PutMapping("roles")
    @Operation(summary = "修改或添加角色")
    public void setRoles(@RequestBody Collection<DefaultRole> roles) {
        for (DefaultRole role : roles)
            if (role.getRid() == null)
                role.setRid(idGenerator.generate());
        roleService.createRoles(roles);
    }

    @PreAuthorize("hasAnyAuthority('DELETE_ROLE')")
    @DeleteMapping("roles")
    @Operation(summary = "删除角色")
    public void deleteRoles(@RequestParam Collection<Long> id) {
        roleService.removeRoles(id);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('READ_USER') or hasAnyAuthority('READ_USER_ANY')")
    @GetMapping("users/{uid}/roles")
    @Operation(summary = "获取用户角色")
    public Collection<? extends Role> getUserRoles(@PathVariable Long uid) {
        return userService.getRoles(uid);
    }

    @PreAuthorize("hasAnyAuthority('WRITE_USER_ROLE')")
    @PutMapping("users/{uid}/roles")
    @Operation(summary = "修改或添加用户角色")
    public void setUserRoles(@PathVariable Long uid, @RequestBody Collection<DefaultUserRole> roles) {
        userService.addRoles(uid, roles);
    }

    @PreAuthorize("hasAnyAuthority('WRITE_USER_ROLE')")
    @DeleteMapping("users/{uid}/roles")
    @Operation(summary = "删除用户角色")
    public void deleteUserRoles(@PathVariable Long uid, @RequestParam Collection<Long> id) {
        userService.removeRoles(uid, id);
    }
}
