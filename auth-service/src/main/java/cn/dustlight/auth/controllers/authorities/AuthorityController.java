package cn.dustlight.auth.controllers.authorities;

import cn.dustlight.auth.entities.Authority;
import cn.dustlight.auth.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "权限管理", description = "权限的增删改查。")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public interface AuthorityController<T extends Authority> {

    @Operation(summary = "获取权限")
    @GetMapping("authorities")
    Collection<? extends Authority> getAuthorities(@RequestParam(required = false) Collection<Long> id);

    @Operation(summary = "修改或添加权限")
    @PutMapping("authorities")
    @PreAuthorize("hasAnyAuthority('WRITE_AUTHORITY')")
    void setAuthorities(@RequestBody Collection<T> authorities);

    @Operation(summary = "删除权限")
    @DeleteMapping("authorities")
    @PreAuthorize("hasAnyAuthority('DELETE_AUTHORITY')")
    void deleteAuthorities(@RequestParam Collection<Long> id);

    @Operation(summary = "获取授权作用域权限")
    @GetMapping("client/{cid}/authorities")
    Collection<String> getClientAuthorities(@PathVariable("cid") String cid);

    @Operation(summary = "修改或添加授权作用域权限")
    @PutMapping("client/{cid}/authorities")
    @PreAuthorize("hasAnyAuthority('WRITE_SCOPE')")
    void setClientAuthorities(@PathVariable("cid") String cid, @RequestParam Collection<Long> authorityId);

    @Operation(summary = "删除授权作用域权限")
    @DeleteMapping("client/{cid}/authorities")
    @PreAuthorize("hasAnyAuthority('WRITE_SCOPE')")
    void deleteClientAuthorities(@PathVariable("cid") String cid, @RequestParam Collection<Long> authorityId);

    @Operation(summary = "获取角色权限")
    @GetMapping("roles/{rid}/authorities")
    Collection<String> getRoleAuthorities(@PathVariable("rid") Long rid);

    @Operation(summary = "修改或添加角色权限")
    @PutMapping("roles/{rid}/authorities")
    @PreAuthorize("hasAnyAuthority('WRITE_ROLE')")
    void setRoleAuthorities(@PathVariable("rid") Long rid, @RequestParam Collection<Long> authorityId);

    @Operation(summary = "删除角色权限")
    @DeleteMapping("roles/{rid}/authorities")
    @PreAuthorize("hasAnyAuthority('WRITE_ROLE')")
    void deleteRoleAuthorities(@PathVariable("rid") Long rid, @RequestParam Collection<Long> authorityId);

}
