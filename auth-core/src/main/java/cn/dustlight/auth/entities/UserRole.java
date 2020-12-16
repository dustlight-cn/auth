package cn.dustlight.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 用户角色信息
 */
public interface UserRole extends Role, Serializable {

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

    @JsonIgnore
    @Override
    Date getCreatedAt();

    @JsonIgnore
    @Override
    Date getUpdatedAt();
}
