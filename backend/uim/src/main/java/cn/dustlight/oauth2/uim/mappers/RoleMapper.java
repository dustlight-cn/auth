package cn.dustlight.oauth2.uim.mappers;

import cn.dustlight.oauth2.uim.entities.v1.roles.DefaultRole;
import cn.dustlight.oauth2.uim.entities.v1.roles.DefaultUserRole;
import cn.dustlight.oauth2.uim.entities.v1.roles.Role;
import cn.dustlight.oauth2.uim.entities.v1.roles.UserRole;
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

    @Select("<script>SELECT * FROM roles WHERE rid IN " +
            "<foreach collection='rids' item='rid' open='(' separator=',' close=')'>" +
            "#{rid}</foreach></script>")
    Collection<DefaultRole> selectRoles(Collection<Long> rids);

    @Select("SELECT * FROM roles WHERE rid=#{rid}")
    DefaultRole selectRole(Long rid);

    @Insert("INSERT INTO roles (rid,roleName,roleDescription) VALUES (#{rid},#{roleName},#{roleDescription}) " +
            "ON DUPLICATE KEY UPDATE roleName=VALUES(roleName),roleDescription=VALUES(roleDescription)")
    boolean insertRole(Long rid, String roleName, String roleDescription);

    @Insert("<script>INSERT INTO roles (rid,roleName,roleDescription) VALUES " +
            "<foreach collection='roles' item='role' separator=','>" +
            "(#{role.rid},#{role.roleName},#{role.roleDescription})" +
            "</foreach> ON DUPLICATE KEY UPDATE roleName=VALUES(roleName),roleDescription=VALUES(roleDescription)</script>")
    boolean insertRoles(Collection<? extends Role> roles);

    @Delete("DELETE FROM roles WHERE rid=#{rid}")
    boolean deleteRole(Long rid);

    @Delete("<script>DELETE FROM roles WHERE rid IN " +
            "<foreach collection='rids' item='rid' open='(' separator=',' close=')'>" +
            "#{rid}</foreach></script>")
    boolean deleteRoles(Collection<Long> rids);

    /* ----------------------------------------------------------------------------------------------------- */

    /* UserRole */

    @Select("SELECT uid,r.rid AS rid,roleName,roleDescription,expiredAt FROM user_role AS ur,roles AS r " +
            "WHERE ur.uid=#{uid} AND ur.rid=r.rid")
    @Results(id = "UserRole", value = {
            @Result(column = "rid", property = "rid"),
            @Result(column = "rid",
                    property = "authorities",
                    many = @Many(select = "cn.dustlight.oauth2.uim.mappers.AuthorityMapper.listRoleAuthorities"))
    })
    Collection<DefaultUserRole> listUserRoles(Long uid);

    @Select("SELECT roleName FROM user_role AS ur,roles AS r " +
            "WHERE ur.uid=#{uid} AND ur.rid=r.rid")
    Collection<String> listUserRoleNames(Long uid);

    @Select("SELECT r.rid FROM user_role AS ur,roles AS r " +
            "WHERE ur.uid=#{uid} AND ur.rid=r.rid")
    Collection<Long> listUserRoleIds(Long uid);

    @Insert("<script>INSERT INTO user_role (uid,rid,expiredAt) VALUES " +
            "<foreach collection='roles' item='role' separator=','>(#{uid},#{role},#{expiredAt})</foreach>" +
            "ON DUPLICATE KEY UPDATE expiredAt=#{expiredAt}</script>")
    boolean insertUserRolesByRoleIds(Long uid, Collection<Long> roles, Date expiredAt);

    @Insert("<script>INSERT INTO user_role (uid,rid,expiredAt) VALUES " +
            "<foreach collection='roles' item='role' separator=','>(#{uid},<choose>" +
            "<when test='role.rid'>#{role.rid}</when>" + // 如果roleId存在
            "<otherwise>(SELECT rid FROM roles WHERE roleName=#{role.roleName} LIMIT 1)</otherwise>" + // 不存在则查询
            "</choose>,#{role.expiredAt})</foreach>" +
            " ON DUPLICATE KEY UPDATE expiredAt=VALUES(expiredAt)</script>")
    <T extends UserRole> boolean insertUserRoles(Long uid, Collection<T> roles);

    @Insert("<script>INSERT INTO user_role (uid,rid,expiredAt) " +
            "(SELECT #{uid} as uid,rid,#{expiredAt} as expiredAt FROM roles WHERE roleName IN " +
            "<foreach collection='roleNames' item='roleName' separator=',' open='(' close=')'>#{roleName}</foreach>)" +
            "ON DUPLICATE KEY UPDATE expiredAt=#{expiredAt}</script>")
    boolean insertUserRolesByRoleNames(Long uid, Collection<String> roleNames, Date expiredAt);

    @Delete("<script>DELETE FROM user_role WHERE uid=#{uid} AND rid IN " +
            "<foreach collection='roles' item='role' separator=',' open='(' close=')'>" +
            "#{role}" +
            "</foreach></script>")
    boolean deleteUserRolesByRoleIds(Long uid, Collection<Long> roles);

    @Delete("<script>DELETE FROM user_role WHERE uid=#{uid} AND rid IN " +
            "(SELECT rid FROM roles WHERE roleName IN " +
            "<foreach collection='roleNames' item='roleName' separator=',' open='(' close=')'>" +
            "#{roleName}" +
            "</foreach>)</script>")
    boolean deleteUserRolesByRoleNames(Long uid, Collection<String> roleNames);

    /* ----------------------------------------------------------------------------------------------------- */
}
