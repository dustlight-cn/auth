package cn.dustlight.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class AuthorizationResponse implements Serializable {

    private AuthorizationClient client;
    private PublicUser owner;
    private String redirect;
    private Long count;
    private boolean approved;

    public AuthorizationClient getClient() {
        return client;
    }

    public void setClient(AuthorizationClient client) {
        this.client = client;
    }

    public PublicUser getOwner() {
        return owner;
    }

    public void setOwner(PublicUser owner) {
        this.owner = owner;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public static class AuthorizationClientScope extends DefaultClientScope {

        private boolean approved;

        @JsonIgnore
        @Override
        public boolean isAutoApprove() {
            return super.isAutoApprove();
        }

        @JsonIgnore
        @Override
        public Long getSid() {
            return super.getSid();
        }

        public boolean isApproved() {
            return approved;
        }

        public void setApproved(boolean approved) {
            this.approved = approved;
        }

        public static AuthorizationClientScope from(ClientScope clientScope) {
            AuthorizationClientScope scope = new AuthorizationClientScope();
            scope.setName(clientScope.getName());
            scope.setDescription(clientScope.getDescription());
            scope.setSubtitle(clientScope.getSubtitle());
            return scope;
        }
    }

    public static class AuthorizationClient extends DefaultClient {

        @JsonIgnore
        @Override
        public Long getUid() {
            return super.getUid();
        }

        @JsonIgnore
        @Override
        public Set<String> getAuthorizedGrantTypes() {
            return super.getAuthorizedGrantTypes();
        }

        @JsonIgnore
        @Override
        public Set<String> getResourceIds() {
            return super.getResourceIds();
        }

        @JsonIgnore
        @Override
        public Set<String> getRegisteredRedirectUri() {
            return super.getRegisteredRedirectUri();
        }

        @JsonIgnore
        @Override
        public String getClientSecret() {
            return super.getClientSecret();
        }

        @JsonIgnore
        @Override
        public Integer getAccessTokenValiditySeconds() {
            return super.getAccessTokenValiditySeconds();
        }

        @JsonIgnore
        @Override
        public Integer getRefreshTokenValiditySeconds() {
            return super.getRefreshTokenValiditySeconds();
        }

        @JsonIgnore
        @Override
        public Map<String, Object> getAdditionalInformation() {
            return super.getAdditionalInformation();
        }

        @JsonIgnore
        @Override
        public Collection<GrantedAuthority> getAuthorities() {
            return super.getAuthorities();
        }

        @ArraySchema(schema = @Schema(implementation = AuthorizationClientScope.class))
        @Override
        public Collection<? extends ClientScope> getScopes() {
            return super.getScopes();
        }
    }
}
