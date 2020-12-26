package cn.dustlight.auth.util;

import java.util.Collection;

/**
 * 分页查询结果
 *
 * @param <T>
 */
public class QueryResults<T> {

    /**
     * 查询结果总数
     */
    private Integer count;
    /**
     * 当前页集合
     */
    private Collection<T> data;

    /**
     * 获取查询结果总数
     *
     * @return 结果总数
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 获取当前页集合
     *
     * @return 当前页集合
     */
    public Collection<T> getData() {
        return data;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }
}
