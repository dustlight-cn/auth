package cn.dustlight.oauth2.uim.controllers.roles;

import cn.dustlight.oauth2.uim.Constants;
import cn.dustlight.oauth2.uim.entities.v1.roles.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "角色管理业务", description = "角色的增删改查")
@RequestMapping(value = Constants.V1.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public interface RoleController<T extends Role> {

    @Operation(summary = "获取角色")
    @GetMapping("roles")
    Collection<? extends Role> getRoles(@RequestParam(required = false) Collection<Long> id);

    @Operation(summary = "修改或添加角色")
    @PutMapping("roles")
    @PreAuthorize("hasAnyAuthority('WRITE_ROLE')")
    void setRoles(@RequestBody Collection<T> roles);

    @Operation(summary = "删除角色")
    @DeleteMapping("roles")
    @PreAuthorize("hasAnyAuthority('DELETE_ROLE')")
    void deleteRoles(@RequestParam Collection<Long> id);

    @Operation(summary = "获取角色权限")
    @GetMapping("role/{id}/authorities")
    Collection<String> getRoleAuthorities(@PathVariable Long id);

    @Operation(summary = "修改或添加角色权限")
    @PutMapping("role/{roleId}/authorities")
    @PreAuthorize("hasAnyAuthority('WRITE_ROLE')")
    void setRoleAuthorities(@PathVariable Long roleId, @RequestParam Collection<Long> authorityId);

    @Operation(summary = "删除角色权限")
    @DeleteMapping("role/{roleId}/authorities")
    @PreAuthorize("hasAnyAuthority('WRITE_ROLE')")
    void deleteRoleAuthorities(@PathVariable Long roleId, @RequestParam Collection<Long> authorityId);
}
