package cn.dustlight.uim;

/**
 * Restful Result
 *
 * @param <T> result type
 */
public class RestfulResult<T> {

    private static final String MSG_SUCCESS = "success";
    private static final int CODE_SUCCESS = 200;
    private static final String MSG_ERROR = "error";
    private static final int CODE_ERROR = 500;

    private String msg = "";
    private int code = 0;
    private T data = null;

    public static <T> RestfulResult<T> success() {
        RestfulResult<T> result = new RestfulResult<T>();
        result.msg = MSG_SUCCESS;
        result.code = CODE_SUCCESS;
        return result;
    }

    public static <T> RestfulResult<T> success(T data) {
        RestfulResult<T> result = new RestfulResult<T>();
        result.msg = MSG_SUCCESS;
        result.code = CODE_SUCCESS;
        result.data = data;
        return result;
    }

    public static <T> RestfulResult<T> error() {
        RestfulResult<T> result = new RestfulResult<T>();
        result.msg = MSG_ERROR;
        result.code = CODE_ERROR;
        return result;
    }

    public static <T> RestfulResult<T> error(String errorMsg) {
        RestfulResult<T> result = new RestfulResult<T>();
        result.msg = errorMsg;
        result.code = CODE_ERROR;
        return result;
    }

    public static <T> RestfulResult<T> error(int errorCode, String errorMsg) {
        RestfulResult<T> result = new RestfulResult<T>();
        result.msg = errorMsg;
        result.code = CODE_ERROR + errorCode;
        return result;
    }

    public int getCode() {
        return code;
    }

    public RestfulResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public T getData() {
        return data;
    }

    public RestfulResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RestfulResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public RestfulResult<T> copy() {
        RestfulResult<T> instance = new RestfulResult<>();
        instance.data = this.data;
        instance.code = this.code;
        instance.msg = this.msg;
        return instance;
    }
}