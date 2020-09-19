package cn.dustlight.oauth2.uim.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dustlight.uim")
public class UimProperties {

    private String[] publicPaths = {};
    private String[] resourcePaths = {};
    private String[] redirectToLoginPath = {};

    private SnowflakeConfiguration snowflake = new SnowflakeConfiguration();
    private boolean csrfEnabled = true;
    private int registerVerificationCodeLength = 6;
    private int resetPasswordVerificationCodeLength = 12;
    private String registerEmail = "Register";
    private String resetPasswordEmail = "Password Reset";

    private Storage storage;

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public String getRegisterEmail() {
        return registerEmail;
    }

    public String getResetPasswordEmail() {
        return resetPasswordEmail;
    }

    public void setRegisterEmail(String registerEmail) {
        this.registerEmail = registerEmail;
    }

    public void setResetPasswordEmail(String resetPasswordEmail) {
        this.resetPasswordEmail = resetPasswordEmail;
    }

    public int getResetPasswordVerificationCodeLength() {
        return resetPasswordVerificationCodeLength;
    }

    public void setResetPasswordVerificationCodeLength(int resetPasswordVerificationCodeLength) {
        this.resetPasswordVerificationCodeLength = resetPasswordVerificationCodeLength;
    }

    public String[] getResourcePaths() {
        return resourcePaths;
    }

    public void setResourcePaths(String[] resourcePaths) {
        this.resourcePaths = resourcePaths;
    }

    public int getRegisterVerificationCodeLength() {
        return registerVerificationCodeLength;
    }

    public void setRegisterVerificationCodeLength(int registerVerificationCodeLength) {
        this.registerVerificationCodeLength = registerVerificationCodeLength;
    }

    public String[] getRedirectToLoginPath() {
        return redirectToLoginPath;
    }

    public void setRedirectToLoginPath(String[] redirectToLoginPath) {
        this.redirectToLoginPath = redirectToLoginPath;
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
        private String storageBaseUrl = null;
        private String storagePath = "dustlight/uim/upload/";
        private Long defaultExpiration = 1000L * 60 * 15L;

        public String getStorageBaseUrl() {
            return storageBaseUrl;
        }

        public void setStorageBaseUrl(String storageBaseUrl) {
            this.storageBaseUrl = storageBaseUrl;
        }

        public String getStoragePath() {
            return storagePath;
        }

        public void setStoragePath(String storagePath) {
            this.storagePath = storagePath;
        }

        public Long getDefaultExpiration() {
            return defaultExpiration;
        }

        public void setDefaultExpiration(Long defaultExpiration) {
            this.defaultExpiration = defaultExpiration;
        }
    }

    public static class SnowflakeConfiguration {
        public Long machineId,dataCenterId,startTimestamp;

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