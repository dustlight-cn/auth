package cn.dustlight.auth.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Date;

public interface DepartmentUser extends User {

    /**
     * 获取部门 ID
     *
     * @return 获取部门 ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getDepartmentId();

    /**
     * 获取加入部门时间
     *
     * @return 获取加入时间
     */
    Date getJoinedDepartmentAt();

}
