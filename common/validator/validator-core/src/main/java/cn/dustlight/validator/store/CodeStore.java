package cn.dustlight.validator.store;

import cn.dustlight.validator.annotation.Duration;
import cn.dustlight.validator.core.Code;

import java.util.Map;

/**
 * 验证码储存器
 */
public interface CodeStore {

    void store(Code code, Object key, Duration duration, Map<String, Object> parameters) throws StoreCodeException;

    Code load(Object key, Map<String, Object> parameters) throws LoadCodeException;

    void remove(Object key) throws RemoveCodeException;

}
