package cn.dustlight.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

public interface ClientScope extends Scope, Serializable {

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
