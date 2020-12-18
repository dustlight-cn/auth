package cn.dustlight.auth.controllers;

import cn.dustlight.auth.entities.DefaultGrantType;
import cn.dustlight.auth.entities.GrantType;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.services.GrantTypeService;
import cn.dustlight.auth.util.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Collection;

@RestController
@Tag(name = "授权模式管理", description = "授权模式的增删改查。")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public class GrantTypeController {

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

    @PreAuthorize("hasAuthority('WRITE_TYPE')")
    @PutMapping("types")
    @Operation(summary = "添加或修改授权模式")
    public void setGrantTypes(@RequestBody Collection<DefaultGrantType> types) {
        if (types != null)
            for (DefaultGrantType type : types)
                if (type.getTid() == null)
                    type.setTid(idGenerator.generate());
        grantTypeService.createGrantTypes(types);
    }

    @PreAuthorize("hasAuthority('READ_CLIENT_ANY')")
    @GetMapping("clients/{cid}/types")
    @Operation(summary = "获取应用授权模式")
    public Collection<? extends GrantType> getClientGrantTypes(@PathVariable("cid") String cid) {
        return clientService.listGrantTypes(cid);
    }

    @PreAuthorize("#user.matchUid(#uid) or hasAuthority('READ_CLIENT_ANY')")
    @GetMapping("users/{uid}/clients/{cid}/types")
    @Operation(summary = "获取应用授权模式")
    public Collection<? extends GrantType> getUserClientGrantTypes(@PathVariable("uid") Long uid, @PathVariable("cid") String cid) {
        return clientService.listGrantTypes(cid, uid);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    @DeleteMapping("users/{uid}/clients/{cid}/types")
    @Operation(summary = "删除应用授权模式")
    public void deleteUserClientGrantTypes(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("tid") Collection<Long> tid) {
        clientService.removeGrantTypes(cid, uid, tid);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("users/{uid}/clients/{cid}/types")
    @Operation(summary = "添加或修改应用授权模式")
    public void addUserClientGrantTypes(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("tid") Collection<Long> tid) {
        clientService.addGrantTypes(cid, uid, tid);
    }

    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    @DeleteMapping("clients/{cid}/types")
    @Operation(summary = "删除应用授权模式")
    public void deleteClientGrantTypes(@PathVariable("cid") String cid, @RequestParam("tid") Collection<Long> tid) {
        clientService.removeGrantTypes(cid, tid);
    }

    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("clients/{cid}/types")
    @Operation(summary = "添加或修改应用授权模式")
    public void addClientGrantTypes(@PathVariable("cid") String cid, @RequestParam("tid") Collection<Long> tid) {
        clientService.addGrantTypes(cid, tid);
    }

    @PreAuthorize("hasAuthority('DELETE_TYPE')")
    @DeleteMapping("types")
    @Operation(summary = "删除授权模式")
    public void deleteGrantTypes(@RequestParam("tid") Collection<Long> tid) {
        grantTypeService.removeGrantTypes(tid);
    }
}
