package cn.dustlight.auth.controllers.roles;

import cn.dustlight.auth.entities.DefaultUserRole;
import cn.dustlight.auth.entities.Role;
import cn.dustlight.auth.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "角色管理", description = "角色的增删改查。")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
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

    @Operation(summary = "获取用户角色")
    @GetMapping("users/{uid}/roles")
    @PreAuthorize("#user.matchUid(#uid) and hasAnyAuthority('READ_USER') or hasAnyAuthority('READ_USER_ANY')")
    Collection<? extends Role> getUserRoles(@PathVariable Long uid);

    @Operation(summary = "修改或添加用户角色")
    @PutMapping("users/{uid}/roles")
    @PreAuthorize("hasAnyAuthority('WRITE_USER_ROLE')")
    void setUserRoles(@PathVariable Long uid, @RequestBody Collection<DefaultUserRole> roles);

    @Operation(summary = "删除用户角色")
    @DeleteMapping("users/{uid}/roles")
    @PreAuthorize("hasAnyAuthority('WRITE_USER_ROLE')")
    void deleteUserRoles(@PathVariable Long uid, @RequestParam Collection<Long> id);
}
