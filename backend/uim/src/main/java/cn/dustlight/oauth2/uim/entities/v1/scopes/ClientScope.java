package cn.dustlight.oauth2.uim.entities.v1.scopes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public interface ClientScope extends Scope {

    @JsonIgnore
    Long getCid();

    boolean isAutoApprove();

    @JsonIgnore
    @Override
    Date getCreatedAt();

    @JsonIgnore
    @Override
    Date getUpdatedAt();
}
