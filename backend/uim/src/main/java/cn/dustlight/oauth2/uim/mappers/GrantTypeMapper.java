package cn.dustlight.oauth2.uim.mappers;

import cn.dustlight.oauth2.uim.entities.v1.types.DefaultGrantType;
import cn.dustlight.oauth2.uim.entities.v1.types.GrantType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Mapper
public interface GrantTypeMapper {

    /* ----------------------------------------------------------------------------------------------------- */

    /* GrantType */

    @Select("SELECT * FROM types")
    Collection<DefaultGrantType> listGrantTypes();

    @Select("SELECT * FROM types WHERE tid=#{tid} LIMIT 1")
    DefaultGrantType selectGrantType(Long tid);

    @Select("<script>SELECT * FROM types WHERE tid IN " +
            "<foreach collection='tids' item='tid' open='(' separator=',' close=')'>" +
            "#{tid}</foreach></script>")
    Collection<DefaultGrantType> selectGrantTypes(Collection<Long> tids);

    @Insert("INSERT INTO types (tid,name,description) VALUES (#{tid},#{name},#{description}) " +
            "ON DUPLICATE KEY UPDATE name=VALUES(name),description=VALUES(description)")
    boolean insertType(Long tid, String name, String description);

    @Insert("<script>INSERT INTO types (tid,name,description) VALUES " +
            "<foreach collection='types' item='type' separator=','>" +
            "(#{type.tid},#{type.name},#{type.description})" +
            "</foreach> ON DUPLICATE KEY UPDATE name=VALUES(name),description=VALUES(description)</script>")
    boolean insertTypes(Collection<? extends GrantType> types);

    @Delete("DELETE FROM types WHERE tid=#{tid}")
    boolean deleteType(Long tid);

    @Delete("<script>DELETE FROM types WHERE tid IN " +
            "<foreach collection='tids' item='tid' open='(' separator=',' close=')'>" +
            "#{tid}</foreach></script>")
    boolean deleteTypes(Collection<Long> tids);

    /* ----------------------------------------------------------------------------------------------------- */

    /* ClientGrantType */

    @Select("SELECT types.* FROM types,client_type WHERE client_type.cid=#{cid} AND client_type.tid=types.tid")
    Collection<DefaultGrantType> listClientGrantTypes(String cid);

    @Insert("<script>INSERT IGNORE INTO client_type (cid,tid) VALUES " +
            "<foreach collection='tids' item='tid' separator=','>" +
            "(#{cid},#{tid})</foreach></script>")
    boolean insertClientGrantTypes(String cid, Collection<Long> tids);

    @Delete("<script>DELETE FROM client_type WHERE cid=#{cid} AND tid IN " +
            "<foreach collection='tids' item='tid' open='(' separator=',' close=')'>" +
            "#{tid}</foreach></script>")
    boolean deleteClientGrantTypes(String cid, Collection<Long> tids);

    /* ----------------------------------------------------------------------------------------------------- */

}
