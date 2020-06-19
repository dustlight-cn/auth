package cn.dustlight.uim.models;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class ClientDetails extends BaseClientDetails implements IClientDetails {

    private Long uid;

    @JsonProperty("client_name")
    @com.fasterxml.jackson.annotation.JsonProperty("client_name")
    private String clientName;

    @Override
    public Long getUid() {
        return uid;
    }

    @Override
    public String getClientName() {
        return clientName;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
