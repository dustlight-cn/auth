package cn.dustlight.oauth2.uim.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dustlight.uim")
public class Properties {

    private String[] publicPaths = {};
    private String[] resourcePaths = {};

    private SnowflakeConfiguration snowflake = new SnowflakeConfiguration();
    private boolean csrfEnabled = false;

    private Storage storage;

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public String[] getResourcePaths() {
        return resourcePaths;
    }

    public void setResourcePaths(String[] resourcePaths) {
        this.resourcePaths = resourcePaths;
    }

    public String[] getPublicPaths() {
        return publicPaths;
    }

    public void setPublicPaths(String[] publicPaths) {
        this.publicPaths = publicPaths;
    }

    public boolean isCsrfEnabled() {
        return csrfEnabled;
    }

    public void setCsrfEnabled(boolean csrfEnabled) {
        this.csrfEnabled = csrfEnabled;
    }

    public SnowflakeConfiguration getSnowflake() {
        return snowflake;
    }

    public void setSnowflake(SnowflakeConfiguration snowflake) {
        this.snowflake = snowflake;
    }

    public static class Storage {
        private String baseUrl = null;
        private String prefix = "uim/upload/";
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

    public static class SnowflakeConfiguration {
        public Long machineId, dataCenterId, startTimestamp;

        public Long getMachineId() {
            return machineId;
        }

        public void setMachineId(Long machineId) {
            this.machineId = machineId;
        }

        public Long getDataCenterId() {
            return dataCenterId;
        }

        public void setDataCenterId(Long dataCenterId) {
            this.dataCenterId = dataCenterId;
        }

        public Long getStartTimestamp() {
            return startTimestamp;
        }

        public void setStartTimestamp(Long startTimestamp) {
            this.startTimestamp = startTimestamp;
        }
    }
}