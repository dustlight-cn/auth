package cn.dustlight.uim.models;

import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Date;
import java.util.Map;

public interface IClientDetails extends ClientDetails {

    Long getUid();

    String getClientName();

    Map<String, String> getScopeDescriptions();

    boolean isEnabled();

    String getDescription();

    Date getCreatedAt();

    Date getUpdatedAt();
}
