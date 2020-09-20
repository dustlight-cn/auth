package cn.dustlight.validator.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultCode implements Code {

    private Object code;
    private Map<String, Object> body;

    public DefaultCode(Object code, Map<String, Object> body) {
        this.code = code;
        this.body = body;
    }

    public DefaultCode(Object code) {
        this.code = code;
        this.body = new LinkedHashMap<>();
    }

    @Override
    public Object getCode() {
        return code;
    }

    @Override
    public Map<String, Object> getBody() {
        return body;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }
}
