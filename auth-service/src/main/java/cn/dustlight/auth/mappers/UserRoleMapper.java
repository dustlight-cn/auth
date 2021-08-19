package cn.dustlight.auth.mappers;

import cn.dustlight.auth.entities.UserRoleClient;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Mapper
public interface UserRoleMapper {

    @Select("SELECT " +
            "c.cid, " +
            "c.name, " +
            "c.createdAt, " +
            "c.updatedAt, " +
            "c.uid, " +
            "COUNT(r.cid) AS count " +
            "FROM " +
            "user_role AS ur, " +
            "roles AS r, " +
            "clients AS c " +
            "WHERE " +
            "ur.uid = #{uid} AND ur.rid = r.rid " +
            "AND c.cid = r.cid " +
            "GROUP BY r.cid")
    @Results({
            @Result(column = "cid", property = "cid"),
            @Result(column = "cid", property = "members", many = @Many(select = "cn.dustlight.auth.mappers.ClientMapper.getClientMemberIds"))
    })
    Collection<UserRoleClient> selectUserRoleClients(Long uid);
}
