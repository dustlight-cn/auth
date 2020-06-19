package cn.dustlight.uim.models;

import org.springframework.security.oauth2.provider.ClientDetails;

public interface IClientDetails extends ClientDetails {

    Long getUid();

    String getClientName();

}
