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

    @Select("SELECT * FROM authorities WHERE cid=#{cid}")
    Collection<DefaultAuthority> listAuthoritiesWithClientId(@Param("cid") String cid);

    @Select("SELECT * FROM authorities WHERE aid=#{aid} LIMIT 1")
    DefaultAuthority selectAuthority(@Param("aid") Long aid);

    @Select("<script>SELECT * FROM authorities WHERE aid IN " +
            "<foreach collection='aids' item='aid' open='(' separator=',' close=')'>" +
            "#{aid}</foreach></script>")
    Collection<DefaultAuthority> selectAuthorities(@Param("aids") Collection<Long> aids);

    @Insert("INSERT INTO authorities (aid,authorityName,authorityDescription) VALUES (#{aid},#{authorityName},#{authorityDescription}) " +
            "ON DUPLICATE KEY UPDATE authorityName=VALUES(authorityName),authorityDescription=VALUES(authorityDescription)")
    Boolean insertAuthority(@Param("aid") Long aid,
                            @Param("authorityName") String authorityName,
                            @Param("authorityDescription") String authorityDescription);

    @Insert("<script>INSERT INTO authorities (aid,authorityName,authorityDescription,cid) VALUES " +
            "<foreach collection='authorities' item='authority' separator=','>" +
            "(#{authority.aid},#{authority.authorityName},#{authority.authorityDescription},#{authority.cid})" +
            "</foreach> ON DUPLICATE KEY UPDATE authorityName=VALUES(authorityName),authorityDescription=VALUES(authorityDescription),cid=VALUES(cid)</script>")
    Boolean insertAuthorities(@Param("authorities") Collection<? extends Authority> authorities);

    @Delete("DELETE FROM authorities WHERE aid=#{aid}")
    Boolean deleteAuthority(@Param("aid") Long aid);

    @Delete("DELETE FROM authorities WHERE aid=#{aid} AND cid=#{cid}")
    Boolean deleteAuthorityWithClientId(@Param("aid") Long aid, @Param("cid") String cid);

    @Delete("<script>DELETE FROM authorities WHERE aid IN " +
            "<foreach collection='aids' item='aid' open='(' separator=',' close=')'>" +
            "#{aid}</foreach></script>")
    Boolean deleteAuthorities(@Param("aids") Collection<Long> aids);

    @Delete("<script>DELETE FROM authorities WHERE cid=#{cid} AND aid IN " +
            "<foreach collection='aids' item='aid' open='(' separator=',' close=')'>" +
            "#{aid}</foreach></script>")
    Boolean deleteAuthoritiesWithClientId(@Param("aids") Collection<Long> aids, @Param("cid") String cid);

    /* ------------------------------------------------------------------------------------------------ */

    /* RoleAuthority */

    @Select("SELECT authorityName FROM role_authority AS ra,authorities AS a " +
            "WHERE ra.rid=#{rid} AND ra.aid=a.aid")
    Collection<String> listRoleAuthorities(@Param("rid") Long rid);

    @Insert("<script>INSERT IGNORE INTO role_authority (rid,aid) VALUES " +
            "<foreach collection='aids' item='aid' separator=','>" +
            "(#{rid},#{aid})</foreach></script>")
    Boolean insertRoleAuthorities(@Param("rid") Long rid,
                                  @Param("aids") Collection<Long> aids);

    @Delete("<script>DELETE FROM role_authority WHERE rid=#{rid} AND aid IN " +
            "<foreach collection='aids' item='aid' open='(' separator=',' close=')'>" +
            "#{aid}</foreach></script>")
    Boolean deleteRoleAuthorities(@Param("rid") Long rid,
                                  @Param("aids") Collection<Long> aids);

    /* ------------------------------------------------------------------------------------------------ */

    /* ClientAuthority */

    @Select("SELECT authorityName FROM client_authority AS ca,authorities AS a " +
            "WHERE ca.cid=#{cid} AND ca.aid=a.aid")
    Collection<String> listClientAuthorities(@Param("cid") String cid);

    @Insert("<script>INSERT IGNORE INTO client_authority (cid,aid) VALUES " +
            "<foreach collection='aids' item='aid' separator=','>" +
            "(#{cid},#{aid})</foreach></script>")
    Boolean insertClientAuthorities(@Param("cid") String cid,
                                    @Param("aids") Collection<Long> aids);

    @Delete("<script>DELETE FROM client_authority WHERE cid=#{cid} AND aid IN " +
            "<foreach collection='aids' item='aid' open='(' separator=',' close=')'>" +
            "#{aid}</foreach></script>")
    Boolean deleteClientAuthorities(@Param("cid") String cid,
                                    @Param("aids") Collection<Long> aids);
}
