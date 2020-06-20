package cn.dustlight.uim.models;

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
    private List<String> authorizedGrantTypes;
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
            return null;
        Set<String> set = new HashSet<>();
        for (ClientScope s : scope) {
            set.add(s.getScopeName());
        }
        return set;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        if (authorizedGrantTypes == null)
            return null;
        return new HashSet<>(authorizedGrantTypes);
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        if (!StringUtils.hasText(redirectUri))
            return null;
        return StringUtils.commaDelimitedListToSet(redirectUri);
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

    public boolean isEnabled() {
        return enabled;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
//
//    public Map<String, List<GrantedAuthority>> getScopeAuthorities() {
//        if (scope == null || scope.isEmpty())
//            return null;
//        Map<String, List<GrantedAuthority>> scopeAuthorities = new LinkedHashMap<>();
//        for (ClientScope s : scope) {
//
//            try {
//                Logger.getLogger("???").info(new ObjectMapper().writeValueAsString(s));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//            scopeAuthorities.put(s.getScopeName(), s.getAuthorities());
//        }
//        return scopeAuthorities;
//    }

    public static class ClientScope implements Serializable {

        private String scopeName;
        private boolean autoApprove;
//        private String[] authorities;

        public String getScopeName() {
            return scopeName;
        }

        public boolean isAutoApprove() {
            return autoApprove;
        }
//
//        public List<GrantedAuthority> getAuthorities() {
//            if (authorities == null || authorities.length == 0)
//                return null;
//            return AuthorityUtils.createAuthorityList(authorities);
//        }
    }
}
