package cn.dustlight.auth.mappers;

import cn.dustlight.auth.entities.ClientScope;
import cn.dustlight.auth.entities.DefaultClientScope;
import cn.dustlight.auth.entities.DefaultScope;
import cn.dustlight.auth.entities.Scope;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Mapper
public interface ScopeMapper {

    /* ----------------------------------------------------------------------------------------------------- */

    /* Scope */

    @Select("SELECT * FROM scopes")
    Collection<DefaultScope> listScopes();

    @Select("SELECT * FROM scopes WHERE sid=#{sid} LIMIT 1")
    DefaultScope selectScope(@Param("sid") Long sid);

    @Select("<script>SELECT * FROM scopes WHERE sid IN " +
            "<foreach collection='sids' item='sid' open='(' separator=',' close=')'>" +
            "#{sid}</foreach></script>")
    Collection<DefaultScope> selectScopes(@Param("sids") Collection<Long> sids);

    @Insert("INSERT INTO scopes (sid,name,subtitle,description) VALUES " +
            "(#{sid},#{name},#{subtitle},#{description}) " +
            "ON DUPLICATE KEY UPDATE name=VALUES(name),subtitle=VALUES(subtitle),description=VALUES(description)")
    Boolean insertScope(@Param("sid") Long sid,
                        @Param("name") String name,
                        @Param("subtitle") String subtitle,
                        @Param("description") String description);

    @Insert("<script>INSERT INTO scopes (sid,name,subtitle,description) VALUES " +
            "<foreach collection='scopes' item='scope' separator=','>" +
            "(#{scope.sid},#{scope.name},#{scope.subtitle},#{scope.description})" +
            "</foreach> ON DUPLICATE KEY UPDATE name=VALUES(name),subtitle=VALUES(subtitle),description=VALUES(description)</script>")
    Boolean insertScopes(@Param("scopes") Collection<? extends Scope> scopes);

    @Delete("DELETE FROM scopes WHERE sid=#{sid}")
    Boolean deleteScope(@Param("sid") Long sid);

    @Delete("<script>DELETE FROM scopes WHERE sid IN " +
            "<foreach collection='sids' item='sid' open='(' separator=',' close=')'>" +
            "#{sid}</foreach></script>")
    Boolean deleteScopes(@Param("sids") Collection<Long> sids);

    /* ----------------------------------------------------------------------------------------------------- */

    /* ClientScope */

    @Select("SELECT scopes.* FROM scopes,client_scope WHERE client_scope.cid=#{cid} AND client_scope.sid=scopes.sid")
    Collection<DefaultClientScope> listClientScopes(@Param("cid") String cid);

    @Select("SELECT scopes.name FROM scopes,client_scope WHERE client_scope.cid=#{cid} AND client_scopes.sid=scopes.sid")
    Collection<String> listClientScopeNames(@Param("cid") String cid);

    @Select("SELECT sid FROM client_scope WHERE cid=#{cid}")
    Collection<Long> listClientScopeIds(@Param("cid") String cid);

    @Insert("<script>INSERT INTO client_scope (cid,sid,autoApprove) VALUES " +
            "<foreach collection='sids' item='sid' separator=','>" +
            "(#{cid},#{sid},#{autoApprove})" +
            "</foreach> ON DUPLICATE KEY UPDATE autoApprove=VALUES(autoApprove)</script>")
    Boolean insertClientScopeByScopeIds(@Param("cid") String cid,
                                        @Param("sids") Collection<Long> sids,
                                        @Param("autoApprove") boolean autoApprove);

    @Insert("<script>INSERT INTO client_scope (cid,sid,autoApprove) VALUES " +
            "<foreach collection='scopes' item='scope' separator=','>" +
            "(#{scope.cid},#{scope.sid},#{scope.autoApprove})" +
            "</foreach> ON DUPLICATE KEY UPDATE autoApprove=VALUES(autoApprove)</script>")
    Boolean insertClientScopes(@Param("scopes") Collection<? extends ClientScope> scopes);

    @Delete("<script>DELETE FROM client_scope WHERE cid=#{cid} AND sid IN " +
            "<foreach collection='sids' item='sid' open='(' separator=',' close=')'>" +
            "#{sid}</foreach></script>")
    Boolean deleteClientScopes(@Param("cid") String cid,
                               @Param("sids") Collection<Long> sids);

    /* ----------------------------------------------------------------------------------------------------- */
}
