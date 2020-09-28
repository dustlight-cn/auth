package cn.dustlight.oauth2.uim.entities.v1.scopes;

import java.util.Date;

/**
 * 授权作用域
 */
public interface Scope {

    /**
     * 获取授权作用域id
     *
     * @return
     */
    Long getSid();

    /**
     * 获取授权作用域名称
     *
     * @return
     */
    String getName();

    /**
     * 获取授权作用域标题
     *
     * @return
     */
    String getSubtitle();

    /**
     * 获取授权作用域描述
     *
     * @return
     */
    String getDescription();

    /**
     * 获取创建时间
     *
     * @return
     */
    Date getCreatedAt();

    /**
     * 获取更新时间
     *
     * @return
     */
    Date getUpdatedAt();
}
