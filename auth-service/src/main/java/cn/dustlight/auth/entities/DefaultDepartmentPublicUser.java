package cn.dustlight.auth.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DefaultDepartmentPublicUser extends DefaultPublicUser implements DepartmentPublicUser {

    private Long departmentId;
    private Date joinedDepartmentAt;

}
