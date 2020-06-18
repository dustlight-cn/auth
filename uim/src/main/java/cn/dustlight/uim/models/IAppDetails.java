package cn.dustlight.uim.models;

public interface IAppDetails {

    long getUid();

    String getAppKey();

    String getAppSecret();

    String getAppName();

    String getScope();

    String getRedirectUri();
}
