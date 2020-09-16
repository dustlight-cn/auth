package cn.dustlight.oauth2.uim.utils;

import java.util.Collection;
import java.util.LinkedHashSet;

public class OrdersStringBuilder {

    private Collection<String> attrs;

    public OrdersStringBuilder(String... attrs) {
        if (attrs != null && attrs.length > 0) {
            this.attrs = new LinkedHashSet<>(attrs.length);
            for (String attr : attrs) {
                this.attrs.add(attr);
                this.attrs.add("-" + attr);
            }
        }
    }

    public OrdersStringBuilder(Collection<String> attrs) {
        this.attrs = attrs;
    }

    public String build(Collection<String> orders) {
        if (orders == null || orders.size() == 0 ||
                attrs == null || attrs.size() == 0)
            return null;
        StringBuilder builder = new StringBuilder();
        for (String order : orders) {
            if (!attrs.contains(order))
                continue;
            if (builder.length() > 0)
                builder.append(',');
            builder.append(order);
        }
        if (builder.length() == 0)
            return null;
        return builder.toString();
    }

    public static OrdersStringBuilder create(String... attrs) {
        return new OrdersStringBuilder(attrs);
    }

    public static OrdersStringBuilder create(Collection<String> attrs) {
        return new OrdersStringBuilder(attrs);
    }
}
