package cn.dustlight.auth.services;

import cn.dustlight.auth.entities.Role;

import java.util.Collection;

/**
 * 角色相关Services
 */
public interface RoleService {

    /**
     * 获取所有角色
     *
     * @return
     */
    Collection<? extends Role> listRoles();

    /**
     * 获取角色
     *
     * @param rids 角色id集合
     * @return
     */
    Collection<? extends Role> getRoles(Collection<Long> rids);

    /**
     * 获取角色
     *
     * @param rid 角色id
     * @return
     */
    Role getRole(Long rid);

    /**
     * 创建角色
     *
     * @param name        角色名
     * @param description 角色描述
     */
    void createRole(String name, String description);

    /**
     * 创建角色
     *
     * @param roles 角色信息集合
     */
    void createRoles(Collection<? extends Role> roles);

    /**
     * 删除角色
     *
     * @param rid 角色id
     */
    void removeRole(Long rid);

    /**
     * 删除角色
     *
     * @param rids 角色id集合
     */
    void removeRoles(Collection<Long> rids);

    /**
     * 获取角色权限
     *
     * @param rid 角色id
     * @return
     */
    Collection<String> listRoleAuthorities(Long rid);

    /**
     * 创建角色权限
     *
     * @param rid  角色id
     * @param aids 权限id集合
     * @return
     */
    void createRoleAuthorities(Long rid, Collection<Long> aids);

    /**
     * 删除角色权限
     *
     * @param rid  角色id
     * @param aids 权限id集合
     * @return
     */
    void removeRoleAuthorities(Long rid, Collection<Long> aids);
}
