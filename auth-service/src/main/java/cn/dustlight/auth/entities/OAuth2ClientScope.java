package cn.dustlight.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OAuth2ClientScope extends DefaultClientScope {

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

    public static OAuth2ClientScope from(ClientScope clientScope) {
        OAuth2ClientScope scope = new OAuth2ClientScope();
        scope.setName(clientScope.getName());
        scope.setDescription(clientScope.getDescription());
        scope.setSubtitle(clientScope.getSubtitle());
        return scope;
    }
}
