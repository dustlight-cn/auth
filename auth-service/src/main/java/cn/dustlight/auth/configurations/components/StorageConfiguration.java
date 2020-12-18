package cn.dustlight.auth.configurations.components;

import cn.dustlight.auth.util.CdnStorageProxy;
import cn.dustlight.storage.core.RestfulStorage;
import cn.dustlight.storage.tencent.cos.TencentCloudObjectStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(StorageConfiguration.StorageProperties.class)
public class StorageConfiguration {

    @Bean("authStorage")
    @ConditionalOnMissingBean(name = "authStorage")
    public RestfulStorage authStorage(@Autowired TencentCloudObjectStorage tencentCloudObjectStorage,
                                      @Autowired StorageProperties properties) {
        CdnStorageProxy storage = new CdnStorageProxy(tencentCloudObjectStorage, properties.getBaseUrl());
        storage.setPrefix(properties.getPrefix());
        return storage;
    }

    @ConfigurationProperties("dustlight.auth.storage")
    public static class StorageProperties {
        private String baseUrl = null;
        private String prefix = "auth/upload/";
        private Long defaultExpiration = 1000L * 60 * 15L;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public Long getDefaultExpiration() {
            return defaultExpiration;
        }

        public void setDefaultExpiration(Long defaultExpiration) {
            this.defaultExpiration = defaultExpiration;
        }
    }
}
