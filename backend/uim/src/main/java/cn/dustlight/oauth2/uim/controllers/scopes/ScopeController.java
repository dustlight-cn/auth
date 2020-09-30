package cn.dustlight.oauth2.uim.controllers.scopes;

import cn.dustlight.oauth2.uim.Constants;
import cn.dustlight.oauth2.uim.entities.v1.scopes.Scope;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "授权作用域管理", description = "授权作用域的增删改查。")
@RequestMapping(value = Constants.V1.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public interface ScopeController<T extends Scope> {

    @Operation(summary = "获取授权作用域")
    @GetMapping("scopes")
    Collection<? extends Scope> getScopes(@RequestParam(required = false) Collection<Long> id);

    @Operation(summary = "修改或添加授权作用域")
    @PutMapping("scopes")
    @PreAuthorize("hasAnyAuthority('WRITE_SCOPE')")
    void setScopes(@RequestBody Collection<T> scopes);

    @Operation(summary = "删除授权作用域")
    @DeleteMapping("scopes")
    @PreAuthorize("hasAnyAuthority('DELETE_SCOPE')")
    void deleteScopes(@RequestParam Collection<Long> id);

    @Operation(summary = "获取授权作用域权限")
    @GetMapping("scope/{id}/authorities")
    Collection<String> getScopeAuthorities(@PathVariable Long id);

    @Operation(summary = "修改或添加授权作用域权限")
    @PutMapping("scope/{scopeId}/authorities")
    @PreAuthorize("hasAnyAuthority('WRITE_SCOPE')")
    void setScopeAuthorities(@PathVariable Long scopeId, @RequestParam Collection<Long> authorityId);

    @Operation(summary = "删除授权作用域权限")
    @DeleteMapping("scope/{scopeId}/authorities")
    @PreAuthorize("hasAnyAuthority('WRITE_SCOPE')")
    void deleteScopeAuthorities(@PathVariable Long scopeId, @RequestParam Collection<Long> authorityId);
}
