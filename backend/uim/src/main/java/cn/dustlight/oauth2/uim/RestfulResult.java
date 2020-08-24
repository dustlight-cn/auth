package cn.dustlight.oauth2.uim;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.*;

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

    private static final MappingJackson2JsonView jsonView = new MappingJackson2JsonView();

    static {
        Set<String> set = new HashSet<>();
        set.add("msg");
        set.add("code");
        set.add("data");

        jsonView.setModelKeys(set);
    }

    public ModelAndView toModelAndView() {
        return new ModelAndView(jsonView)
                .addObject("msg", msg)
                .addObject("code", code)
                .addObject("data", data);
    }
}