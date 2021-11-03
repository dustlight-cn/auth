package cn.dustlight.auth.mappers;

import cn.dustlight.auth.entities.DefaultDepartment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Mapper
public interface DepartmentMapper {

    @Select("SELECT * FROM `departments` WHERE org=#{org}")
    Collection<DefaultDepartment> selectDepartmentsByOrg(@Param("org") Long org);

    @Insert("INSERT INTO `departments` (`did`,`org`,`name`,`description`,`parent`) VALUES " +
            "(#{did},#{org},#{name},#{description},#{parent})")
    boolean insertDepartment(@Param("did") Long did,
                             @Param("org") Long org,
                             @Param("name") String name,
                             @Param("description") String description,
                             @Param("parent") Long parent);

    @Delete("DELETE FROM `departments` WHERE org=#{org} AND did=#{did}")
    boolean deleteDepartmentByIdAndOrg(@Param("org") Long org,
                                       @Param("did") Long did);

    @Delete("DELETE FROM `departments` WHERE did=#{did}")
    boolean deleteDepartmentById(@Param("did") Long did);

    @Select("SELECT * FROM `departments` WHERE org=#{org} AND did=#{did} LIMIT 1")
    DefaultDepartment selectDepartmentByIdAndOrg(@Param("org") Long org,
                                                 @Param("did") Long did);

    @Select("SELECT * FROM `departments` WHERE did=#{did} LIMIT 1")
    DefaultDepartment selectDepartmentById(@Param("did") Long did);
}
