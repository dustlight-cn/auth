package cn.dustlight.oauth2.uim.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Date;
import java.util.Map;

public interface IClientDetails extends ClientDetails {

    @JsonSerialize(using = ToStringSerializer.class)
    Long getUid();

    String getClientName();

    Map<String, IScopeDetails> getScopeDetails();

    Map<String, IGrantType> getGrantTypeDetails();

    boolean isEnabled();

    String getDescription();

    Date getCreatedAt();

    Date getUpdatedAt();
}
