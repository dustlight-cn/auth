package cn.dustlight.auth.entities;

import cn.dustlight.auth.util.Datable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

/**
 * 授权模式
 */
public interface GrantType extends Datable, Serializable {

    /**
     * 获取授权模式id
     *
     * @return 授权模式id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getTid();

    /**
     * 获取授权模式名
     *
     * @return 授权模式名
     */
    String getName();

    /**
     * 获取授权模式描述
     *
     * @return 授权模式描述
     */
    String getDescription();
}
