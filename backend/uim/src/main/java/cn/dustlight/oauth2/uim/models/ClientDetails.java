package cn.dustlight.oauth2.uim.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientDetails implements IClientDetails {

    private Long uid;
    private String clientName;
    private String clientId;
    private String clientSecret;
    private List<String> resourceIds;
    private List<ClientScope> scope;
    private List<IGrantType> authorizedGrantTypes;
    private String redirectUri;
    private String[] authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private boolean enabled;
    private String description;
    private Date createdAt;
    private Date updatedAt;

    @Override
    public Long getUid() {
        return uid;
    }

    @Override
    public String getClientName() {
        return clientName;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        if (resourceIds == null)
            return null;
        return new HashSet<>(resourceIds);
    }

    @Override
    public boolean isSecretRequired() {
        return clientSecret != null && clientSecret.length() > 0;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        return scope != null && !scope.isEmpty();
    }

    @Override
    public Set<String> getScope() {
        if (scope == null)
            return new HashSet<>();
        Set<String> set = new HashSet<>();
        for (ClientScope s : scope) {
            set.add(s.getScopeName());
        }
        return set;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        HashSet<String> tmp = new HashSet<>();
        if (authorizedGrantTypes == null)
            return tmp;
        for (IGrantType t : authorizedGrantTypes)
            tmp.add(t.getName());
        return tmp;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        if (!StringUtils.hasText(redirectUri))
            return null;
        Set<String> uriSet = StringUtils.commaDelimitedListToSet(redirectUri);
        Set<String> trimSet = new LinkedHashSet<>();
        for (String uri : uriSet) {
            if (uri == null)
                continue;
            trimSet.add(uri.trim());
        }
        return trimSet;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (authorities == null)
            return null;
        return AuthorityUtils.createAuthorityList(authorities);
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
        if (scope == null)
            return false;
        for (ClientScope sc : scope) {
            if (sc.getScopeName().equals(s))
                return sc.isAutoApprove();
        }
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        if (additionalInformation == null)
            return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(additionalInformation, HashMap.class);
        } catch (JsonProcessingException e) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Map<String, IScopeDetails> getScopeDetails() {
        Map<String, IScopeDetails> map = new LinkedHashMap<>();
        for (ClientScope s : scope) {
            ScopeDetails details = new ScopeDetails();
            details.setDescription(s.getScopeDescription());
            details.setName(s.getScopeName());
            details.setId(s.getId());
            map.put(s.getScopeName(), details);
        }
        return map;
    }

    @Override
    public Map<String, IGrantType> getGrantTypeDetails() {
        LinkedHashMap result = new LinkedHashMap();
        if (authorizedGrantTypes == null)
            return result;
        for (IGrantType t : authorizedGrantTypes)
            result.put(t.getName(), t);
        return result;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public static class ClientScope implements Serializable {

        private Long id;
        private String scopeName;
        private String scopeDescription;
        private boolean autoApprove;

        public Long getId() {
            return id;
        }

        public String getScopeName() {
            return scopeName;
        }

        public boolean isAutoApprove() {
            return autoApprove;
        }

        public String getScopeDescription() {
            return scopeDescription;
        }
    }
}
