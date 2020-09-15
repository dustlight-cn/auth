package cn.dustlight.oauth2.uim.entities.v1.authorities;

import java.io.Serializable;
import java.util.Date;

public interface AuthorityDetails extends Serializable {

    Long getAuthorityId();

    String getAuthorityName();

    String getAuthorityDescription();

    Date getCreatedAt();

    Date getUpdatedAt();
}
