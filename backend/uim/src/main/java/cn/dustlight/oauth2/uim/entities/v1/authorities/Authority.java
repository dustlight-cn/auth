package cn.dustlight.oauth2.uim.entities.v1.authorities;

import java.io.Serializable;
import java.util.Date;

public interface Authority extends Serializable {

    Long getAuthorityId();

    String getAuthorityName();

    String getAuthorityDescription();

    Date getCreatedAt();

    Date getUpdatedAt();
}
