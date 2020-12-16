package cn.dustlight.auth.mappers;

import cn.dustlight.auth.entities.Authority;
import cn.dustlight.auth.entities.DefaultAuthority;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Mapper
public interface AuthorityMapper {
    /* ------------------------------------------------------------------------------------------------ */

    /* Authority */

    @Select("SELECT * FROM authorities")
    Collection<DefaultAuthority> listAuthorities();

    @Select("SELECT * FROM authorities WHERE aid=#{aid} LIMIT 1")
    DefaultAuthority selectAuthority(@Param("aid") Long aid);

    @Select("<script>SELECT * FROM authorities WHERE aid IN " +
            "<foreach collection='aids' item='aid' open='(' separator=',' close=')'>" +
            "#{aid}</foreach></script>")
    Collection<DefaultAuthority> selectAuthorities(@Param("aids") Collection<Long> aids);

    @Insert("INSERT INTO authorities (aid,authorityName,authorityDescription) VALUES (#{aid},#{authorityName},#{authorityDescription}) " +
            "ON DUPLICATE KEY UPDATE authorityName=VALUES(authorityName),authorityDescription=VALUES(authorityDescription)")
    boolean insertAuthority(@Param("aid") Long aid,
                            @Param("authorityName") String authorityName,
                            @Param("authorityDescription") String authorityDescription);

    @Insert("<script>INSERT INTO authorities (aid,authorityName,authorityDescription) VALUES " +
            "<foreach collection='authorities' item='authority' separator=','>" +
            "(#{authority.aid},#{authority.authorityName},#{authority.authorityDescription})" +
            "</foreach> ON DUPLICATE KEY UPDATE authorityName=VALUES(authorityName),authorityDescription=VALUES(authorityDescription)</script>")
    boolean insertAuthorities(@Param("authorities") Collection<? extends Authority> authorities);

    @Delete("DELETE FROM authorities WHERE aid=#{aid}")
    boolean deleteAuthority(@Param("aid") Long aid);

    @Delete("<script>DELETE FROM authorities WHERE aid IN " +
            "<foreach collection='aids' item='aid' open='(' separator=',' close=')'>" +
            "#{aid}</foreach></script>")
    boolean deleteAuthorities(@Param("aids") Collection<Long> aids);

    /* ------------------------------------------------------------------------------------------------ */

    /* RoleAuthority */

    @Select("SELECT authorityName FROM role_authority AS ra,authorities AS a " +
            "WHERE ra.rid=#{rid} AND ra.aid=a.aid")
    Collection<String> listRoleAuthorities(@Param("rid") Long rid);

    @Insert("<script>INSERT IGNORE INTO role_authority (rid,aid) VALUES " +
            "<foreach collection='aids' item='aid' separator=','>" +
            "(#{rid},#{aid})</foreach></script>")
    boolean insertRoleAuthorities(@Param("rid") Long rid,
                                  @Param("aids") Collection<Long> aids);

    @Delete("<script>DELETE FROM role_authority WHERE rid=#{rid} AND aid IN " +
            "<foreach collection='aids' item='aid' open='(' separator=',' close=')'>" +
            "#{aid}</foreach></script>")
    boolean deleteRoleAuthorities(@Param("rid") Long rid,
                                  @Param("aids") Collection<Long> aids);

    /* ------------------------------------------------------------------------------------------------ */

    /* ScopeAuthority */

    @Select("<script>SELECT * FROM authorities as a,scope_authority as sa WHERE a.aid=sa.aid AND sa.sid IN " +
            "(SELECT sid FROM scopes WHERE name IN " +
            "<foreach collection='scopeNames' item='scopeName' separator=',' open='(' close=')'>#{scopeName}</foreach>" +
            ")</script>")
    Collection<String> listScopeAuthoritiesByScopeNames(@Param("scopeNames") Collection<String> scopeNames);

    @Select("SELECT authorityName FROM scope_authority AS sa,authorities AS a " +
            "WHERE sa.sid=#{sid} AND sa.aid=a.aid")
    Collection<String> listScopeAuthorities(@Param("sid") Long sid);

    @Insert("<script>INSERT IGNORE INTO scope_authority (sid,aid) VALUES " +
            "<foreach collection='aids' item='aid' separator=','>" +
            "(#{sid},#{aid})</foreach></script>")
    boolean insertScopeAuthorities(@Param("sid") Long sid,
                                   @Param("aids") Collection<Long> aids);

    @Delete("<script>DELETE FROM scope_authority WHERE sid=#{sid} AND aid IN " +
            "<foreach collection='aids' item='aid' open='(' separator=',' close=')'>" +
            "#{aid}</foreach></script>")
    boolean deleteScopeAuthorities(@Param("sid") Long sid,
                                   @Param("aids") Collection<Long> aids);

    /* ------------------------------------------------------------------------------------------------ */

}
