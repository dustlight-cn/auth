package cn.dustlight.auth.util;

public final class Constants {

    public final static String VERSION = "v1";

    public final static String API_ROOT = "/" + VERSION;

    public final static String CHARSET = "UTF-8";

    public final static String AVATAR_FORMAT = "users/%s/avatar";

    public final static String CLIENT_LOGO_FORMAT = "clients/%s/logo";

    public static class ContentType {
        public static final String APPLICATION_JSON = "application/json; charset=" + CHARSET;
    }

    public final static class CrossOrigin {
        public static final String origin = "*";
        public static final String allowCredentials = "true";
    }
}
