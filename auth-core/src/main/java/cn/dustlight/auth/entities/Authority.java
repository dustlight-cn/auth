package cn.dustlight.auth.entities;


import cn.dustlight.auth.util.Datable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

/**
 * 权限实体
 */
public interface Authority extends Datable, Serializable {

    /**
     * 获取权限id
     *
     * @return 权限id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getAid();

    /**
     * 获取权限名
     *
     * @return 权限名
     */
    String getAuthorityName();

    /**
     * 获取权限描述
     *
     * @return 权限描述
     */
    String getAuthorityDescription();

}
