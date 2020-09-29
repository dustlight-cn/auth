package cn.dustlight.oauth2.uim.entities.v1.authorities;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限信息
 */
public interface Authority extends Serializable {

    /**
     * 获取权限id
     *
     * @return
     */
    Long getAid();

    /**
     * 获取权限名
     *
     * @return
     */
    String getAuthorityName();

    /**
     * 获取权限描述
     *
     * @return
     */
    String getAuthorityDescription();

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
