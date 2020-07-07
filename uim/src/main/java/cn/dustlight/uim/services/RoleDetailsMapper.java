package cn.dustlight.uim.services;

import cn.dustlight.uim.models.RoleDetails;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@CacheNamespace
@Service
@Mapper
public interface RoleDetailsMapper {

    @Select("SELECT id,role_name,description,createdAt,updatedAt FROM role_details where id=#{id}")
    @Results(id = "RoleDetails", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "role_name"),
            @Result(property = "description", column = "description"),
            @Result(property = "createdAt", column = "createdAt"),
            @Result(property = "updatedAt", column = "updatedAt"),
    })
    RoleDetails getRoleDetails(Long id);

    @Select("SELECT role_name,description FROM role_details where id=#{id}")
    @ResultMap("RoleDetails")
    RoleDetails getRoleNameAndDescription(Long id);
}
