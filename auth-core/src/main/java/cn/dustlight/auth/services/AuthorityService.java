package cn.dustlight.auth.services;

import cn.dustlight.auth.entities.Authority;

import java.util.Collection;

/**
 * 权限相关Service
 */
public interface AuthorityService {

    /**
     * 获取全部权限
     *
     * @return 权限集合
     */
    Collection<? extends Authority> listAuthorities();

    /**
     * 获取应用下全部权限
     *
     * @param cid 应用id
     * @return 权限集合
     */
    Collection<? extends Authority> listAuthorities(String cid);

    /**
     * 获取权限
     *
     * @param aids 权限id集合
     * @return 权限集合
     */
    Collection<? extends Authority> getAuthorities(Collection<Long> aids);

    /**
     * 获取权限
     *
     * @param aid 权限id
     * @return 权限
     */
    Authority getAuthority(Long aid);

    /**
     * 创建权限
     *
     * @param name        权限名
     * @param description 权限描述
     */
    void createAuthority(String name, String description);

    /**
     * 创建权限
     *
     * @param authorities 权限信息集合
     */
    void createAuthorities(Collection<? extends Authority> authorities);

    /**
     * 删除权限
     *
     * @param aid 权限id
     */
    void removeAuthority(Long aid);

    /**
     * 删除权限
     *
     * @param aid 权限id
     * @param cid 应用id
     */
    void removeAuthority(Long aid, String cid);

    /**
     * 删除权限
     *
     * @param aids 权限id集合
     */
    void removeAuthorities(Collection<Long> aids);

    /**
     * 删除权限
     *
     * @param aids 权限id集合
     * @param cid  应用id
     */
    void removeAuthorities(Collection<Long> aids, String cid);
}
