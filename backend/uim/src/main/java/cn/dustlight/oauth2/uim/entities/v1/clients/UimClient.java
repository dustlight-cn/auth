package cn.dustlight.oauth2.uim.entities.v1.clients;

import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Date;

public interface UimClient extends ClientDetails {

    String getClientName();

    String getClientDescription();

    Long getUid();

    Integer getStatus();

    Date getCreatedAt();

    Date getUpdatedAt();
}
