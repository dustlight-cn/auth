package cn.dustlight.oauth2.uim.services.authorities;

import cn.dustlight.oauth2.uim.entities.v1.authorities.Authority;

import java.util.Collection;

/**
 * 权限相关Service
 */
public interface AuthorityService {

    /**
     * 获取全部权限
     *
     * @return
     */
    Collection<? extends Authority> listAuthorities();

    /**
     * 获取权限
     *
     * @param aids 权限id集合
     * @return
     */
    Collection<? extends Authority> selectAuthorities(Collection<Long> aids);

    /**
     * 获取权限
     *
     * @param aid 权限id
     * @return
     */
    Authority selectAuthority(Long aid);

    /**
     * 插入权限
     *
     * @param name        权限名
     * @param description 权限描述
     */
    void insertAuthority(String name, String description);

    /**
     * 插入权限
     *
     * @param authorities 权限信息集合
     */
    void insertAuthorities(Collection<Authority> authorities);

    /**
     * 删除权限
     *
     * @param aid 权限id
     */
    void deleteAuthority(Long aid);

    /**
     * 删除权限
     *
     * @param aids 权限id集合
     */
    void deleteAuthorities(Collection<Long> aids);
}
