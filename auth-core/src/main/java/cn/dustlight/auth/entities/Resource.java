package cn.dustlight.auth.entities;

import cn.dustlight.auth.util.Datable;

import java.io.Serializable;

/**
 * 资源
 */
public interface Resource extends Datable, Serializable {

    /**
     * 获取资源ID
     *
     * @return 资源ID
     */
    String getRid();

    /**
     * 获取资源名称
     *
     * @return 资源名称
     */
    String getName();
}
