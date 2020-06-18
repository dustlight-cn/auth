package cn.dustlight.uim.models;

public class AppDetails implements IAppDetails {

    private long uid;

    private String appKey;

    private String appSecret;

    private String appName;

    private String scope;

    private String redirectUri;

    @Override
    public long getUid() {
        return uid;
    }

    @Override
    public String getAppKey() {
        return appKey;
    }

    @Override
    public String getAppSecret() {
        return appSecret;
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public String getRedirectUri() {
        return redirectUri;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
