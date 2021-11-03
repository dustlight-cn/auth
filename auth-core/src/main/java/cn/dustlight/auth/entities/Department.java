package cn.dustlight.auth.entities;

import cn.dustlight.auth.util.Datable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

/**
 * 部门
 */
public interface Department extends Datable, Serializable {

    /**
     * 获取部门 ID
     *
     * @return 部门 ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getDid();

    /**
     * 获取组织 ID
     *
     * @return 组织 ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getOrg();

    /**
     * 获取部门名称
     *
     * @return 部门名称
     */
    String getName();

    /**
     * 获取部门简介
     *
     * @return 部门简介
     */
    String getDescription();

    /**
     * 获取上级部门 ID
     *
     * @return 上级部门 ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getParent();

}
