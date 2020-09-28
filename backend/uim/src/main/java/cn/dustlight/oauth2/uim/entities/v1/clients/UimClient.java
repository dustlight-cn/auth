package cn.dustlight.oauth2.uim.entities.v1.clients;

import cn.dustlight.oauth2.uim.entities.v1.scopes.ClientScope;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Date;

public interface UimClient extends ClientDetails {

    String getClientName();

    String getClientDescription();

    Long getUid();

    Integer getStatus();

    Date getCreatedAt();

    Date getUpdatedAt();

    Collection<ClientScope> getScopes();
}
