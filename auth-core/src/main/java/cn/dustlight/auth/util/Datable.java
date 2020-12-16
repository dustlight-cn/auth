package cn.dustlight.auth.util;

import java.util.Date;

public interface Datable {
    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    Date getCreatedAt();

    /**
     * 获取更新时间
     *
     * @return 更新时间
     */
    Date getUpdatedAt();
}
