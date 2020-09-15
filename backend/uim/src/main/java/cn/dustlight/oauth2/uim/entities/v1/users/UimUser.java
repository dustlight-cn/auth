package cn.dustlight.oauth2.uim.entities.v1.users;

import cn.dustlight.oauth2.uim.entities.ser.ToStringCollectionSerializer;
import cn.dustlight.oauth2.uim.entities.v1.roles.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * 用户详情接口
 */
public interface UimUser extends UserDetails {

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
    Collection<UserRole> getUserRoles();

    /**
     * 获取用户创建时间
     *
     * @return 创建时间
     */
    Date getCreatedAt();

    /**
     * 获取用户更新时间
     *
     * @return 更新时间
     */
    Date getUpdatedAt();

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

    @JsonSerialize(using = ToStringCollectionSerializer.class)
    @Override
    Collection<? extends GrantedAuthority> getAuthorities();
}
