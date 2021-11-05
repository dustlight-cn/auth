package cn.dustlight.auth.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DefaultDepartmentUser extends DefaultUser implements DepartmentUser {

    private Long departmentId;
    private Date joinedDepartmentAt;

}
