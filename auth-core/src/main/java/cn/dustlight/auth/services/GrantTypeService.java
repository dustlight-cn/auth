package cn.dustlight.auth.services;

import cn.dustlight.auth.entities.GrantType;

import java.util.Collection;

/**
 * 授权模式相关Service
 */
public interface GrantTypeService {

    /**
     * 获取全部授权模式
     *
     * @return 授权模式集合
     */
    Collection<? extends GrantType> listGrantTypes();

    /**
     * 获取授权模式
     *
     * @param tids 授权模式ID集合
     * @return 授权模式集合
     */
    Collection<? extends GrantType> getGrantTypes(Collection<Long> tids);

    /**
     * 获取授权模式
     *
     * @param tid 授权模式ID
     * @return 授权模式
     */
    GrantType getGrantType(Long tid);

    /**
     * 创建授权模式
     *
     * @param name        名称
     * @param description 描述
     */
    void createGrantType(String name, String description);

    /**
     * 创建授权模式
     *
     * @param grantTypes 授权模式集合
     */
    void createGrantTypes(Collection<? extends GrantType> grantTypes);

    /**
     * 删除授权模式
     *
     * @param tid 授权模式ID
     */
    void removeGrantType(Long tid);

    /**
     * 删除授权模式
     *
     * @param tids 授权模式ID集合
     */
    void removeGrantTypes(Collection<Long> tids);
}
