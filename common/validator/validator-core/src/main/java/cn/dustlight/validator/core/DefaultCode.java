package cn.dustlight.validator.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultCode<T> implements Code<T> {

    private String name;
    private T value;
    private Map<String, Object> data;

    public DefaultCode(T value, Map<String, Object> data) {
        this.value = value;
        this.data = data;
    }

    public DefaultCode(T value) {
        this.value = value;
        this.data = new LinkedHashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public Map<String, Object> getDate() {
        return data;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setName(String name) {
        this.name = name;
    }
}
