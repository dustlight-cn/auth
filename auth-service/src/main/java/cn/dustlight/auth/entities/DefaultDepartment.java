package cn.dustlight.auth.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Schema(name = "Department")
public class DefaultDepartment implements Department {

    private Date createdAt, updatedAt;

    private Long did, parent, org;

    private String name, description;

}
