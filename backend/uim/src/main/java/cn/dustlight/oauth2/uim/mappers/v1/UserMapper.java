package cn.dustlight.oauth2.uim.mappers.v1;

import cn.dustlight.oauth2.uim.entities.v1.users.DefaultPublicUimUser;
import cn.dustlight.oauth2.uim.entities.v1.users.DefaultUimUser;
import cn.dustlight.oauth2.uim.entities.v1.users.PublicUimUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE username=#{uoe} OR email=#{uoe}")
    @Results(id = "User", value = {
            @Result(column = "uid", property = "uid"),
            @Result(column = "uid",
                    property = "roles",
                    many = @Many(select = "cn.dustlight.oauth2.uim.mappers.v1.RoleMapper.listUserRoles"))
    })
    DefaultUimUser selectUserByUsernameOrEmail(String uoe);

    @Select("SELECT * FROM users WHERE uid=#{uid}")
    @ResultMap("User")
    DefaultUimUser selectUser(Long uid);

    @Select({"<script>SELECT * FROM users WHERE uid IN ",
            "<foreach collection='uids' item='uid' open='(' separator=',' close=')'>#{uid}</foreach>",
            "</script>"})
    Collection<DefaultPublicUimUser> selectUsersPublic(@Param("uids") Collection<Long> uids);

}
