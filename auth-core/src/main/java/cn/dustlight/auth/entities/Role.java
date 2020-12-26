package cn.dustlight.auth.entities;

import cn.dustlight.auth.util.Datable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

/**
 * 角色信息
 */
public interface Role extends Datable, Serializable {

    /**
     * 获取角色id
     *
     * @return 角色id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getRid();

    /**
     * 获取角色名
     *
     * @return 角色名
     */
    String getRoleName();

    /**
     * 获取角色描述
     *
     * @return 角色描述
     */
    String getRoleDescription();
}
