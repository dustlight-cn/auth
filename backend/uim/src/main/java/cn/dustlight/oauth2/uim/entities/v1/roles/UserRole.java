package cn.dustlight.oauth2.uim.entities.v1.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 用户角色信息
 */
public interface UserRole extends Role, Serializable {

    @JsonIgnore
    @Override
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
