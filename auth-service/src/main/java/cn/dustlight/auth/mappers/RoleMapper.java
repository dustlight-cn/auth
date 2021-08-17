package cn.dustlight.auth.mappers;

import cn.dustlight.auth.entities.DefaultRole;
import cn.dustlight.auth.entities.DefaultUserRole;
import cn.dustlight.auth.entities.Role;
import cn.dustlight.auth.entities.UserRole;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
@Mapper
public interface RoleMapper {

    /* ----------------------------------------------------------------------------------------------------- */

    /* Role */

    @Select("SELECT * FROM roles")
    Collection<DefaultRole> listRoles();

    @Select("SELECT * FROM roles WHERE cid=#{cid}")
    Collection<DefaultRole> listRolesWithClientId(@Param("cid") String cid);

    @Select("<script>SELECT * FROM roles WHERE rid IN " +
            "<foreach collection='rids' item='rid' open='(' separator=',' close=')'>" +
            "#{rid}</foreach></script>")
    Collection<DefaultRole> selectRoles(@Param("rids") Collection<Long> rids);

    @Select("SELECT * FROM roles WHERE rid=#{rid} LIMIT 1")
    DefaultRole selectRole(@Param("rid") Long rid);

    @Insert("INSERT INTO roles (rid,roleName,roleDescription,cid) VALUES (#{rid},#{roleName},#{roleDescription},#{cid}) " +
            "ON DUPLICATE KEY UPDATE roleName=VALUES(roleName),roleDescription=VALUES(roleDescription),cid=#{cid}")
    Boolean insertRole(@Param("rid") Long rid,
                       @Param("roleName") String roleName,
                       @Param("roleDescription") String roleDescription,
                       @Param("cid") String cid);

    @Insert("<script>INSERT INTO roles (rid,roleName,roleDescription,cid) VALUES " +
            "<foreach collection='roles' item='role' separator=','>" +
            "(#{role.rid},#{role.roleName},#{role.roleDescription},#{cid})" +
            "</foreach> ON DUPLICATE KEY UPDATE roleName=VALUES(roleName),roleDescription=VALUES(roleDescription)</script>")
    Boolean insertRoles(@Param("roles") Collection<? extends Role> roles,
                        @Param("cid") String cid);

    @Delete("DELETE FROM roles WHERE rid=#{rid}")
    Boolean deleteRole(@Param("rid") Long rid);

    @Delete("DELETE FROM roles WHERE rid=#{rid} AND cid=#{cid}")
    Boolean deleteRoleWithClientId(@Param("rid") Long rid,
                                   @Param("cid") String cid);

    @Delete("<script>DELETE FROM roles WHERE rid IN " +
            "<foreach collection='rids' item='rid' open='(' separator=',' close=')'>" +
            "#{rid}</foreach></script>")
    Boolean deleteRoles(@Param("rids") Collection<Long> rids);

    @Delete("<script>DELETE FROM roles WHERE cid=#{cid} AND rid IN " +
            "<foreach collection='rids' item='rid' open='(' separator=',' close=')'>" +
            "#{rid}</foreach></script>")
    Boolean deleteRolesWithClientId(@Param("rids") Collection<Long> rids,
                                    @Param("cid") String cid);

    /* ----------------------------------------------------------------------------------------------------- */

    /* UserRole */

    @Select("SELECT uid,r.rid AS rid,roleName,roleDescription,expiredAt FROM user_role AS ur,roles AS r " +
            "WHERE ur.uid=#{uid} AND ur.rid=r.rid")
    @Results(id = "UserRole", value = {
            @Result(column = "rid", property = "rid"),
            @Result(column = "rid",
                    property = "authorities",
                    many = @Many(select = "cn.dustlight.auth.mappers.AuthorityMapper.listRoleAuthorities"))
    })
    Collection<DefaultUserRole> listUserRoles(@Param("uid") Long uid);

    @Select("SELECT uid,r.rid AS rid,roleName,roleDescription,expiredAt FROM user_role AS ur,roles AS r " +
            "WHERE ur.uid=#{uid} AND ur.rid=r.rid AND r.cid=#{cid}")
    Collection<DefaultUserRole> listUserRolesWithClientId(@Param("uid") Long uid,
                                                          @Param("cid") String cid);

    @Select("SELECT roleName FROM user_role AS ur,roles AS r " +
            "WHERE ur.uid=#{uid} AND ur.rid=r.rid")
    Collection<String> listUserRoleNames(@Param("uid") Long uid);

    @Select("SELECT r.rid FROM user_role AS ur,roles AS r " +
            "WHERE ur.uid=#{uid} AND ur.rid=r.rid")
    Collection<Long> listUserRoleIds(@Param("uid") Long uid);

    @Insert("<script>INSERT INTO user_role (uid,rid,expiredAt) VALUES " +
            "<foreach collection='roles' item='role' separator=','>(#{uid},#{role},#{expiredAt})</foreach>" +
            "ON DUPLICATE KEY UPDATE expiredAt=#{expiredAt}</script>")
    Boolean insertUserRolesByRoleIds(@Param("uid") Long uid,
                                     @Param("roles") Collection<Long> roles,
                                     @Param("expiredAt") Date expiredAt);

    @Insert("<script>INSERT INTO user_role (uid,rid,expiredAt) VALUES " +
            "<foreach collection='roles' item='role' separator=','>(#{uid},<choose>" +
            "<when test='role.rid!=null'>#{role.rid}</when>" + // 如果roleId存在
            "<otherwise>(SELECT rid FROM roles WHERE roleName=#{role.roleName} LIMIT 1)</otherwise>" + // 不存在则查询
            "</choose>,#{role.expiredAt})</foreach>" +
            " ON DUPLICATE KEY UPDATE expiredAt=VALUES(expiredAt)</script>")
    <T extends UserRole> Boolean insertUserRoles(@Param("uid") Long uid,
                                                 @Param("roles") Collection<T> roles);

    @Insert("<script>INSERT INTO user_role (uid,rid,expiredAt) " +
            "(SELECT #{uid} as uid,rid,#{expiredAt} as expiredAt FROM roles WHERE roleName IN " +
            "<foreach collection='roleNames' item='roleName' separator=',' open='(' close=')'>#{roleName}</foreach>)" +
            "ON DUPLICATE KEY UPDATE expiredAt=#{expiredAt}</script>")
    Boolean insertUserRolesByRoleNames(@Param("uid") Long uid,
                                       @Param("roleNames") Collection<String> roleNames,
                                       @Param("expiredAt") Date expiredAt);

    @Delete("<script>DELETE FROM user_role WHERE uid=#{uid} AND rid IN " +
            "<foreach collection='roles' item='role' separator=',' open='(' close=')'>" +
            "#{role}" +
            "</foreach></script>")
    Boolean deleteUserRolesByRoleIds(@Param("uid") Long uid,
                                     @Param("roles") Collection<Long> roles);

    @Delete("<script>DELETE FROM user_role WHERE uid=#{uid} AND rid IN " +
            "(SELECT rid FROM roles WHERE roleName IN " +
            "<foreach collection='roleNames' item='roleName' separator=',' open='(' close=')'>" +
            "#{roleName}" +
            "</foreach>)</script>")
    Boolean deleteUserRolesByRoleNames(@Param("uid") Long uid,
                                       @Param("roleNames") Collection<String> roleNames);

    /* ----------------------------------------------------------------------------------------------------- */
}
