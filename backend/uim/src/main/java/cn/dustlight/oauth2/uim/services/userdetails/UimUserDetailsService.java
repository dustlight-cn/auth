package cn.dustlight.oauth2.uim.services.userdetails;

import cn.dustlight.oauth2.uim.entities.v1.roles.UserRole;
import cn.dustlight.oauth2.uim.entities.v1.users.PublicUimUser;
import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.Date;

/**
 * 用户相关Service
 */
public interface UimUserDetailsService extends UserDetailsService {

    /**
     * 创建用户
     *
     * @param username             用户名
     * @param password             密码
     * @param email                注册邮箱
     * @param gender               性别
     * @param roles                角色集合
     * @param accountExpiredAt     账号过期时间
     * @param credentialsExpiredAt 凭证过期时间
     * @param unlockedAt           账号解锁时间
     * @param enabled              账号是否启用
     * @return 用户对象
     */
    UimUser createUser(String username,
                       String password,
                       String email,
                       int gender,
                       Collection<UserRole> roles,
                       Date accountExpiredAt,
                       Date credentialsExpiredAt,
                       Date unlockedAt,
                       boolean enabled);

    /**
     * 通过uid获取用户
     *
     * @param uid 用户uid
     * @return 用户对象
     */
    UimUser loadUser(Long uid);

    /**
     * 通过uid集合获取用户（公开）集合
     *
     * @param uidArray 用户uid集合
     * @return 用户对象集合
     */
    Collection<PublicUimUser> loadUserPublic(Collection<Long> uidArray);

    /**
     * 通过用户名集合获取用户集合
     *
     * @param usernames 用户名集合
     * @return 用户对象集合
     */
    Collection<UimUser> loadUsersByUsername(Collection<String> usernames);

    /**
     * 通过uid集合获取用户集合
     *
     * @param uidArray 用户uid集合
     * @return 用户对象集合
     */
    Collection<UimUser> loadUsers(Collection<Long> uidArray);

    /**
     * 更新用户密码
     *
     * @param uid      用户uid
     * @param password 新密码
     * @return 是否成功
     */
    boolean updatePassword(Long uid, String password);

    /**
     * 通过邮箱更新密码
     *
     * @param email    用户邮箱
     * @param password 新密码
     * @return 是否成功
     */
    boolean updatePasswordByEmail(String email, String password);

    /**
     * 更新用户昵称
     *
     * @param uid      用户uid
     * @param nickname 用户昵称
     * @return 是否成功
     */
    boolean updateNickname(Long uid, String nickname);

    /**
     * 更新用户性别
     *
     * @param uid    用户uid
     * @param gender 用户性别
     * @return 是否成功
     */
    boolean updateGender(Long uid, int gender);

    /**
     * 更新用户邮箱
     *
     * @param uid   用户uid
     * @param email 用户邮箱
     * @return 是否成功
     */
    boolean updateEmail(Long uid, String email);

    /**
     * 为用户添加角色
     *
     * @param uid   用户uid
     * @param roles 角色集合
     * @return 是否成功
     */
    boolean addRoles(Long uid, Collection<UserRole> roles);

    /**
     * 为用户移除角色
     *
     * @param uid     用户uid
     * @param roleIds 角色id集合
     * @return 是否成功
     */
    boolean removeRoles(Long uid, Collection<Long> roleIds);

    /**
     * 通过角色名更新用户角色
     *
     * @param uid      用户uid
     * @param roleName 角色名
     * @return 是否成功
     */
    boolean updateRoleByRoleName(Long uid, String roleName);

    /**
     * 更新用户解锁时间
     *
     * @param uid        用户uid
     * @param unlockedAt 用户解锁时间
     * @return 是否成功
     */
    boolean updateUnlockedAt(Long uid, Date unlockedAt);

    /**
     * 更新账号过期时间
     *
     * @param uid       用户uid
     * @param expiredAt 账号过期时间
     * @return 是否成功
     */
    boolean updateAccountExpiredAt(Long uid, Date expiredAt);

    /**
     * 更新凭证过期时间
     *
     * @param uid       用户uid
     * @param expiredAt 凭证过期时间
     * @return 是否成功
     */
    boolean updateCredentialsExpiredAt(Long uid, Date expiredAt);

    /**
     * 更新用户是否启用
     *
     * @param uid     用户uid
     * @param enabled 用户是否启用
     * @return 是否成功
     */
    boolean updateEnabled(Long uid, boolean enabled);
}
