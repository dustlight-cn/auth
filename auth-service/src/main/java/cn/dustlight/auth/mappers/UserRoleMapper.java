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
            "COUNT(r.rid) AS count " +
            "FROM " +
            "user_role AS ur, " +
            "roles AS r, " +
            "clients AS c " +
            "WHERE " +
            "ur.uid = #{uid} AND ur.rid = r.rid " +
            "AND c.cid = r.cid " +
            "GROUP BY c.cid")
    @Results({
            @Result(column = "cid", property = "cid"),
            @Result(column = "cid", property = "members", many = @Many(select = "cn.dustlight.auth.mappers.ClientMapper.getClientMemberIds"))
    })
    Collection<UserRoleClient> selectUserRoleClients(Long uid);

    @Select("SELECT " +
            "clients.cid, " +
            "clients.name, " +
            "clients.createdAt, " +
            "clients.updatedAt, " +
            "clients.uid, " +
            "COUNT(r.rid) AS count " +
            "FROM " +
            "clients, " +
            "((SELECT cid FROM clients WHERE uid = #{uid}) UNION (SELECT cid FROM client_members WHERE uid = #{uid})) AS c, " +
            "roles AS r " +
            "WHERE clients.cid = c.cid AND r.cid = c.cid " +
            "GROUP BY clients.cid")
    Collection<UserRoleClient> selectManagedUserRoleClients(Long uid);
}
