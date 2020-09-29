package cn.dustlight.oauth2.uim.entities.v1.types;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Date;

/**
 * 授权模式
 */
public interface GrantType {

    /**
     * 获取授权模式id
     *
     * @return
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getTid();

    /**
     * 获取授权模式名
     *
     * @return
     */
    String getName();

    /**
     * 获取授权模式描述
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
