package cn.dustlight.uim.services;

import cn.dustlight.uim.models.RoleDetails;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheNamespace
@Service
@Mapper
public interface RoleDetailsMapper {

    @Select("SELECT id,role_name,description,createdAt,updatedAt FROM role_details WHERE id=#{id}")
    @Results(id = "RoleDetails", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "role_name"),
            @Result(property = "description", column = "description"),
            @Result(property = "createdAt", column = "createdAt"),
            @Result(property = "updatedAt", column = "updatedAt"),
    })
    RoleDetails getRoleDetails(Long id);

    @Select("SELECT * FROM role_details")
    @ResultMap("RoleDetails")
    List<RoleDetails> getAllRoleDetails();

    @Select("SELECT role_name,description FROM role_details WHERE id=#{id}")
    @ResultMap("RoleDetails")
    RoleDetails getRoleNameAndDescription(Long id);

    @Delete("DELETE FROM role_details WHERE id=#{id}")
    boolean deleteRoleDetails(Long id);

    @Insert("INSERT INTO role_details(id,role_name,description) VALUES(#{id},#{name},#{description})")
    boolean insertRoleDetails(Long id, String name, String description);

    @Update("UPDATE role_details SET role_name=#{name},description=#{description} WHERE id=#{id}")
    boolean updateRoleDetails(Long id, String name, String description);
}
