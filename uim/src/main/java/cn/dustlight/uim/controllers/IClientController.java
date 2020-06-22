package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.models.AuthorityDetails;
import cn.dustlight.uim.models.ClientDetails;
import cn.dustlight.uim.models.IUserDetails;
import cn.dustlight.uim.models.ScopeDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.scope.Scope;

import java.security.Principal;
import java.util.List;
import java.util.Set;

/**
 * 开放平台业务控制器
 */
@RequestMapping("/api/client")
public interface IClientController {

    @GetMapping("/scopes")
    RestfulResult<List<ScopeDetails>> getScopeDetails();

    @GetMapping("/authorities")
    RestfulResult<List<AuthorityDetails>> getAuthorityDetails();

    @GetMapping("/clients")
    @PreAuthorize("isAuthenticated()")
    RestfulResult<List<ClientDetails>> getCurrentUserClientDetails(Authentication authentication);

    @GetMapping("/clients/{userId}")
    @PreAuthorize("hasAuthority('QUERY_USER_CLIENT')")
    RestfulResult<List<ClientDetails>> getUserClientDetails(@PathVariable Long userId);

    @GetMapping("/authorities/role/{roleId}")
    RestfulResult<List<AuthorityDetails>> getRoleAuthorities(@PathVariable Long roleId);

    @GetMapping("/authorities/scope/{scopeId}")
    RestfulResult<List<AuthorityDetails>> getScopeAuthorities(@PathVariable Long scopeId);
}
