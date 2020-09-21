package cn.dustlight.validator.store;

import cn.dustlight.validator.annotations.Duration;
import cn.dustlight.validator.core.Code;

import java.util.Map;

/**
 * 验证码储存器
 */
public interface CodeStore<T> {

    void store(Code<T> code, Duration duration, Map<String, Object> parameters) throws StoreCodeException;

    Code<T> load(String name, Map<String, Object> parameters) throws LoadCodeException;

    void remove(String key) throws RemoveCodeException;

}
