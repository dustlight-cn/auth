package cn.dustlight.oauth2.uim.mappers;

import cn.dustlight.oauth2.uim.entities.v1.scopes.DefaultClientScope;
import cn.dustlight.oauth2.uim.entities.v1.scopes.DefaultScope;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Mapper
public interface ScopeMapper {

    @Select("SELECT * FROM scopes")
    Collection<DefaultScope> listScopes();

    @Select("SELECT scopes.* FROM scopes,client_scope WHERE client_scope.cid=#{cid} AND client_scope.sid=scopes.sid")
    Collection<DefaultClientScope> listClientScopes(String cid);

    @Select("SELECT * FROM scopes WHERE sid=#{sid}")
    DefaultScope selectScope(Long sid);

    @Select("<script>SELECT * FROM scopes WHERE sid IN " +
            "<foreach collection='sids' item='sid' open='(' separator=',' close=')'>" +
            "#{sid}</foreach></script>")
    Collection<DefaultScope> selectScopes(Collection<Long> sids);

    @Insert("INSERT INTO scopes (sid,name,subtitle,description) VALUES " +
            "(#{sid},#{name},#{subtitle},#{description}) " +
            "ON DUPLICATE KEY UPDATE name=VALUES(name),subtitle=VALUES(subtitle),description=VALUES(description)")
    boolean insertScope(Long sid, String name, String subtitle, String description);

    @Insert("<script>INSERT INTO scopes (sid,name,subtitle,description) VALUES " +
            "<foreach collection='scopes' item='scope' separator=','>" +
            "(#{scope.sid},#{scope.name},#{scope.subtitle},#{scope.description})" +
            "</foreach> ON DUPLICATE KEY UPDATE name=VALUES(name),subtitle=VALUES(subtitle),description=VALUES(description)</script>")
    boolean insertScopes(Collection<DefaultScope> scopes);

    @Delete("DELETE FROM scopes WHERE sid=#{sid}")
    boolean deleteScope(Long sid);

    @Delete("<script>DELETE FROM scopes WHERE sid IN " +
            "<foreach collection='sids' item='sid' open='(' separator=',' close=')'>" +
            "#{sid}</foreach></script>")
    boolean deleteScopes(Collection<Long> sids);
}
