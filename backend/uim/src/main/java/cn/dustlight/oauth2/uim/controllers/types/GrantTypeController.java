package cn.dustlight.oauth2.uim.controllers.types;

import cn.dustlight.oauth2.uim.Constants;
import cn.dustlight.oauth2.uim.entities.v1.types.GrantType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "授权模式管理", description = "授权模式的增删改查。")
@RequestMapping(value = Constants.V1.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public interface GrantTypeController<T extends GrantType> {

    @Operation(summary = "获取授权模式")
    @GetMapping("types")
    Collection<? extends GrantType> getGrantTypes(@RequestParam(required = false, name = "tid") Collection<Long> tid);

    @Operation(summary = "添加或修改授权模式")
    @PutMapping("types")
    @PreAuthorize("hasAuthority('WRITE_TYPE')")
    void setGrantTypes(@RequestBody Collection<T> types);

    @Operation(summary = "删除授权模式")
    @DeleteMapping("types")
    @PreAuthorize("hasAuthority('DELETE_TYPE')")
    void deleteGrantTypes(@RequestParam("tid") Collection<Long> tid);

    @Operation(summary = "获取应用授权模式")
    @GetMapping("clients/{cid}/types")
    @PreAuthorize("hasAuthority('READ_CLIENT_ANY')")
    Collection<? extends GrantType> getClientGrantTypes(@PathVariable("cid") String cid);

    @Operation(summary = "添加或修改应用授权模式")
    @PutMapping("clients/{cid}/types")
    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    void addClientGrantTypes(@PathVariable("cid") String cid,
                             @RequestParam("tid") Collection<Long> tid);

    @Operation(summary = "删除应用授权模式")
    @DeleteMapping("clients/{cid}/types")
    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    void deleteClientGrantTypes(@PathVariable("cid") String cid,
                                @RequestParam("tid") Collection<Long> tid);


    @Operation(summary = "获取应用授权模式")
    @GetMapping("users/{uid}/clients/{cid}/types")
    @PreAuthorize("#user.matchUid(#uid) or hasAuthority('READ_CLIENT_ANY')")
    Collection<? extends GrantType> getUserClientGrantTypes(@PathVariable("uid") Long uid,
                                                            @PathVariable("cid") String cid);

    @Operation(summary = "添加或修改应用授权模式")
    @PutMapping("users/{uid}/clients/{cid}/types")
    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    void addUserClientGrantTypes(@PathVariable("uid") Long uid,
                                 @PathVariable("cid") String cid,
                                 @RequestParam("tid") Collection<Long> tid);

    @Operation(summary = "删除应用授权模式")
    @DeleteMapping("users/{uid}/clients/{cid}/types")
    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    void deleteUserClientGrantTypes(@PathVariable("uid") Long uid,
                                    @PathVariable("cid") String cid,
                                    @RequestParam("tid") Collection<Long> tid);
}
