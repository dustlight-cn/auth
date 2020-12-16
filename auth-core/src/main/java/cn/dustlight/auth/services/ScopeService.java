package cn.dustlight.auth.services;

import cn.dustlight.auth.entities.Scope;

import java.util.Collection;

/**
 * 授权作用域相关Service
 */
public interface ScopeService {

    /**
     * 获取全部授权作用域
     *
     * @return 全部授权作用域
     */
    Collection<? extends Scope> listScopes();

    /**
     * 获取授权作用域
     *
     * @param sids 授权作用域id集合
     * @return 授权作用域
     */
    Collection<? extends Scope> getScopes(Collection<Long> sids);

    /**
     * 获取授权作用域
     *
     * @param sid 授权作用域id
     * @return 授权作用域
     */
    Scope getScope(Long sid);

    /**
     * 创建授权作用域
     *
     * @param name        名称
     * @param subtitle    标题
     * @param description 描述
     */
    void createScope(String name, String subtitle, String description);

    /**
     * 创建授权作用域
     *
     * @param scopes 授权作用域集合
     */
    void createScopes(Collection<? extends Scope> scopes);

    /**
     * 删除授权作用域
     *
     * @param sid 授权作用域id
     */
    void removeScope(Long sid);

    /**
     * 删除授权作用域
     *
     * @param sids 授权作用域id集合
     */
    void removeScopes(Collection<Long> sids);

    /**
     * 获取授权作用域权限
     *
     * @param sid 授权作用域id
     * @return 授权作用域权限
     */
    Collection<String> listScopeAuthorities(Long sid);

    /**
     * 获取授权作用域权限
     *
     * @param scopeNames 授权作用域名集合
     * @return 权作用域权限
     */
    Collection<String> listScopeAuthorities(Collection<String> scopeNames);

    /**
     * 创建授权作用域权限
     *
     * @param sid  授权作用域id
     * @param aids 权限id集合
     */
    void createScopeAuthorities(Long sid, Collection<Long> aids);

    /**
     * 删除授权作用域权限
     *
     * @param sid  授权作用域id
     * @param aids 权限id集合
     */
    void removeScopeAuthorities(Long sid, Collection<Long> aids);
}
