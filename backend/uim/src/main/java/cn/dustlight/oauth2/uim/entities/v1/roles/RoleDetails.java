package cn.dustlight.oauth2.uim.entities.v1.roles;

import java.io.Serializable;
import java.util.Date;

public interface RoleDetails extends Serializable {

    Long getRoleId();

    String getRoleName();

    String getRoleDescription();

    Date getCreatedAt();

    Date getUpdatedAt();
}
