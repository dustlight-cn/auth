package cn.dustlight.validator.core;

import java.io.Serializable;
import java.util.Map;

/**
 * 验证码
 */
public interface Code<T> extends Serializable {

    /**
     * 获取验证码名
     *
     * @return
     */
    String getName();

    /**
     * 获取验证码值
     *
     * @return
     */
    T getValue();

    /**
     * 获取额外数据
     *
     * @return
     */
    Map<String, Object> getData();
}
