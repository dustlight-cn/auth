package cn.dustlight.auth.entities;

import cn.dustlight.auth.util.Datable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

/**
 * 授权作用域
 */
public interface Scope extends Datable, Serializable {

    /**
     * 获取授权作用域id
     *
     * @return 授权作用域id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getSid();

    /**
     * 获取授权作用域名称
     *
     * @return 授权作用域名称
     */
    String getName();

    /**
     * 获取授权作用域标题
     *
     * @return 授权作用域标题
     */
    String getSubtitle();

    /**
     * 获取授权作用域描述
     *
     * @return 授权作用域描述
     */
    String getDescription();
}
