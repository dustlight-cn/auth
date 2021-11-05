package cn.dustlight.auth.controllers.resources;

import cn.dustlight.auth.entities.DefaultGrantType;
import cn.dustlight.auth.entities.GrantType;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.services.GrantTypeService;
import cn.dustlight.auth.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "GrantTypes", description = "授权模式的增删改查。")
@SecurityRequirement(name = "AccessToken")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@CrossOrigin(origins = Constants.CrossOrigin.origin, allowCredentials = Constants.CrossOrigin.allowCredentials)
public class GrantTypeResource {

    @Autowired
    private GrantTypeService grantTypeService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private UniqueGenerator<Long> idGenerator;

    @GetMapping("types")
    @Operation(summary = "获取授权模式")
    public Collection<? extends GrantType> getGrantTypes(@RequestParam(required = false, name = "tid") Collection<Long> tid) {
        if (tid == null || tid.size() == 0)
            return grantTypeService.listGrantTypes();
        return grantTypeService.getGrantTypes(tid);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_TYPE')) and #oauth2.clientHasAnyRole('WRITE_TYPE')")
    @PutMapping("types")
    @Operation(summary = "添加或修改授权模式", description = "应用和用户需要 WRITE_TYPE 权限。")
    public void setGrantTypes(@RequestBody Collection<DefaultGrantType> types) {
        if (types != null)
            for (DefaultGrantType type : types)
                if (type.getTid() == null)
                    type.setTid(idGenerator.generate());
        grantTypeService.createGrantTypes(types);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_TYPE')) and #oauth2.clientHasAnyRole('WRITE_TYPE')")
    @DeleteMapping("types")
    @Operation(summary = "删除授权模式", description = "应用和用户需要 WRITE_TYPE 权限。")
    public void deleteGrantTypes(@RequestParam("tid") Collection<Long> tid) {
        grantTypeService.removeGrantTypes(tid);
    }

    /* ----------------------------------------------------------------------------------------------------------- */

    @PreAuthorize("(#oauth2.client or hasAuthority('READ_CLIENT')) and #oauth2.clientHasAnyRole('READ_CLIENT')")
    @GetMapping("clients/{cid}/types")
    @Operation(summary = "获取应用授权模式", description = "应用和用户需要 READ_CLIENT 权限。")
    public Collection<? extends GrantType> getClientGrantTypes(@PathVariable("cid") String cid) {
        return clientService.listGrantTypes(cid);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @DeleteMapping("clients/{cid}/types")
    @Operation(summary = "删除应用授权模式", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void deleteClientGrantTypes(@PathVariable("cid") String cid, @RequestParam("tid") Collection<Long> tid) {
        clientService.removeGrantTypes(cid, tid);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("clients/{cid}/types")
    @Operation(summary = "添加应用授权模式", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void addClientGrantTypes(@PathVariable("cid") String cid, @RequestParam("tid") Collection<Long> tid) {
        clientService.addGrantTypes(cid, tid);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('READ_CLIENT')) and #oauth2.clientHasAnyRole('READ_CLIENT')")
    @GetMapping("users/{uid}/clients/{cid}/types")
    @Operation(summary = "获取应用授权模式", description = "应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。")
    public Collection<? extends GrantType> getUserClientGrantTypes(@PathVariable("uid") Long uid, @PathVariable("cid") String cid) {
        return clientService.listGrantTypes(cid, uid);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @DeleteMapping("users/{uid}/clients/{cid}/types")
    @Operation(summary = "删除应用授权模式", description = "应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。")
    public void deleteUserClientGrantTypes(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("tid") Collection<Long> tid) {
        clientService.removeGrantTypes(cid, uid, tid);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("users/{uid}/clients/{cid}/types")
    @Operation(summary = "添加应用授权模式", description = "应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。")
    public void addUserClientGrantTypes(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("tid") Collection<Long> tid) {
        clientService.addGrantTypes(cid, uid, tid);
    }
}
