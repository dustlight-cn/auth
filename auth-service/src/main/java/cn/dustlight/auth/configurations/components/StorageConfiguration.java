package cn.dustlight.auth.configurations.components;

import cn.dustlight.auth.services.storages.StorageHandler;
import cn.dustlight.auth.services.storages.CdnStorageProxy;
import cn.dustlight.storage.core.RestfulStorage;
import cn.dustlight.storage.core.Storage;
import cn.dustlight.storage.local.LocalStorage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.HashSet;

@EnableConfigurationProperties(StorageConfiguration.StorageProperties.class)
public class StorageConfiguration {

    private Log logger = LogFactory.getLog(StorageConfiguration.class.getName());

    @Bean
    @ConditionalOnProperty(name = "dustlight.auth.storage.storage-type",
            havingValue = "local", matchIfMissing = true)
    public StorageHandler localStorageHandler(@Autowired BeanFactory beanFactory,
                                              @Autowired StorageProperties properties) {
        Collection<Storage> storages = new HashSet<>();

        storages.add(LocalStorage.from((properties.baseUrl != null ? properties.baseUrl : "") +
                (properties.prefix != null ? properties.prefix : "")));

        StorageHandler handler = new StorageHandler(storages);
        handler.setExpiration(properties.getDefaultExpiration());
        logger.info("Storage Mode: LOCAL");
        return handler;
    }

    @Bean
    @ConditionalOnProperty(name = "dustlight.auth.storage.storage-type",
            havingValue = "storage")
    public StorageHandler storageHandler(@Autowired BeanFactory beanFactory,
                                         @Autowired StorageProperties properties,
                                         @Autowired Storage storage) {
        Collection<Storage> storages = new HashSet<>();
        if (storage instanceof RestfulStorage) {
            RestfulStorage restfulStorage = (RestfulStorage) storage;
            CdnStorageProxy proxy = new CdnStorageProxy(restfulStorage, properties.getBaseUrl());
            proxy.setPrefix(properties.getPrefix());
            proxy.setSimpleUrl(properties.isSimpleMode());
            storages.add(proxy);
        } else {
            storages.add(storage);
        }
        StorageHandler handler = new StorageHandler(storages);
        handler.setExpiration(properties.getDefaultExpiration());
        logger.info("Storage Mode: STORAGE");
        return handler;
    }

    @ConfigurationProperties("dustlight.auth.storage")
    public static class StorageProperties {

        private StorageType storageType = StorageType.LOCAL;
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

        public StorageType getStorageType() {
            return storageType;
        }

        public void setStorageType(StorageType storageType) {
            this.storageType = storageType;
        }

        public enum StorageType {
            LOCAL,
            STORAGE
        }
    }
}
