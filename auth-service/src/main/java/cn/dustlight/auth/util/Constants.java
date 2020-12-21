package cn.dustlight.auth.util;

public final class Constants {

    public final static String VERSION = "v0";

    public final static String API_ROOT = "/" + VERSION;

    public final static String CHARSET = "UTF-8";

    public static class ContentType {
        public static final String APPLICATION_JSON = "application/json; charset=" + CHARSET;
    }

    public final static class CrossOrigin {
        public static final String origin = "*";
        public static final String allowCredentials = "true";
    }
}
