package cn.dustlight.auth.controllers.clients;

import cn.dustlight.auth.entities.Client;
import cn.dustlight.auth.util.Constants;
import cn.dustlight.auth.util.QueryResults;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "应用管理业务", description = "应用的增删改查")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public interface ClientController {

    /* ---------------------------------------------------------------------------------------- */

    /* 管理员权限 */

    @Operation(summary = "查询应用")
    @GetMapping("clients")
    @PreAuthorize("hasAuthority('READ_CLIENT_ANY')")
    QueryResults<? extends Client> getClients(@RequestParam(required = false, name = "q") String keywords,
                                              @RequestParam(required = false) Collection<String> order,
                                              @RequestParam(required = false) Integer offset,
                                              @RequestParam(required = false) Integer limit);

    @Operation(summary = "删除应用")
    @DeleteMapping("clients")
    @PreAuthorize("hasAuthority('DELETE_CLIENT_ANY')")
    void removeClients(@RequestParam Collection<String> cids);

    @Operation(summary = "获取应用")
    @GetMapping("clients/{cid}")
    @PreAuthorize("hasAuthority('READ_CLIENT_ANY')")
    Client getClient(@PathVariable("cid") String cid);

    @Operation(summary = "删除应用")
    @DeleteMapping("clients/{cid}")
    @PreAuthorize("hasAuthority('DELETE_CLIENT_ANY')")
    void removeClient(@PathVariable("cid") String cid);

    @Operation(summary = "创建应用")
    @PostMapping("clients")
    @PreAuthorize("hasAuthority('CREATE_CLIENT') and hasAuthority('WRITE_CLIENT_ANY')")
    Client createClient(@RequestParam Long uid,
                        @RequestParam String name,
                        @RequestParam String description,
                        @RequestParam String redirectUri,
                        @RequestParam Collection<Long> scopes,
                        @RequestParam Collection<Long> grantTypes,
                        @RequestParam(required = false, defaultValue = "7200") Integer accessTokenValidity,
                        @RequestParam(required = false, defaultValue = "86400") Integer refreshTokenValidity,
                        @RequestParam(required = false) String additionalInformation,
                        @RequestParam(required = false, defaultValue = "0") Integer status);

    @Operation(summary = "更新应用密钥")
    @PutMapping("clients/{cid}/secret")
    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    String updateClientSecret(@PathVariable("cid") String cid);

    @Operation(summary = "更新应用名称")
    @PutMapping("clients/{cid}/name")
    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    void updateClientName(@PathVariable("cid") String cid, @RequestParam("name") String name);

    @Operation(summary = "更新应用描述")
    @PutMapping("clients/{cid}/description")
    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    void updateClientDescription(@PathVariable("cid") String cid, @RequestParam("description") String description);

    @Operation(summary = "更新应用回调地址")
    @PutMapping("clients/{cid}/redirect")
    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    void updateClientRedirectUri(@PathVariable("cid") String cid, @RequestParam("redirectUri") String redirectUri);

    @Operation(summary = "更新应用状态")
    @PutMapping("clients/{cid}/status")
    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    void updateClientStatus(@PathVariable("cid") String cid, @RequestParam("status") Integer status);

    @Operation(summary = "更新应用AccessToken有效期")
    @PutMapping("clients/{cid}/access-token-validity")
    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    void updateClientAccessTokenValidity(@PathVariable("cid") String cid, @RequestParam("accessTokenValidity") Integer accessTokenValidity);

    @Operation(summary = "更新应用RefreshToken有效期")
    @PutMapping("clients/{cid}/refresh-token-validity")
    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    void updateClientRefreshTokenValidity(@PathVariable("cid") String cid, @RequestParam("refreshTokenValidity") Integer refreshTokenValidity);

    @Operation(summary = "获取应用Logo")
    @GetMapping("clients/{cid}/logo")
    void getClientLogo(@PathVariable("cid") String cid);

    @Operation(summary = "更新应用Logo",
            requestBody = @RequestBody(required = true, content = @Content(mediaType = "image/*", schema = @Schema(type = "string", format = "binary")))
    )
    @PutMapping("clients/{cid}/logo")
    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    void updateClientLogo(@PathVariable("cid") String cid);

    /* ---------------------------------------------------------------------------------------- */

    /* 开发者权限 */

    @Operation(summary = "查询应用")
    @GetMapping("users/{uid}/clients")
    @PreAuthorize("#user.matchUid(#uid) or hasAuthority('READ_CLIENT_ANY')")
    QueryResults<? extends Client> getClients(@PathVariable("uid") Long uid,
                                              @RequestParam(required = false, name = "q") String keywords,
                                              @RequestParam(required = false) Collection<String> order,
                                              @RequestParam(required = false) Integer offset,
                                              @RequestParam(required = false) Integer limit);

    @Operation(summary = "删除应用")
    @DeleteMapping("users/{uid}/clients")
    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('DELETE_CLIENT_ANY')")
    void removeClients(@PathVariable("uid") Long uid,
                       @RequestParam Collection<String> cids);

    @Operation(summary = "获取应用")
    @GetMapping("users/{uid}/clients/{cid}")
    @PreAuthorize("#user.matchUid(#uid) or hasAuthority('READ_CLIENT_ANY')")
    Client getClient(@PathVariable("uid") Long uid,
                     @PathVariable("cid") String cid);

    @Operation(summary = "删除应用")
    @DeleteMapping("users/{uid}/clients/{cid}")
    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('DELETE_CLIENT_ANY')")
    void removeClient(@PathVariable("uid") Long uid,
                      @PathVariable("cid") String cid);

    @Operation(summary = "创建应用")
    @PostMapping("users/{uid}/clients")
    @PreAuthorize("(#user.matchUid(#uid) or hasAuthority('WRITE_CLIENT_ANY')) and hasAuthority('CREATE_CLIENT')")
    Client createClient(@PathVariable("uid") Long uid,
                        @RequestParam String name,
                        @RequestParam String description,
                        @RequestParam String redirectUri,
                        @RequestParam Collection<Long> scopes,
                        @RequestParam Collection<Long> grantTypes);

    @Operation(summary = "更新应用密钥")
    @PutMapping("users/{uid}/clients/{cid}/secret")
    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    String updateClientSecret(@PathVariable("uid") Long uid, @PathVariable("cid") String cid);

    @Operation(summary = "更新应用名称")
    @PutMapping("users/{uid}/clients/{cid}/name")
    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    void updateClientName(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("name") String name);

    @Operation(summary = "更新应用描述")
    @PutMapping("users/{uid}/clients/{cid}/description")
    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    void updateClientDescription(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("description") String description);

    @Operation(summary = "更新应用回调地址")
    @PutMapping("users/{uid}/clients/{cid}/redirect")
    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    void updateClientRedirectUri(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("redirectUri") String redirectUri);

    @Operation(summary = "获取应用Logo")
    @GetMapping("users/{uid}/clients/{cid}/logo")
    void getClientLogo(@PathVariable("uid") Long uid, @PathVariable("cid") String cid);

    @Operation(summary = "更新应用Logo",
            requestBody = @RequestBody(required = true, content = @Content(mediaType = "image/*", schema = @Schema(type = "string", format = "binary")))
    )
    @PutMapping("users/{uid}/clients/{cid}/logo")
    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    void updateClientLogo(@PathVariable("uid") Long uid, @PathVariable("cid") String cid);

    /* ---------------------------------------------------------------------------------------- */

}
