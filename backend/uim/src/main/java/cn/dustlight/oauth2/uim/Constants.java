package cn.dustlight.oauth2.uim;

/**
 * 全局静态常量类
 */
public final class Constants {
    /**
     * 全局接口根路径
     */
    public static final String API_ROOT = "/u/";
    /**
     * 当前版本号
     */
    public static final int VERSION = V1.VERSION;

    public static class V1 {
        /**
         * 版本号
         */
        public static final int VERSION = 1;
        /**
         * v1版本接口根路径
         */
        public static final String API_ROOT = Constants.API_ROOT + "v" + VERSION + "/";

        public static class Resource {
            /**
             * 会话资源
             */
            public static final String SESSION = "session";

            /**
             * 用户资源
             */
            public static final String USER = "user";
        }
    }

    public static class ContentType {
        public static final String APPLICATION_JSON = "application/json;charset=utf-8";
    }
}
