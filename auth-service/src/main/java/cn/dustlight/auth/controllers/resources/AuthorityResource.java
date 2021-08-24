package cn.dustlight.auth.controllers.resources;

import cn.dustlight.auth.util.Constants;
import cn.dustlight.auth.entities.Authority;
import cn.dustlight.auth.entities.DefaultAuthority;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.services.AuthorityService;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.services.RoleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Authorities", description = "权限资源的增删改查。")
@SecurityRequirement(name = "AccessToken")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@CrossOrigin(origins = Constants.CrossOrigin.origin, allowCredentials = Constants.CrossOrigin.allowCredentials)
public class AuthorityResource {

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
    public Collection<? extends Authority> getAuthorities(@RequestParam(required = false) Collection<Long> id,
                                                          @RequestParam(required = false, name = "clientId") String clientId,
                                                          OAuth2Authentication authentication) {
        if (!StringUtils.hasText(clientId))
            clientId = authentication.getOAuth2Request().getClientId();
        if (id == null)
            return authorityService.listAuthorities(clientId);
        return authorityService.getAuthorities(id);
    }

    @PreAuthorize("(#oauth2.client or #user.isClientOwnerOrMember(#clientId) or hasAnyAuthority('WRITE_AUTHORITY')) and #oauth2.clientHasAnyRole('WRITE_AUTHORITY')")
    @PutMapping("authorities")
    @Operation(summary = "修改或添加权限", description = "应用和用户需要 WRITE_AUTHORITY 权限。")
    public Collection<DefaultAuthority> setAuthorities(@RequestBody Collection<DefaultAuthority> authorities,
                                                       @RequestParam(required = false, name = "clientId") String clientId,
                                                       OAuth2Authentication authentication) {
        if (!StringUtils.hasText(clientId))
            clientId = authentication.getOAuth2Request().getClientId();
        for (DefaultAuthority authority : authorities)
            if (authority.getAid() == null)
                authority.setAid(idGenerator.generate());
        authorityService.createAuthorities(authorities, clientId);
        return authorities;
    }

    @PreAuthorize("(#oauth2.client or #user.isClientOwnerOrMember(#clientId) or hasAnyAuthority('WRITE_AUTHORITY')) and #oauth2.clientHasAnyRole('WRITE_AUTHORITY')")
    @DeleteMapping("authorities")
    @Operation(summary = "删除权限", description = "应用和用户需要 WRITE_AUTHORITY 权限。")
    public void deleteAuthorities(@RequestBody Collection<Long> id,
                                  @RequestParam(required = false, name = "clientId") String clientId,
                                  OAuth2Authentication authentication) {
        if (!StringUtils.hasText(clientId))
            clientId = authentication.getOAuth2Request().getClientId();
        authorityService.removeAuthorities(id, clientId);
    }

    /* ---------------------------------------------------------------------------------------------- */

    @GetMapping("roles/{rid}/authorities")
    @Operation(summary = "获取角色权限")
    public Collection<String> getRoleAuthorities(@PathVariable("rid") Long rid) {
        return roleService.listRoleAuthorities(rid);
    }

    @PreAuthorize("(#oauth2.client or #user.isClientOwnerOrMember(#clientId) or hasAnyAuthority('GRANT_ROLE')) and #oauth2.clientHasAnyRole('GRANT_ROLE')")
    @PutMapping("roles/{rid}/authorities")
    @Operation(summary = "添加角色权限", description = "应用和用户需要 GRANT_ROLE 权限。")
    public void setRoleAuthorities(@PathVariable("rid") Long rid,
                                   @RequestParam Collection<Long> authorityId,
                                   @RequestParam(required = false, name = "clientId") String clientId,
                                   OAuth2Authentication authentication) {
        if (!StringUtils.hasText(clientId))
            clientId = authentication.getOAuth2Request().getClientId();
        roleService.createRoleAuthorities(rid, authorityId, clientId);
    }

    @PreAuthorize("(#oauth2.client or #user.isClientOwnerOrMember(#clientId) or hasAnyAuthority('GRANT_ROLE')) and #oauth2.clientHasAnyRole('GRANT_ROLE')")
    @DeleteMapping("roles/{rid}/authorities")
    @Operation(summary = "删除角色权限", description = "应用和用户需要 GRANT_ROLE 权限。")
    public void deleteRoleAuthorities(@PathVariable("rid") Long rid,
                                      @RequestParam Collection<Long> authorityId,
                                      @RequestParam(required = false, name = "clientId") String clientId) {
        roleService.removeRoleAuthorities(rid, authorityId);
    }

    /* --------------------------------------------------------------------------------------------------- */

    @PreAuthorize("(#oauth2.client or hasAuthority('READ_CLIENT')) and #oauth2.clientHasAnyRole('READ_CLIENT')")
    @GetMapping("clients/{cid}/authorities")
    @Operation(summary = "获取应用权限", description = "应用和用户需要 READ_CLIENT 权限。")
    public Collection<String> getClientAuthorities(@PathVariable("cid") String cid) {
        return clientService.listAuthorities(cid);
    }

    @PreAuthorize("(#oauth2.client or hasAnyAuthority('GRANT_CLIENT')) and #oauth2.clientHasAnyRole('GRANT_CLIENT')")
    @PutMapping("clients/{cid}/authorities")
    @Operation(summary = "添加应用权限", description = "应用和用户需要 GRANT_CLIENT 权限。")
    public void setClientAuthorities(@PathVariable("cid") String cid, @RequestParam Collection<Long> authorityId) {
        clientService.addAuthorities(cid, authorityId);
    }

    @PreAuthorize("(#oauth2.client or hasAnyAuthority('GRANT_CLIENT')) and #oauth2.clientHasAnyRole('GRANT_CLIENT')")
    @DeleteMapping("clients/{cid}/authorities")
    @Operation(summary = "删除应用权限", description = "应用和用户需要 GRANT_CLIENT 权限。")
    public void deleteClientAuthorities(@PathVariable("cid") String cid, @RequestParam Collection<Long> authorityId) {
        clientService.removeAuthorities(cid, authorityId);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('READ_CLIENT')) and #oauth2.clientHasAnyRole('READ_CLIENT')")
    @GetMapping("users/{uid}/clients/{cid}/authorities")
    @Operation(summary = "获取应用权限", description = "应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。")
    public Collection<String> getUserClientAuthorities(@PathVariable("uid") Long uid,
                                                       @PathVariable("cid") String cid) {
        return clientService.listAuthorities(cid, uid);
    }
}
