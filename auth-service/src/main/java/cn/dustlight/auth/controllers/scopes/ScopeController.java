package cn.dustlight.auth.controllers.scopes;

import cn.dustlight.auth.entities.Scope;
import cn.dustlight.auth.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "授权作用域管理", description = "授权作用域的增删改查。")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
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

    @Operation(summary = "获取应用授权作用域")
    @GetMapping("clients/{cid}/scopes")
    @PreAuthorize("hasAuthority('READ_CLIENT_ANY')")
    Collection<? extends Scope> getClientScopes(@PathVariable("cid") String cid);

    @Operation(summary = "添加应用授权作用域")
    @PutMapping("clients/{cid}/scopes")
    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    void addClientScopes(@PathVariable("cid") String cid, @RequestParam("sid") Collection<Long> sid);

    @Operation(summary = "删除应用授权作用域")
    @DeleteMapping("clients/{cid}/scopes")
    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    void removeClientScopes(@PathVariable("cid") String cid, @RequestParam("sid") Collection<Long> sid);

    @Operation(summary = "获取应用授权作用域")
    @GetMapping("users/{uid}/clients/{cid}/scopes")
    @PreAuthorize("#user.matchUid(#uid) or hasAuthority('READ_CLIENT_ANY')")
    Collection<? extends Scope> getUserClientScopes(@PathVariable("uid") Long uid, @PathVariable("cid") String cid);

    @Operation(summary = "添加应用授权作用域")
    @PutMapping("users/{uid}/clients/{cid}/scopes")
    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    void addUserClientScopes(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("sid") Collection<Long> sid);

    @Operation(summary = "删除应用授权作用域")
    @DeleteMapping("users/{uid}/clients/{cid}/scopes")
    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    void removeUserClientScopes(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("sid") Collection<Long> sid);
}
