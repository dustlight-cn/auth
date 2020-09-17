package cn.dustlight.oauth2.uim;

public final class Constants {
    public static final String API_ROOT = "/api/";

    public static class V1 {
        public static final int VERSION = 1;
        public static final String API_ROOT = Constants.API_ROOT + "v" + VERSION + "/";
        public static final String SESSION = "session";
    }

    public static class ContentType {
        public static final String APPLICATION_JSON = "application/json;charset=utf-8";
    }
}
