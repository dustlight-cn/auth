package cn.dustlight.oauth2.uim.services.code;

public interface VerificationCodeStoreService {

    void store(String key, String code, long duration);

    String getCode(String key);

    void remove(String key);

    boolean verify(String key, String code);

}
