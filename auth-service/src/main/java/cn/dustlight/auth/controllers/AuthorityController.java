package cn.dustlight.auth.controllers;

import cn.dustlight.auth.util.Constants;
import cn.dustlight.auth.entities.Authority;
import cn.dustlight.auth.entities.DefaultAuthority;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.services.AuthorityService;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.services.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "权限管理", description = "权限的增删改查。")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UniqueGenerator<Long> idGenerator;

    @GetMapping("authorities")
    @Operation(summary = "获取权限")
    public Collection<? extends Authority> getAuthorities(@RequestParam(required = false) Collection<Long> id) {
        if (id == null)
            return authorityService.listAuthorities();
        return authorityService.getAuthorities(id);
    }

    @PreAuthorize("hasAnyAuthority('WRITE_AUTHORITY')")
    @PutMapping("authorities")
    @Operation(summary = "修改或添加权限")
    public void setAuthorities(@RequestBody Collection<DefaultAuthority> authorities) {
        for (DefaultAuthority authority : authorities)
            if (authority.getAid() == null)
                authority.setAid(idGenerator.generate());
        authorityService.createAuthorities(authorities);
    }

    @PreAuthorize("hasAnyAuthority('DELETE_AUTHORITY')")
    @DeleteMapping("authorities")
    @Operation(summary = "删除权限")
    public void deleteAuthorities(@RequestBody Collection<Long> id) {
        authorityService.removeAuthorities(id);
    }

    @GetMapping("clients/{cid}/authorities")
    @Operation(summary = "获取授权作用域权限")
    public Collection<String> getClientAuthorities(@PathVariable("cid") String cid) {
        return clientService.listAuthorities(cid);
    }

    @PreAuthorize("hasAnyAuthority('WRITE_SCOPE')")
    @PutMapping("clients/{cid}/authorities")
    @Operation(summary = "修改或添加授权作用域权限")
    public void setClientAuthorities(@PathVariable("cid") String cid, @RequestParam Collection<Long> authorityId) {
        clientService.addAuthorities(cid, authorityId);
    }

    @PreAuthorize("hasAnyAuthority('WRITE_SCOPE')")
    @DeleteMapping("clients/{cid}/authorities")
    @Operation(summary = "删除授权作用域权限")
    public void deleteClientAuthorities(@PathVariable("cid") String cid, @RequestParam Collection<Long> authorityId) {
        clientService.removeAuthorities(cid, authorityId);
    }

    @GetMapping("roles/{rid}/authorities")
    @Operation(summary = "获取角色权限")
    public Collection<String> getRoleAuthorities(@PathVariable("rid") Long rid) {
        return roleService.listRoleAuthorities(rid);
    }

    @PreAuthorize("hasAnyAuthority('WRITE_ROLE')")
    @PutMapping("roles/{rid}/authorities")
    @Operation(summary = "修改或添加角色权限")
    public void setRoleAuthorities(@PathVariable("rid") Long rid, @RequestParam Collection<Long> authorityId) {
        roleService.createRoleAuthorities(rid, authorityId);
    }

    @PreAuthorize("hasAnyAuthority('WRITE_ROLE')")
    @DeleteMapping("roles/{rid}/authorities")
    @Operation(summary = "删除角色权限")
    public void deleteRoleAuthorities(@PathVariable("rid") Long rid, @RequestParam Collection<Long> authorityId) {
        roleService.removeRoleAuthorities(rid, authorityId);
    }

}
