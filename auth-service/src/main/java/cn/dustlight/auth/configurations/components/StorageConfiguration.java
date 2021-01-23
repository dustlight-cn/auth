package cn.dustlight.auth.configurations.components;

import cn.dustlight.auth.services.storages.StorageHandler;
import cn.dustlight.auth.services.storages.CdnStorageProxy;
import cn.dustlight.storage.core.RestfulStorage;
import cn.dustlight.storage.core.Storage;
import cn.dustlight.storage.local.LocalStorage;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.util.Collection;
import java.util.HashSet;

@EnableConfigurationProperties(StorageConfiguration.StorageProperties.class)
public class StorageConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Order()
    public StorageHandler storageHandler(@Autowired BeanFactory beanFactory,
                                         @Autowired StorageProperties properties,
                                         @Autowired(required = false) RestfulStorage restfulStorage) {
        Collection<Storage> storages = new HashSet<>();
        if (restfulStorage != null) {
            CdnStorageProxy storage = new CdnStorageProxy(restfulStorage, properties.getBaseUrl());
            storage.setPrefix(properties.getPrefix());
            storage.setSimpleUrl(properties.isSimpleMode());
            storages.add(storage);
        }
        storages.add(LocalStorage.from((properties.baseUrl != null ? properties.baseUrl : "") +
                (properties.prefix != null ? properties.prefix : "")));
        StorageHandler handler = new StorageHandler(storages);
        handler.setExpiration(properties.getDefaultExpiration());
        return handler;
    }

    @ConfigurationProperties("dustlight.auth.storage")
    public static class StorageProperties {

        private boolean simpleMode = true;
        private String baseUrl = null;
        private String prefix = "upload/auth/";
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

        public boolean isSimpleMode() {
            return simpleMode;
        }

        public void setSimpleMode(boolean simpleMode) {
            this.simpleMode = simpleMode;
        }
    }
}
