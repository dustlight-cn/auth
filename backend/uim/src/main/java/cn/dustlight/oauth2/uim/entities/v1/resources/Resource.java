package cn.dustlight.oauth2.uim.entities.v1.resources;

import java.util.Date;

public interface Resource {

    String getRid();

    String getName();

    Date getCreatedAt();

    Date getUpdatedAt();
}
