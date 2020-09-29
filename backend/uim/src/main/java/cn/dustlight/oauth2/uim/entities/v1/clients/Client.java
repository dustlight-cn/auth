package cn.dustlight.oauth2.uim.entities.v1.clients;

import cn.dustlight.oauth2.uim.entities.v1.scopes.ClientScope;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Date;

public interface Client extends ClientDetails {

    String getClientName();

    String getClientDescription();

    @JsonSerialize(using = ToStringSerializer.class)
    Long getUid();

    Integer getStatus();

    Date getCreatedAt();

    Date getUpdatedAt();

    Collection<ClientScope> getScopes();
}
