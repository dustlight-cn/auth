package cn.dustlight.auth.entities;

import cn.dustlight.auth.util.Datable;
import cn.dustlight.auth.util.ToStringCollectionSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 用户详情
 */
public interface User extends UserDetails, Datable, Serializable {

    /**
     * 获取用户UID
     *
     * @return 用户uid
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getUid();

    /**
     * 获取用户注册邮箱
     *
     * @return 用户邮箱
     */
    String getEmail();

    /**
     * 获取用户昵称
     *
     * @return 用户昵称
     */
    String getNickname();

    @JsonIgnore
    @Override
    String getPassword();

    /**
     * 获取用户性别
     *
     * @return 用户性别
     */
    int getGender();

    /**
     * 获取用户头像
     *
     * @return 用户头像
     */
    String getAvatar();

    /**
     * 获取用户角色集合
     *
     * @return 角色集合
     */
    @JsonProperty("roles")
    @Schema(name = "roles")
    Collection<UserRole> getUserRoles();

    /**
     * 获取账号过期时间
     *
     * @return 账号过期时间
     */
    Date getAccountExpiredAt();

    /**
     * 获取凭证过期时间
     *
     * @return 凭证过期时间
     */
    Date getCredentialsExpiredAt();

    /**
     * 获取账号解锁时间
     *
     * @return 解锁时间
     */
    Date getUnlockedAt();

    @ArraySchema(schema = @Schema(type = "string"))
    @JsonSerialize(using = ToStringCollectionSerializer.class)
    @Override
    Collection<? extends GrantedAuthority> getAuthorities();
}
