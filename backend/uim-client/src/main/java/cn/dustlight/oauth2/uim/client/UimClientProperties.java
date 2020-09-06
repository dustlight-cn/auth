package cn.dustlight.oauth2.uim.client;

import cn.dustlight.oauth2.uim.client.converter.IUimUserConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "dustlight.uim-client")
public class UimClientProperties {

    private Map<String, List<String>> userDataPath = new LinkedHashMap<>();
    private Map<String, Class<? extends IUimUserConverter>> customConverter = new LinkedHashMap<>();

    public Map<String, Class<? extends IUimUserConverter>> getCustomConverter() {
        return customConverter;
    }

    public void setCustomConverter(Map<String, Class<? extends IUimUserConverter>> customConverter) {
        this.customConverter = customConverter;
    }

    public Map<String, List<String>> getUserDataPath() {
        return userDataPath;
    }

    public void setUserDataPath(Map<String, List<String>> userDataPath) {
        this.userDataPath = userDataPath;
    }
}
