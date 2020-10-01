package cn.dustlight.oauth2.uim.controllers.clients;

import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.oauth2.uim.entities.results.QueryResults;
import cn.dustlight.oauth2.uim.entities.v1.clients.Client;
import cn.dustlight.oauth2.uim.services.clients.ClientService;
import cn.dustlight.storage.core.Permission;
import cn.dustlight.storage.core.RestfulStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RestController
public class DefaultClientController implements ClientController {

    @Autowired
    private ClientService clientService;

    @Qualifier("cdnStorage")
    @Autowired
    private RestfulStorage storage;

    @Override
    public QueryResults<? extends Client, ? extends Number> getClients(String keywords, Collection<String> order, Integer offset, Integer limit) {
        if (keywords == null || keywords.length() == 0)
            return clientService.list(order, offset, limit);
        return clientService.search(keywords, order, offset, limit);
    }

    @Override
    public void removeClients(Collection<String> cids) {
        clientService.delete(cids);
    }

    @Override
    public Client getClient(String cid) {
        return clientService.loadClientByClientId(cid);
    }

    @Override
    public void removeClient(String cid) {
        clientService.delete(Arrays.asList(cid));
    }

    @Override
    public Client createClient(Long uid, String name, String description, String redirectUri, Collection<Long> scopes, Collection<Long> grantTypes, Integer accessTokenValidity, Integer refreshTokenValidity, String additionalInformation, Integer status) {
        return clientService.create(uid, name, description, redirectUri, scopes, grantTypes, accessTokenValidity, refreshTokenValidity, additionalInformation, status);
    }

    @Override
    public String updateClientSecret(String cid) {
        return clientService.updateSecret(cid);
    }

    @Override
    public void updateClientName(String cid, String name) {
        clientService.updateName(cid, name);
    }

    @Override
    public void updateClientDescription(String cid, String description) {
        clientService.updateDescription(cid, description);
    }

    @Override
    public void updateClientRedirectUri(String cid, String redirectUri) {
        clientService.updateRedirectUri(cid, redirectUri);
    }

    @Override
    public void updateClientStatus(String cid, Integer status) {
        clientService.updateStatus(cid, status);
    }

    @Override
    public void updateClientAccessTokenValidity(String cid, Integer accessTokenValidity) {
        clientService.updateAccessTokenValidity(cid, accessTokenValidity);
    }

    @Override
    public void updateClientRefreshTokenValidity(String cid, Integer refreshTokenValidity) {
        clientService.updateRefreshTokenValidity(cid, refreshTokenValidity);
    }

    @Override
    public void getClientLogo(String cid) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();

            String key = generateLogoKey(cid);
            if (!storage.isExist(key))
                ErrorEnum.RESOURCE_NOT_FOUND.throwException();
            String redirectUrl = storage.generateGetUrl(key, 1000 * 60 * 60L); // 生成下载URL
            response.setStatus(HttpStatus.FOUND.value());
            response.setHeader("Location", redirectUrl);
        } catch (IOException e) {
            ErrorEnum.RESOURCE_NOT_FOUND.details(e.getMessage()).throwException();
        }
    }

    @Override
    public void updateClientLogo(String cid) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();

            String key = generateLogoKey(cid);
            String redirectUrl = storage.generatePutUrl(key, Permission.WRITABLE, 1000 * 60 * 60L); // 生成签名上传URL
            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
            response.setHeader("Location", redirectUrl);
        } catch (IOException e) {
            ErrorEnum.UPDATE_CLIENT_FAIL.details(e.getMessage()).throwException();
        }
    }

    @Override
    public QueryResults<? extends Client, ? extends Number> getClients(Long uid, String keywords, Collection<String> order, Integer offset, Integer limit) {
        if (keywords == null || keywords.length() == 0)
            return clientService.list(uid, order, offset, limit);
        return clientService.search(keywords, uid, order, offset, limit);
    }

    @Override
    public void removeClients(Long uid, Collection<String> cids) {
        clientService.delete(uid, cids);
    }

    @Override
    public Client getClient(Long uid, String cid) {
        Client result = clientService.loadClientByClientId(cid);
        if (result == null || !uid.equals(result.getUid()))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        return result;
    }

    @Override
    public void removeClient(Long uid, String cid) {
        clientService.delete(uid, Arrays.asList(cid));
    }

    @Override
    public Client createClient(Long uid, String name, String description, String redirectUri, Collection<Long> scopes, Collection<Long> grantTypes) {
        return clientService.create(uid, name, description, redirectUri, scopes, grantTypes);
    }

    @Override
    public String updateClientSecret(Long uid, String cid) {
        return clientService.updateSecret(cid, uid);
    }

    @Override
    public void updateClientName(Long uid, String cid, String name) {
        clientService.updateName(cid, uid, name);
    }

    @Override
    public void updateClientDescription(Long uid, String cid, String description) {
        clientService.updateDescription(cid, uid, description);
    }

    @Override
    public void updateClientRedirectUri(Long uid, String cid, String redirectUri) {
        clientService.updateRedirectUri(cid, uid, redirectUri);
    }

    @Override
    public void getClientLogo(Long uid, String cid) {
        if (!clientService.isOwner(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        getClientLogo(cid);
    }

    @Override
    public void updateClientLogo(Long uid, String cid) {
        if (!clientService.isOwner(cid, uid))
            ErrorEnum.CLIENT_NOT_FOUND.throwException();
        updateClientLogo(cid);
    }


    protected String generateLogoKey(Object id) {
        return String.format("clients/%s/logo", id);
    }
}
