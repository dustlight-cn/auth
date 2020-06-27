package cn.dustlight.uim.services;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@CacheNamespace
@Service
@Mapper
public interface RoleDetailsMapper {

    @Select("SELECT role_name FROM role_details where id=#{id}")
    String getRoleNameByRoleId(Long id);

}
