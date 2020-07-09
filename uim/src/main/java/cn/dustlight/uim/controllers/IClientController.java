package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.models.*;
import org.apache.ibatis.annotations.Delete;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 开放平台业务控制器
 */
@RequestMapping("/api/client")
public interface IClientController {

    @PostMapping("/app")
    @PreAuthorize("hasAnyAuthority('CREATE_CLIENT')")
    RestfulResult createClient(String name, String redirectUri, String description,
                               @RequestParam(value = "scopes") List<Long> scopes,
                               @RequestParam(value = "grantTypes") List<Long> grantTypes,
                               Authentication authentication);

    @DeleteMapping("/app/{appKey}")
    @PreAuthorize("hasAnyAuthority('DELETE_CLIENT','DELETE_CLIENT_ANY')")
    RestfulResult deleteClient(@PathVariable String appKey, Authentication authentication);

    @PostMapping("/app_secret/{appKey}")
    @PreAuthorize("hasAnyAuthority('UPDATE_CLIENT','UPDATE_CLIENT_ANY')")
    RestfulResult<String> resetClientSecret(@PathVariable String appKey, Authentication authentication);

    @PostMapping("/app_name/{appKey}")
    @PreAuthorize("hasAnyAuthority('UPDATE_CLIENT','UPDATE_CLIENT_ANY')")
    RestfulResult updateClientName(@PathVariable String appKey, String name, Authentication authentication);

    @PostMapping("/app_description/{appKey}")
    @PreAuthorize("hasAnyAuthority('UPDATE_CLIENT','UPDATE_CLIENT_ANY')")
    RestfulResult updateClientDescription(@PathVariable String appKey, String description, Authentication authentication);

    @PostMapping("/app_redirect_uri/{appKey}")
    @PreAuthorize("hasAnyAuthority('UPDATE_CLIENT','UPDATE_CLIENT_ANY')")
    RestfulResult updateClientRedirectUri(@PathVariable String appKey, String redirectUri, Authentication authentication);

//    @PostMapping("/app_scopes/{appKey}")
//    @PreAuthorize("hasAnyAuthority('UPDATE_CLIENT','UPDATE_CLIENT_ANY')")
//    RestfulResult updateClientScopes(@PathVariable String appKey, String redirectUri, Authentication authentication);

    @GetMapping("/scopes")
    <T extends IScopeDetails> RestfulResult<List<T>> getScopeDetails();

    @GetMapping("/authorities")
    <T extends IAuthorityDetails> RestfulResult<List<T>> getAuthorityDetails();

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('MANAGE_ROLE')")
    <T extends IRoleDetails> RestfulResult<List<T>> getRoles();

    @PostMapping("/role")
    @PreAuthorize("hasAuthority('MANAGE_ROLE')")
    RestfulResult insertRole(String name, String description);

    @PostMapping("/role/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ROLE')")
    RestfulResult updateRole(@PathVariable Long id, String name, String description);

    @DeleteMapping("/role/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ROLE')")
    RestfulResult deleteRole(@PathVariable Long id);

    @DeleteMapping("/role_authority")
    @PreAuthorize("hasAuthority('MANAGE_ROLE')")
    RestfulResult removeRoleAuthority(Long roleId, Long authorityId);

    @PostMapping("/role_authority")
    @PreAuthorize("hasAuthority('MANAGE_ROLE')")
    RestfulResult insertAuthority(Long roleId, Long authorityId);

    @PostMapping("/authority/{id}")
    @PreAuthorize("hasAuthority('MANAGE_AUTHORITY')")
    RestfulResult updateAuthority(@PathVariable Long id, String name, String description);

    @DeleteMapping("/authority/{id}")
    @PreAuthorize("hasAuthority('MANAGE_AUTHORITY')")
    RestfulResult deleteAuthority(@PathVariable Long id);

    @PostMapping("/authority")
    @PreAuthorize("hasAuthority('MANAGE_AUTHORITY')")
    RestfulResult insertAuthority(String name, String description);

    @GetMapping("/grant_types")
    <T extends IGrantType> RestfulResult<List<T>> getGrantTypes();

    @GetMapping("/clients")
    @PreAuthorize("isAuthenticated()")
    <T extends IClientDetails> RestfulResult<List<T>> getCurrentUserClientDetails(Authentication authentication);

    @GetMapping("/clients/{userId}")
    @PreAuthorize("hasAuthority('QUERY_USER_CLIENT')")
    <T extends IClientDetails> RestfulResult<List<T>> getUserClientDetails(@PathVariable Long userId);

    @GetMapping("/authorities/role/{roleId}")
    <T extends IAuthorityDetails> RestfulResult<List<T>> getRoleAuthorities(@PathVariable Long roleId);

    @GetMapping("/authorities/scope/{scopeId}")
    <T extends IAuthorityDetails> RestfulResult<List<T>> getScopeAuthorities(@PathVariable Long scopeId);

}
