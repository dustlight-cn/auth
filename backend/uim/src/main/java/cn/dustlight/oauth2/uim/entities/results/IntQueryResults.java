package cn.dustlight.oauth2.uim.entities.results;

import java.util.Collection;

/**
 * 整型查询结果
 *
 * @param <T>
 */
public class IntQueryResults<T> implements QueryResults<T, Integer> {

    private Integer count;
    private Collection<T> data;

    @Override
    public Integer getCount() {
        return count;
    }

    @Override
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
