package cn.dustlight.auth.controllers.resources;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.Client;
import cn.dustlight.auth.entities.DefaultClient;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.services.storages.StorageHandler;
import cn.dustlight.auth.util.Constants;
import cn.dustlight.auth.util.QueryResults;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

@RestController
@Tag(name = "Clients", description = "应用的增删改查")
@SecurityRequirement(name = "AccessToken")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@CrossOrigin(origins = Constants.CrossOrigin.origin, allowCredentials = Constants.CrossOrigin.allowCredentials)
public class ClientResource {

    @Autowired
    private ClientService<DefaultClient> clientService;

    @Autowired
    private StorageHandler storageHandler;

    @PreAuthorize("(#oauth2.client or hasAuthority('READ_CLIENT')) and #oauth2.clientHasAnyRole('READ_CLIENT')")
    @GetMapping("clients")
    @Operation(summary = "查询应用", description = "应用和用户需要 READ_CLIENT 权限。")
    public QueryResults<? extends Client> getClients(@RequestParam(required = false, name = "q") String keywords, @RequestParam(required = false) Collection<String> order, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit) {
        if (keywords == null || keywords.length() == 0)
            return setLogo(clientService.list(order, offset, limit));
        return setLogo(clientService.search(keywords, order, offset, limit));
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @DeleteMapping("clients")
    @Operation(summary = "删除应用", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void removeClients(@RequestParam Collection<String> cids) {
        clientService.delete(cids);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('READ_CLIENT')) and #oauth2.clientHasAnyRole('READ_CLIENT')")
    @GetMapping("clients/{cid}")
    @Operation(summary = "获取应用", description = "应用和用户需要 READ_CLIENT 权限。")
    public Client getClient(@PathVariable("cid") String cid) {
        return setLogo(clientService.loadClientByClientId(cid));
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @DeleteMapping("clients/{cid}")
    @Operation(summary = "删除应用", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void removeClient(@PathVariable("cid") String cid) throws IOException {
        clientService.delete(Arrays.asList(cid));
        try {
            storageHandler.remove(generateLogoKey(cid));
        } catch (Exception e) {
            ErrorEnum.DELETE_CLIENT_LOGO_FAIL.details(e.getMessage());
        }
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PostMapping("clients")
    @Operation(summary = "创建应用", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public Client createClient(@RequestParam Long uid,
                               @RequestParam String name,
                               @RequestParam String description,
                               @RequestParam String redirectUri,
                               @RequestParam(required = false) Collection<Long> scopes,
                               @RequestParam(required = false) Collection<Long> grantTypes,
                               @RequestParam(required = false, defaultValue = "7200") Integer accessTokenValidity,
                               @RequestParam(required = false, defaultValue = "86400") Integer refreshTokenValidity,
                               @RequestParam(required = false) String additionalInformation,
                               @RequestParam(required = false, defaultValue = "0") Integer status) {
        return setLogo(clientService.create(uid, name, description, redirectUri, scopes, grantTypes, accessTokenValidity, refreshTokenValidity, additionalInformation, status));
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("clients/{cid}/secret")
    @Operation(summary = "更新应用密钥", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public String updateClientSecret(@PathVariable("cid") String cid) {
        return clientService.updateSecret(cid);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("clients/{cid}/name")
    @Operation(summary = "更新应用名称", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void updateClientName(@PathVariable("cid") String cid, @RequestParam("name") String name) {
        clientService.updateName(cid, name);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("clients/{cid}/description")
    @Operation(summary = "更新应用描述", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void updateClientDescription(@PathVariable("cid") String cid, @RequestParam("description") String description) {
        clientService.updateDescription(cid, description);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("clients/{cid}/redirect")
    @Operation(summary = "更新应用回调地址", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void updateClientRedirectUri(@PathVariable("cid") String cid, @RequestParam("redirectUri") String redirectUri) {
        clientService.updateRedirectUri(cid, redirectUri);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("clients/{cid}/status")
    @Operation(summary = "更新应用状态", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void updateClientStatus(@PathVariable("cid") String cid, @RequestParam("status") Integer status) {
        clientService.updateStatus(cid, status);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("clients/{cid}/access-token-validity")
    @Operation(summary = "更新应用AccessToken有效期", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void updateClientAccessTokenValidity(@PathVariable("cid") String cid, @RequestParam("accessTokenValidity") Integer accessTokenValidity) {
        clientService.updateAccessTokenValidity(cid, accessTokenValidity);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("clients/{cid}/refresh-token-validity")
    @Operation(summary = "更新应用RefreshToken有效期", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void updateClientRefreshTokenValidity(@PathVariable("cid") String cid, @RequestParam("refreshTokenValidity") Integer refreshTokenValidity) {
        clientService.updateRefreshTokenValidity(cid, refreshTokenValidity);
    }

    @GetMapping("clients/{cid}/logo")
    @Operation(summary = "获取应用 Logo")
    public void getClientLogo(@PathVariable("cid") String cid) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String key = generateLogoKey(cid);
            storageHandler.handle(key, attributes.getRequest(), attributes.getResponse(), "image/*");
        } catch (IOException e) {
            ErrorEnum.RESOURCE_NOT_FOUND.details(e.getMessage()).throwException();
        }
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("clients/{cid}/logo")
    @Operation(summary = "更新应用 Logo", description = "应用和用户需要 WRITE_CLIENT 权限。",
            requestBody = @RequestBody(required = true, content = @Content(mediaType = "image/*", schema = @Schema(type = "string", format = "binary"))))
    public void updateClientLogo(@PathVariable("cid") String cid) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String key = generateLogoKey(cid);
            storageHandler.handle(key, attributes.getRequest(), attributes.getResponse());
        } catch (IOException e) {
            ErrorEnum.UPDATE_CLIENT_FAIL.details(e.getMessage()).throwException();
        }
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("clients/{cid}/members")
    @Operation(summary = "添加应用成员", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void addClientMembers(@PathVariable("cid") String cid,
                                 @RequestParam("uids") Collection<Long> uids) {
        clientService.addMembers(cid, uids);
    }

    @PreAuthorize("(#oauth2.client or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @DeleteMapping("clients/{cid}/members")
    @Operation(summary = "移除应用成员", description = "应用和用户需要 WRITE_CLIENT 权限。")
    public void removeClientMembers(@PathVariable("cid") String cid,
                                    @RequestParam("uids") Collection<Long> uids) {
        clientService.removeMembers(cid, uids);
    }

    /* ------------------------------------------------------------------------------------------------ */

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('READ_CLIENT')) and #oauth2.clientHasAnyRole('READ_CLIENT')")
    @GetMapping("users/{uid}/clients")
    @Operation(summary = "查询用户应用", description = "应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。")
    public QueryResults<? extends Client> getUserClients(@PathVariable("uid") Long uid,
                                                         @RequestParam(required = false, name = "q") String keywords,
                                                         @RequestParam(required = false) Collection<String> order,
                                                         @RequestParam(required = false) Integer offset,
                                                         @RequestParam(required = false) Integer limit) {
        if (keywords == null || keywords.length() == 0)
            return setLogo(clientService.list(uid, order, offset, limit));
        return setLogo(clientService.search(keywords, uid, order, offset, limit));
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @DeleteMapping("users/{uid}/clients")
    @Operation(summary = "删除用户应用", description = "应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。")
    public void removeUserClients(@PathVariable("uid") Long uid,
                                  @RequestParam Collection<String> cids) {
        clientService.delete(uid, cids);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('READ_CLIENT')) and #oauth2.clientHasAnyRole('READ_CLIENT')")
    @GetMapping("users/{uid}/clients/{cid}")
    @Operation(summary = "获取用户应用", description = "应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。")
    public Client getUserClient(@PathVariable("uid") Long uid,
                                @PathVariable("cid") String cid) {
        DefaultClient result = clientService.loadClientByClientId(cid);
        if (result == null || !clientService.isOwnerOrMember(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        return setLogo(result);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @DeleteMapping("users/{uid}/clients/{cid}")
    @Operation(summary = "删除用户应用", description = "应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。")
    public void removeUserClient(@PathVariable("uid") Long uid,
                                 @PathVariable("cid") String cid) {
        clientService.delete(uid, Arrays.asList(cid));
        try {
            storageHandler.remove(generateLogoKey(cid));
        } catch (Exception e) {
            ErrorEnum.DELETE_CLIENT_LOGO_FAIL.details(e.getMessage());
        }
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) and hasAuthority('CREATE_CLIENT') or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PostMapping("users/{uid}/clients")
    @Operation(summary = "创建用户应用", description = "应用和用户需要 WRITE_CLIENT 权限，或者应用拥有 WRITE_CLIENT 权限且 uid 为当前用户并拥有 CREATE_CLIENT 权限）")
    public Client createUserClient(@PathVariable("uid") Long uid,
                                   @RequestParam String name,
                                   @RequestParam String description,
                                   @RequestParam String redirectUri,
                                   @RequestParam(required = false) Collection<Long> scopes,
                                   @RequestParam(required = false) Collection<Long> grantTypes) {
        return setLogo(clientService.create(uid, name, description, redirectUri, scopes, grantTypes));
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("users/{uid}/clients/{cid}/secret")
    @Operation(summary = "更新用户应用密钥", description = "应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。")
    public String updateUserClientSecret(@PathVariable("uid") Long uid,
                                         @PathVariable("cid") String cid) {
        return clientService.updateSecret(cid, uid);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("users/{uid}/clients/{cid}/name")
    @Operation(summary = "更新用户应用名称", description = "应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。")
    public void updateUserClientName(@PathVariable("uid") Long uid,
                                     @PathVariable("cid") String cid,
                                     @RequestParam("name") String name) {
        clientService.updateName(cid, uid, name);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("users/{uid}/clients/{cid}/description")
    @Operation(summary = "更新用户应用描述", description = "应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。")
    public void updateUserClientDescription(@PathVariable("uid") Long uid,
                                            @PathVariable("cid") String cid,
                                            @RequestParam("description") String description) {
        clientService.updateDescription(cid, uid, description);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("users/{uid}/clients/{cid}/redirect")
    @Operation(summary = "更新用户应用回调地址", description = "应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。")
    public void updateUserClientRedirectUri(@PathVariable("uid") Long uid,
                                            @PathVariable("cid") String cid,
                                            @RequestParam("redirectUri") String redirectUri) {
        clientService.updateRedirectUri(cid, uid, redirectUri);
    }

    @GetMapping("users/{uid}/clients/{cid}/logo")
    @Operation(summary = "获取用户应用 Logo", description = "应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。")
    public void getUserClientLogo(@PathVariable("uid") Long uid,
                                  @PathVariable("cid") String cid) {
        if (!clientService.isOwnerOrMember(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        getClientLogo(cid);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("users/{uid}/clients/{cid}/logo")
    @Operation(summary = "更新用户应用 Logo", description = "应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。"
            , requestBody = @RequestBody(required = true, content = @Content(mediaType = "image/*", schema = @Schema(type = "string", format = "binary"))))
    public void updateUserClientLogo(@PathVariable("uid") Long uid,
                                     @PathVariable("cid") String cid) {
        if (!clientService.isOwnerOrMember(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        updateClientLogo(cid);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @PutMapping("users/{uid}/clients/{cid}/members")
    @Operation(summary = "添加应用成员", description = "应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。")
    public void addUserClientMembers(@PathVariable("uid") Long uid,
                                     @PathVariable("cid") String cid,
                                     @RequestParam("uids") Collection<Long> uids) {
        if (!clientService.isOwner(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        clientService.addMembers(cid, uids);
    }

    @PreAuthorize("(#oauth2.client or #user.matchUid(#uid) or hasAuthority('WRITE_CLIENT')) and #oauth2.clientHasAnyRole('WRITE_CLIENT')")
    @DeleteMapping("users/{uid}/clients/{cid}/members")
    @Operation(summary = "移除应用成员", description = "应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。")
    public void removeUserClientMembers(@PathVariable("uid") Long uid,
                                        @PathVariable("cid") String cid,
                                        @RequestParam("uids") Collection<Long> uids) {
        if (!clientService.isOwner(cid, uid))
            ErrorEnum.ACCESS_DENIED.throwException();
        clientService.removeMembers(cid, uids);
    }

    /* ----------------------------------------------------------------------------------------------------- */

    protected String generateLogoKey(Object id) {
        return String.format(Constants.CLIENT_LOGO_FORMAT, id);
    }

    public static <T extends DefaultClient> T setLogo(T client) {
        if (client == null)
            return null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String url = URI.create(attributes.getRequest().getRequestURL().toString())
                .resolve(Constants.API_ROOT + "/clients/" + client.getClientId() + "/logo")
                .toASCIIString();
        client.setLogo(url);
        return client;
    }

    public static <T extends DefaultClient, C extends Collection<T>> C setLogo(C clients) {
        if (clients == null)
            return null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        URI uri = URI.create(attributes.getRequest().getRequestURL().toString());
        for (T client : clients) {
            if (client == null)
                continue;
            String url = uri.resolve(Constants.API_ROOT + "/clients/" + client.getClientId() + "/logo").toASCIIString();
            client.setLogo(url);
        }
        return clients;
    }

    public static <T extends DefaultClient> QueryResults<T> setLogo(QueryResults<T> results) {
        if (results == null || results.getData() == null)
            return results;
        results.setData(setLogo(results.getData()));
        return results;
    }
}
