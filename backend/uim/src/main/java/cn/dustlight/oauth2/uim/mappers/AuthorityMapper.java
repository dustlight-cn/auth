package cn.dustlight.oauth2.uim.mappers;

import cn.dustlight.oauth2.uim.entities.v1.authorities.Authority;
import cn.dustlight.oauth2.uim.entities.v1.authorities.DefaultAuthority;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Mapper
public interface AuthorityMapper {
    /* ------------------------------------------------------------------------------------------------ */

    /* Authority */

    @Select("SELECT * FROM authorities")
    Collection<DefaultAuthority> listAuthorities();

    @Select("SELECT * FROM authorities WHERE aid=#{aid}")
    DefaultAuthority selectAuthority(Long aid);

    @Select("<script>SELECT * FROM authorities WHERE aid IN " +
            "<foreach collection='aids' item='aid' open='(' separator=',' close=')'>" +
            "#{aid}</foreach></script>")
    Collection<DefaultAuthority> selectAuthorities(Collection<Long> aids);

    @Insert("INSERT INTO authorities (aid,authorityName,authorityDescription) VALUES (#{aid},#{authorityName},#{authorityDescription}) " +
            "ON DUPLICATE KEY UPDATE authorityName=VALUES(authorityName),authorityDescription=VALUES(authorityDescription)")
    boolean insertAuthority(Long aid, String authorityName, String authorityDescription);

    @Insert("<script>INSERT INTO authorities (aid,authorityName,authorityDescription) VALUES " +
            "<foreach collection='authorities' item='authority' separator=','>" +
            "(#{authority.aid},#{authority.authorityName},#{authority.authorityDescription})" +
            "</foreach> ON DUPLICATE KEY UPDATE authorityName=VALUES(authorityName),authorityDescription=VALUES(authorityDescription)</script>")
    boolean insertAuthorities(Collection<Authority> authorities);

    @Delete("DELETE FROM authorities WHERE aid=#{aid}")
    boolean deleteAuthority(Long aid);

    @Delete("<script>DELETE FROM authorities WHERE aid IN " +
            "<foreach collection='aids' item='aid' open='(' separator=',' close=')'>" +
            "#{aid}</foreach></script>")
    boolean deleteAuthorities(Collection<Long> aids);

    /* ------------------------------------------------------------------------------------------------ */

    /* RoleAuthority */

    @Select("SELECT authorityName FROM role_authority AS ra,authorities AS a " +
            "WHERE ra.rid=#{rid} AND ra.aid=a.aid")
    Collection<String> listRoleAuthorities(Long rid);

    /* ------------------------------------------------------------------------------------------------ */

    /* ScopeAuthority */

    @Select("<script>SELECT * FROM authorities as a,scope_authority as sa WHERE a.aid=sa.aid AND sa.sid IN " +
            "(SELECT sid FROM scopes WHERE name IN " +
            "<foreach collection='scopeNames' item='scopeName' separator=',' open='(' close=')'>#{scopeName}</foreach>" +
            ")</script>")
    Collection<String> listScopeAuthorities(Collection<String> scopeNames);

    /* ------------------------------------------------------------------------------------------------ */

}
