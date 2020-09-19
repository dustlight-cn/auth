package cn.dustlight.oauth2.uim.utils;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Sql排序部分生成器
 * <p>
 * 将字符串集合过滤后生成对应的Sql部分
 */
public class OrderBySqlBuilder {

    /**
     * 过滤器
     */
    private Collection<String> filters;

    public OrderBySqlBuilder(String... filters) {
        if (filters != null && filters.length > 0) {
            this.filters = new LinkedHashSet<>(filters.length);
            for (String attr : filters) {
                this.filters.add(attr);
                this.filters.add("-" + attr);
            }
        }
    }

    public OrderBySqlBuilder(Collection<String> filters) {
        this.filters = filters;
    }

    /**
     * 生成Sql部分语句，只允许在filters中存在的元素
     *
     * @param orders 排序条件
     * @return
     */
    public String build(Collection<String> orders) {
        if (orders == null || orders.size() == 0 ||
                filters == null || filters.size() == 0)
            return null;
        StringBuilder builder = new StringBuilder();
        for (String order : orders) {
            if (!filters.contains(order))
                continue;
            if (builder.length() > 0)
                builder.append(',');
            builder.append(order);
        }
        if (builder.length() == 0)
            return null;
        return builder.toString();
    }

    public static OrderBySqlBuilder create(String... attrs) {
        return new OrderBySqlBuilder(attrs);
    }

    public static OrderBySqlBuilder create(Collection<String> attrs) {
        return new OrderBySqlBuilder(attrs);
    }
}
