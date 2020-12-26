package cn.dustlight.auth.controllers.resources;

import cn.dustlight.auth.entities.DefaultClient;
import cn.dustlight.auth.services.storages.StorageHandler;
import cn.dustlight.auth.util.Constants;
import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.Client;
import cn.dustlight.auth.services.ClientService;
import cn.dustlight.auth.util.QueryResults;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

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

    @PreAuthorize("hasAuthority('READ_CLIENT_ANY')")
    @GetMapping("clients")
    @Operation(summary = "查询应用")
    public QueryResults<? extends Client> getClients(@RequestParam(required = false, name = "q") String keywords, @RequestParam(required = false) Collection<String> order, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit) {
        if (keywords == null || keywords.length() == 0)
            return setLogo(clientService.list(order, offset, limit));
        return setLogo(clientService.search(keywords, order, offset, limit));
    }

    @PreAuthorize("hasAuthority('DELETE_CLIENT_ANY')")
    @DeleteMapping("clients")
    @Operation(summary = "删除应用")
    public void removeClients(@RequestParam Collection<String> cids) {
        clientService.delete(cids);
    }

    @PreAuthorize("hasAuthority('READ_CLIENT_ANY')")
    @GetMapping("clients/{cid}")
    @Operation(summary = "获取应用")
    public Client getClient(@PathVariable("cid") String cid) {
        return setLogo(clientService.loadClientByClientId(cid));
    }

    @PreAuthorize("hasAuthority('DELETE_CLIENT_ANY')")
    @DeleteMapping("clients/{cid}")
    @Operation(summary = "删除应用")
    public void removeClient(@PathVariable("cid") String cid) {
        clientService.delete(Arrays.asList(cid));
    }

    @PreAuthorize("hasAuthority('CREATE_CLIENT') and hasAuthority('WRITE_CLIENT_ANY')")
    @PostMapping("clients")
    @Operation(summary = "创建应用")
    public Client createClient(@RequestParam Long uid, @RequestParam String name, @RequestParam String description, @RequestParam String redirectUri, @RequestParam Collection<Long> scopes, @RequestParam Collection<Long> grantTypes, @RequestParam(required = false, defaultValue = "7200") Integer accessTokenValidity, @RequestParam(required = false, defaultValue = "86400") Integer refreshTokenValidity, @RequestParam(required = false) String additionalInformation, @RequestParam(required = false, defaultValue = "0") Integer status) {
        return setLogo(clientService.create(uid, name, description, redirectUri, scopes, grantTypes, accessTokenValidity, refreshTokenValidity, additionalInformation, status));
    }

    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("clients/{cid}/secret")
    @Operation(summary = "更新应用密钥")
    public String updateClientSecret(@PathVariable("cid") String cid) {
        return clientService.updateSecret(cid);
    }

    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("clients/{cid}/name")
    @Operation(summary = "更新应用名称")
    public void updateClientName(@PathVariable("cid") String cid, @RequestParam("name") String name) {
        clientService.updateName(cid, name);
    }

    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("clients/{cid}/description")
    @Operation(summary = "更新应用描述")
    public void updateClientDescription(@PathVariable("cid") String cid, @RequestParam("description") String description) {
        clientService.updateDescription(cid, description);
    }

    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("clients/{cid}/redirect")
    @Operation(summary = "更新应用回调地址")
    public void updateClientRedirectUri(@PathVariable("cid") String cid, @RequestParam("redirectUri") String redirectUri) {
        clientService.updateRedirectUri(cid, redirectUri);
    }

    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("clients/{cid}/status")
    @Operation(summary = "更新应用状态")
    public void updateClientStatus(@PathVariable("cid") String cid, @RequestParam("status") Integer status) {
        clientService.updateStatus(cid, status);
    }

    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("clients/{cid}/access-token-validity")
    @Operation(summary = "更新应用AccessToken有效期")
    public void updateClientAccessTokenValidity(@PathVariable("cid") String cid, @RequestParam("accessTokenValidity") Integer accessTokenValidity) {
        clientService.updateAccessTokenValidity(cid, accessTokenValidity);
    }

    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("clients/{cid}/refresh-token-validity")
    @Operation(summary = "更新应用RefreshToken有效期")
    public void updateClientRefreshTokenValidity(@PathVariable("cid") String cid, @RequestParam("refreshTokenValidity") Integer refreshTokenValidity) {
        clientService.updateRefreshTokenValidity(cid, refreshTokenValidity);
    }

    @GetMapping("clients/{cid}/logo")
    @Operation(summary = "获取应用Logo")
    public void getClientLogo(@PathVariable("cid") String cid) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String key = generateLogoKey(cid);
            storageHandler.handle(key, attributes.getRequest(), attributes.getResponse(), "image/*");
        } catch (IOException e) {
            ErrorEnum.RESOURCE_NOT_FOUND.details(e.getMessage()).throwException();
        }
    }

    @PreAuthorize("hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("clients/{cid}/logo")
    @Operation(summary = "更新应用Logo", requestBody = @RequestBody(required = true, content = @Content(mediaType = "image/*", schema = @Schema(type = "string", format = "binary"))))
    public void updateClientLogo(@PathVariable("cid") String cid) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String key = generateLogoKey(cid);
            storageHandler.handle(key, attributes.getRequest(), attributes.getResponse());
        } catch (IOException e) {
            ErrorEnum.UPDATE_CLIENT_FAIL.details(e.getMessage()).throwException();
        }
    }

    @PreAuthorize("#user.matchUid(#uid) or hasAuthority('READ_CLIENT_ANY')")
    @GetMapping("users/{uid}/clients")
    @Operation(summary = "查询应用")
    public QueryResults<? extends Client> getClients(@PathVariable("uid") Long uid, @RequestParam(required = false, name = "q") String keywords, @RequestParam(required = false) Collection<String> order, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit) {
        if (keywords == null || keywords.length() == 0)
            return setLogo(clientService.list(uid, order, offset, limit));
        return setLogo(clientService.search(keywords, uid, order, offset, limit));
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('DELETE_CLIENT_ANY')")
    @DeleteMapping("users/{uid}/clients")
    @Operation(summary = "删除应用")
    public void removeClients(@PathVariable("uid") Long uid, @RequestParam Collection<String> cids) {
        clientService.delete(uid, cids);
    }

    @PreAuthorize("#user.matchUid(#uid) or hasAuthority('READ_CLIENT_ANY')")
    @GetMapping("users/{uid}/clients/{cid}")
    @Operation(summary = "获取应用")
    public Client getClient(@PathVariable("uid") Long uid, @PathVariable("cid") String cid) {
        DefaultClient result = clientService.loadClientByClientId(cid);
        if (result == null || !uid.equals(result.getUid()))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        return setLogo(result);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('DELETE_CLIENT_ANY')")
    @DeleteMapping("users/{uid}/clients/{cid}")
    @Operation(summary = "删除应用")
    public void removeClient(@PathVariable("uid") Long uid, @PathVariable("cid") String cid) {
        clientService.delete(uid, Arrays.asList(cid));
    }

    @PreAuthorize("(#user.matchUid(#uid) or hasAuthority('WRITE_CLIENT_ANY')) and hasAuthority('CREATE_CLIENT')")
    @PostMapping("users/{uid}/clients")
    @Operation(summary = "创建应用")
    public Client createClient(@PathVariable("uid") Long uid, @RequestParam String name, @RequestParam String description, @RequestParam String redirectUri, @RequestParam Collection<Long> scopes, @RequestParam Collection<Long> grantTypes) {
        return setLogo(clientService.create(uid, name, description, redirectUri, scopes, grantTypes));
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("users/{uid}/clients/{cid}/secret")
    @Operation(summary = "更新应用密钥")
    public String updateClientSecret(@PathVariable("uid") Long uid, @PathVariable("cid") String cid) {
        return clientService.updateSecret(cid, uid);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("users/{uid}/clients/{cid}/name")
    @Operation(summary = "更新应用名称")
    public void updateClientName(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("name") String name) {
        clientService.updateName(cid, uid, name);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("users/{uid}/clients/{cid}/description")
    @Operation(summary = "更新应用描述")
    public void updateClientDescription(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("description") String description) {
        clientService.updateDescription(cid, uid, description);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("users/{uid}/clients/{cid}/redirect")
    @Operation(summary = "更新应用回调地址")
    public void updateClientRedirectUri(@PathVariable("uid") Long uid, @PathVariable("cid") String cid, @RequestParam("redirectUri") String redirectUri) {
        clientService.updateRedirectUri(cid, uid, redirectUri);
    }

    @GetMapping("users/{uid}/clients/{cid}/logo")
    @Operation(summary = "获取应用Logo")
    public void getClientLogo(@PathVariable("uid") Long uid, @PathVariable("cid") String cid) {
        if (!clientService.isOwner(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        getClientLogo(cid);
    }

    @PreAuthorize("#user.matchUid(#uid) and hasAuthority('WRITE_CLIENT') or hasAuthority('WRITE_CLIENT_ANY')")
    @PutMapping("users/{uid}/clients/{cid}/logo")
    @Operation(summary = "更新应用Logo", requestBody = @RequestBody(required = true, content = @Content(mediaType = "image/*", schema = @Schema(type = "string", format = "binary"))))
    public void updateClientLogo(@PathVariable("uid") Long uid, @PathVariable("cid") String cid) {
        if (!clientService.isOwner(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        updateClientLogo(cid);
    }

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
