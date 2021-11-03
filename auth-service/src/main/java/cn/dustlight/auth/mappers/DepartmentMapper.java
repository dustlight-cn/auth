package cn.dustlight.auth.mappers;

import cn.dustlight.auth.entities.DefaultDepartment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Mapper
public interface DepartmentMapper {

    @Select("SELECT * FROM `departments` WHERE org=#{org}")
    Collection<DefaultDepartment> selectDepartmentByOrg(@Param("org") Long org);

    @Insert("INSERT INTO `departments` (`did`,`org`,`name`,`description`,`parent`) VALUES " +
            "(#{did},#{org},#{name},#{description},#{parent})")
    boolean insertDepartment(@Param("did") Long did,
                                       @Param("org") Long org,
                                       @Param("name") String name,
                                       @Param("description") String description,
                                       @Param("parent") Long parent);
}
