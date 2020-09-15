package cn.dustlight.oauth2.uim.mappers.v1;

import cn.dustlight.oauth2.uim.entities.v1.roles.DefaultUserRole;
import org.apache.ibatis.annotations.*;

import java.util.Collection;

@Mapper
public interface RoleMapper {

    @Select("SELECT uid,r.rid AS rid,roleName,roleDescription,expiredAt FROM user_role AS ur,roles AS r " +
            "WHERE ur.uid=#{uid} AND ur.rid=r.rid")
    @Results(id = "UserRole", value = {
            @Result(column = "rid", property = "rid"),
            @Result(column = "rid",
                    property = "authorities",
                    many = @Many(select = "cn.dustlight.oauth2.uim.mappers.v1.AuthorityMapper.listRoleAuthorities"))
    })
    Collection<DefaultUserRole> listUserRoles(Long uid);
}
