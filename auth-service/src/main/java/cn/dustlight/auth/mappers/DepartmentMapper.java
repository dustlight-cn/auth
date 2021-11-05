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

    @Insert("INSERT INTO `departments` (`did`,`org`,`name`,`description`,`parent`) " +
            "(SELECT #{did},#{org},#{name},#{description},did FROM `department` WHERE did=#{parent} AND org=#{org} LIMIT 1)")
    boolean insertDepartment(@Param("did") Long did,
                             @Param("org") Long org,
                             @Param("name") String name,
                             @Param("description") String description,
                             @Param("parent") Long parent);

    @Insert("INSERT INTO `departments` (`did`,`org`,`name`,`description`) VALUES " +
            "(#{did},#{org},#{name},#{description})")
    boolean insertDepartmentWithoutParent(@Param("did") Long did,
                                          @Param("org") Long org,
                                          @Param("name") String name,
                                          @Param("description") String description);

    @Delete("DELETE FROM `departments` WHERE org=#{org} AND did=#{did}")
    boolean deleteDepartmentByIdAndOrg(@Param("org") Long org,
                                       @Param("did") Long did);

    @Delete("DELETE FROM `departments` WHERE did=#{did}")
    boolean deleteDepartmentById(@Param("did") Long did);

    @Update("<script>" +
            "UPDATE `departments` SET" +
            "<if test='name'> name=#{name}</if>" +
            "<if test='description'> description=#{description}</if>" +
            " WHERE did=#{did}" +
            "</script>")
    boolean updateDepartment(@Param("did") Long did,
                             @Param("name") String name,
                             @Param("description") String description);

    @Update("<script>" +
            "UPDATE `departments` SET" +
            "<if test='name'> name=#{name}</if>" +
            "<if test='description'> description=#{description}</if>" +
            " WHERE org=#{org} AND did=#{did}" +
            "</script>")
    boolean updateDepartmentWithOrg(@Param("did") Long did,
                                    @Param("org") Long org,
                                    @Param("name") String name,
                                    @Param("description") String description);

    @Update("UPDATE `departments` AS d INNER JOIN " +
            "(SELECT did,org FROM `departments` WHERE did=#{parent} LIMIT 1) AS p " +
            "SET d.parent=p.did WHERE d.did=#{did} AND d.org=p.org")
    boolean updateDepartmentParent(@Param("did") Long did,
                                   @Param("parent") Long parent);

    @Update("UPDATE `departments` AS d INNER JOIN " +
            "(SELECT did FROM `departments` WHERE did=#{parent} AND org=#{org} LIMIT 1) AS p " +
            "SET d.parent=p.did WHERE d.did=#{did} AND d.org=#{org}")
    boolean updateDepartmentParentWithOrg(@Param("did") Long did,
                                          @Param("org") Long org,
                                          @Param("parent") Long parent);

    @Update("UPDATE `departments` " +
            "SET parent=NULL WHERE did=#{did}")
    boolean updateDepartmentParentToNull(@Param("did") Long did);

    @Update("UPDATE `departments` " +
            "SET parent=NULL WHERE did=#{did} AND org=#{org}")
    boolean updateDepartmentParentToNullWithOrg(@Param("did") Long did,
                                                @Param("org") Long org);

    @Select("SELECT * FROM `departments` WHERE org=#{org} AND did=#{did} LIMIT 1")
    DefaultDepartment selectDepartmentByIdAndOrg(@Param("org") Long org,
                                                 @Param("did") Long did);

    @Select("SELECT * FROM `departments` WHERE did=#{did} LIMIT 1")
    DefaultDepartment selectDepartmentById(@Param("did") Long did);

    @Select("WITH recursive p AS ( " +
            "SELECT * FROM departments WHERE did=#{did} " +
            "UNION ALL " +
            "SELECT d.* FROM departments as d,p WHERE d.parent=p.did " +
            ") " +
            "SELECT * FROM p")
    Collection<DefaultDepartment> selectDepartmentWithChildren(@Param("did") Long did);

    @Select("WITH recursive p AS ( " +
            "SELECT * FROM departments WHERE did=#{did} AND org=#{org} " +
            "UNION ALL " +
            "SELECT d.* FROM departments as d,p WHERE d.parent=p.did AND d.org=#{org} " +
            ") " +
            "SELECT * FROM p")
    Collection<DefaultDepartment> selectDepartmentWithChildrenWithOrg(@Param("org") Long org,
                                                                      @Param("did") Long did);

    @Select("WITH recursive p AS ( " +
            "SELECT * FROM departments WHERE did=#{did} " +
            "UNION ALL " +
            "SELECT d.* FROM departments as d,p WHERE d.did=p.parent " +
            ") " +
            "SELECT * FROM p")
    Collection<DefaultDepartment> selectDepartmentWithParent(@Param("did") Long did);

    @Select("WITH recursive p AS ( " +
            "SELECT * FROM departments WHERE did=#{did} AND org=#{org} " +
            "UNION ALL " +
            "SELECT d.* FROM departments as d,p WHERE d.did=p.parent AND d.org=#{org} " +
            ") " +
            "SELECT * FROM p")
    Collection<DefaultDepartment> selectDepartmentWithParentWithOrg(@Param("org") Long org,
                                                                    @Param("did") Long did);
}
