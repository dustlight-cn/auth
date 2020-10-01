package cn.dustlight.oauth2.uim.entities.v1.clients;

import cn.dustlight.oauth2.uim.entities.v1.authorities.Authority;
import cn.dustlight.oauth2.uim.entities.v1.resources.Resource;
import cn.dustlight.oauth2.uim.entities.v1.scopes.ClientScope;
import cn.dustlight.oauth2.uim.entities.v1.types.GrantType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.*;

@Schema(name = "Client")
public class DefaultClient implements Client {

    private static final Log logger = LogFactory.getLog(DefaultClient.class.getName());

    private String cid, secret, name, description, logo, redirectUri, additionalInformation;
    private Date createdAt, updatedAt;
    private Integer accessTokenValidity, refreshTokenValidity, status;
    private Long uid;
    private Collection<Resource> resources;
    private Collection<ClientScope> scopes;
    private Collection<GrantType> types;
    private Collection<Authority> authorities;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getLogo() {
        return logo;
    }

    @Override
    public Long getUid() {
        return uid;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public Collection<ClientScope> getScopes() {
        return scopes;
    }

    @Override
    public String getClientId() {
        return cid;
    }

    @Override
    public Set<String> getResourceIds() {
        if (resources == null || resources.size() == 0)
            return null;
        Set<String> resourceIds = new LinkedHashSet<>(resources.size());
        for (Resource resource : resources)
            resourceIds.add(resource.getName());
        return resourceIds;
    }

    @Override
    public boolean isSecretRequired() {
        return secret != null && secret.length() > 0;
    }

    @Override
    public String getClientSecret() {
        return secret;
    }

    @Override
    public boolean isScoped() {
        return scopes != null && scopes.size() > 0;
    }

    @Override
    public Set<String> getScope() {
        if (scopes == null || scopes.size() == 0)
            return null;
        Set<String> scopeNames = new LinkedHashSet<>(scopes.size());
        for (ClientScope scope : scopes)
            scopeNames.add(scope.getName());
        return scopeNames;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        if (types == null || types.size() == 0)
            return Collections.emptySet();
        Set<String> grantTypes = new LinkedHashSet<>(types.size());
        for (GrantType type : types)
            grantTypes.add(type.getName());
        return grantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        if (redirectUri == null)
            return Collections.emptySet();
        return StringUtils.commaDelimitedListToSet(redirectUri);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (authorities == null || authorities.size() == 0)
            return Collections.emptySet();
        Collection<GrantedAuthority> result = new LinkedHashSet<>(authorities.size());
        authorities.forEach(authority -> {
            result.add(new SimpleGrantedAuthority(authority.getAuthorityName()));
        });
        return null;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValidity;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValidity;
    }

    @Override
    public boolean isAutoApprove(String s) {
        if (scopes == null || scopes.size() == 0)
            return false;
        for (ClientScope scope : scopes)
            if (scope.getName().equals(s))
                return scope.isAutoApprove();
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        if (additionalInformation == null || mapper == null)
            return null;
        try {
            return mapper.readValue(additionalInformation, LinkedHashMap.class);
        } catch (JsonProcessingException e) {
            logger.warn("Get additional information error", e);
        }
        return null;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @JsonIgnore
    public ObjectMapper getMapper() {
        return mapper;
    }

    public void setClientId(String cid) {
        this.cid = cid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegisteredRedirectUri(Set<String> redirectUri) {
        if (redirectUri == null || redirectUri.size() == 0) {
            this.redirectUri = null;
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (String uri : redirectUri) {
            if (builder.length() != 0)
                builder.append(',');
            builder.append(uri);
        }
        this.redirectUri = builder.toString();
    }

    public void setAccessTokenValiditySeconds(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public void setRefreshTokenValiditySeconds(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setClientSecret(String secret) {
        this.secret = secret;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        if (additionalInformation == null) {
            this.additionalInformation = null;
        }
        if (mapper == null)
            logger.warn("Set additional information fail: ObjectMapper is null");
        else {
            try {
                this.additionalInformation = mapper.writeValueAsString(additionalInformation);
            } catch (JsonProcessingException e) {
                logger.warn("Set additional information error", e);
            }
        }
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
