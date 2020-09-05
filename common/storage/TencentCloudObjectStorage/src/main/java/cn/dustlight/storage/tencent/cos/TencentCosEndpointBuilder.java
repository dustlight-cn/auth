package cn.dustlight.storage.tencent.cos;

import com.qcloud.cos.endpoint.EndpointBuilder;

public class TencentCosEndpointBuilder implements EndpointBuilder {

    private String generalApi;
    private String serviceApi;
    private EndpointBuilder proxy;

    public TencentCosEndpointBuilder(String generalApiEndpoint, String getServiceApiEndpoint, EndpointBuilder defaultProxy) {
        this.generalApi = generalApiEndpoint;
        this.serviceApi = getServiceApiEndpoint;
        this.proxy = defaultProxy;
    }

    @Override
    public String buildGeneralApiEndpoint(String s) {
        return generalApi != null ? generalApi : (proxy != null ? proxy.buildGeneralApiEndpoint(s) : null);
    }

    @Override
    public String buildGetServiceApiEndpoint() {
        return serviceApi != null ? serviceApi : (proxy != null ? proxy.buildGetServiceApiEndpoint() : null);
    }
}
