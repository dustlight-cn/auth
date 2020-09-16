package cn.dustlight.oauth2.uim.entities.results;

import java.io.Serializable;
import java.util.Collection;

/**
 * 查询结果
 *
 * @param <T> 结果类型
 * @param <V> 数字类型
 */
public interface QueryResults<T, V extends Number> extends Serializable {
    /**
     * 获取查询结果总数
     *
     * @return 总数
     */
    V getCount();

    /**
     * 获取查询结果集合
     *
     * @return 结果集合
     */
    Collection<T> getData();
}
