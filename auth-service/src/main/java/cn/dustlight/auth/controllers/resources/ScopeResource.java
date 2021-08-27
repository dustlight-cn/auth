package cn.dustlight.auth.controllers.resources;

import cn.dustlight.auth.entities.DefaultScope;
import cn.dustlight.auth.entities.Scope;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.services.ScopeService;
import cn.dustlight.auth.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "Scopes", description = "授权作用域的增删改查。")
@SecurityRequirement(name = "AccessToken")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@CrossOrigin(origins = Constants.CrossOrigin.origin, allowCredentials = Constants.CrossOrigin.allowCredentials)
public class ScopeResource {

    @Autowired
    private ScopeService scopeService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private UniqueGenerator<Long> idGenerator;

    @GetMapping("scopes")
    @Operation(summary = "获取授权作用域", description = "应用和用户需要 READ_CLIENT 权限。")
    public Collection<? extends Scope> getScopes(@RequestParam(required = false) Collection<Long> id) {
        if (id == null)
            return scopeService.listScopes();
        return scopeService.getScopes(id);
    }

    @PreAuthorize("(#oauth2.client or hasAnyAuthority('WRITE_SCOPE')) and #oauth2.clientHasAnyRole('WRITE_SCOPE')")
    @PutMapping("scopes")
    @Operation(summary = "修改或添加授权作用域", description = "应用和用户需要 WRITE_SCOPE 权限。")
    public void setScopes(@RequestBody Collection<DefaultScope> scopes) {
        for (DefaultScope scope : scopes)
            if (scope.getSid() == null)
                scope.setSid(idGenerator.generate());
        scopeService.createScopes(scopes);
    }

    @PreAuthorize("(#oauth2.client or hasAnyAuthority('WRITE_SCOPE')) and #oauth2.clientHasAnyRole('WRITE_SCOPE')")
    @DeleteMapping("scopes")
    @Operation(summary = "删除授权作用域", description = "应用和用户需要 WRITE_SCOPE 权限。")
    public void deleteScopes(@RequestParam Collection<Long> id) {
        scopeService.removeScopes(id);
    }

    /* ---------------------------------------------------------------------------------------------------- */

    @PreAuthorize("(#oauth2.client or hasAuthority('READ_CLIENT')) and #oauth2.clientHasAnyRole('READ_CLIENT')")
    @GetMapping("clients/{cid}/scopes")
    @Operation(summary = "获取应用授权作用域", description = "应用和用户需要 READ_CLIENT 权限。")
    public Collection<? extends Scope> getClientScopes(@PathVariable("cid") String cid) {
        return clientService.listScopes(cid);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("clients/{cid}/scopes")
    @Operation(summary = "添加应用授权作用域", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void addClientScopes(@PathVariable("cid") String cid, @RequestParam("sid") Collection<Long> sid) {
        clientService.addScopes(cid, sid);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @DeleteMapping("clients/{cid}/scopes")
    @Operation(summary = "删除应用授权作用域", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void removeClientScopes(@PathVariable("cid") String cid, @RequestParam("sid") Collection<Long> sid) {
        clientService.removeScopes(cid, sid);
    }

    /* --------------------------------------------------------- */

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('READ_CLIENT')) and #oauth2.clientHasAnyRole('READ_CLIENT')")
    @GetMapping("users/{uid}/clients/{cid}/scopes")
    @Operation(summary = "获取应用授权作用域", description = "应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。")
    public Collection<? extends Scope> getUserClientScopes(@PathVariable("uid") Long uid, @PathVariable("cid") String cid) {
        return clientService.listScopes(cid, uid);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("users/{uid}/clients/{cid}/scopes")
    @Operation(summary = "添加应用授权作用域", description = "应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。")
    public void addUserClientScopes(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("sid") Collection<Long> sid) {
        clientService.addScopes(cid, uid, sid);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @DeleteMapping("users/{uid}/clients/{cid}/scopes")
    @Operation(summary = "删除应用授权作用域", description = "应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。")
    public void removeUserClientScopes(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("sid") Collection<Long> sid) {
        clientService.removeScopes(cid, uid, sid);
    }
}
