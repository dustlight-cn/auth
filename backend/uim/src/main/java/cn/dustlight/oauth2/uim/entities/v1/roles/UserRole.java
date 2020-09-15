package cn.dustlight.oauth2.uim.entities.v1.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 用户角色信息实体
 */
public interface UserRole extends Serializable {

    /**
     * 获取角色id
     *
     * @return 角色id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getRoleId();

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

    /**
     * 获取角色权限
     *
     * @return 角色权限
     */
    @JsonIgnore
    Collection<String> getAuthorities();

    /**
     * 获取过期时间
     *
     * @return 过期时间
     */
    Date getExpiredAt();

    /**
     * 判断是否过期
     *
     * @return 是否过期
     */
    boolean isExpired();
}
