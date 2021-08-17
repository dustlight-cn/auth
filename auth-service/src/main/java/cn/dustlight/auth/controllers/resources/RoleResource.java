package cn.dustlight.auth.controllers.resources;

import cn.dustlight.auth.entities.DefaultRole;
import cn.dustlight.auth.entities.DefaultUserRole;
import cn.dustlight.auth.entities.Role;
import cn.dustlight.auth.entities.UserRoleClient;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.services.RoleService;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.util.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "Roles", description = "角色的增删改查。")
@SecurityRequirement(name = "AccessToken")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@CrossOrigin(origins = Constants.CrossOrigin.origin, allowCredentials = Constants.CrossOrigin.allowCredentials)
public class RoleResource {

    @Autowired
    private RoleService roleService;

    @Autowired
    protected UserService userService;

    @Autowired
    private UniqueGenerator<Long> idGenerator;

    @GetMapping("roles")
    @Operation(summary = "获取角色")
    public Collection<? extends Role> getRoles(@RequestParam(required = false) Collection<Long> id,
                                               OAuth2Authentication authentication) {
        if (id == null)
            return roleService.listRolesWithClientId(authentication.getOAuth2Request().getClientId());
        return roleService.getRoles(id);
    }

    @PreAuthorize("(#oauth2.client or hasAnyAuthority('WRITE_ROLE')) and #oauth2.clientHasAnyRole('WRITE_ROLE')")
    @PutMapping("roles")
    @Operation(summary = "修改或添加角色", description = "应用和用户需要 WRITE_ROLE 权限。")
    public void setRoles(@RequestBody Collection<DefaultRole> roles,
                         OAuth2Authentication authentication) {
        for (DefaultRole role : roles)
            if (role.getRid() == null)
                role.setRid(idGenerator.generate());
        roleService.createRoles(roles, authentication.getOAuth2Request().getClientId());
    }

    @PreAuthorize("(#oauth2.client or hasAnyAuthority('WRITE_ROLE')) and #oauth2.clientHasAnyRole('WRITE_ROLE')")
    @DeleteMapping("roles")
    @Operation(summary = "删除角色", description = "应用和用户需要 WRITE_ROLE 权限。")
    public void deleteRoles(@RequestParam Collection<Long> id,
                            OAuth2Authentication authentication) {
        roleService.removeRolesWithClientId(id, authentication.getOAuth2Request().getClientId());
    }

    /* ------------------------------------------------------------------------------------------------- */

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAnyAuthority('READ_USER')) and #oauth2.clientHasAnyRole('READ_USER')")
    @GetMapping("users/{uid}/roles")
    @Operation(summary = "获取用户角色", description = "应用和用户（uid 为当前用户除外）需要 READ_USER 权限。")
    public Collection<? extends Role> getUserRoles(@PathVariable Long uid) {
        return userService.getRoles(uid);
    }

    @PreAuthorize("(#oauth2.client or hasAnyAuthority('GRANT_USER')) and #oauth2.clientHasAnyRole('GRANT_USER')")
    @PutMapping("users/{uid}/roles")
    @Operation(summary = "为用户添加角色", description = "应用和用户需要 GRANT_USER 权限。")
    public void setUserRoles(@PathVariable Long uid, @RequestBody Collection<DefaultUserRole> roles) {
        userService.addRoles(uid, roles);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAnyAuthority('GRANT_USER')) and #oauth2.clientHasAnyRole('GRANT_USER')")
    @DeleteMapping("users/{uid}/roles")
    @Operation(summary = "删除用户的角色", description = "应用和用户需要 GRANT_USER 权限。")
    public void deleteUserRoles(@PathVariable Long uid, @RequestParam Collection<Long> id) {
        userService.removeRoles(uid, id);
    }

    /* ----------------------------------------------------------------- */

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAnyAuthority('READ_USER')) and #oauth2.clientHasAnyRole('READ_USER')")
    @GetMapping("users/{uid}/role-clients")
    @Operation(summary = "获取用户角色", description = "应用和用户（uid 为当前用户除外）需要 READ_USER 权限。")
    public Collection<UserRoleClient> getUserRoleClients(@PathVariable Long uid) {
        return userService.getRoleClients(uid);
    }

    @PreAuthorize("(#oauth2.client or hasAnyAuthority('GRANT_USER')) and #oauth2.clientHasAnyRole('GRANT_USER')")
    @PutMapping("users/{uid}/role-clients")
    @Operation(summary = "为用户添加角色", description = "应用和用户需要 GRANT_USER 权限。")
    public void setUserRoleClients(@PathVariable Long uid, @RequestBody Collection<DefaultUserRole> roles) {
        userService.addRoles(uid, roles);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAnyAuthority('GRANT_USER')) and #oauth2.clientHasAnyRole('GRANT_USER')")
    @DeleteMapping("users/{uid}/role-clients")
    @Operation(summary = "删除用户的角色", description = "应用和用户需要 GRANT_USER 权限。")
    public void deleteUserRoleClients(@PathVariable Long uid, @RequestParam Collection<Long> id) {
        userService.removeRoles(uid, id);
    }
}
