package cn.dustlight.auth.mappers;

import cn.dustlight.auth.entities.DefaultGrantType;
import cn.dustlight.auth.entities.GrantType;
import org.apache.ibatis.annotations.*;
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
    DefaultGrantType selectGrantType(@Param("tid") Long tid);

    @Select("<script>SELECT * FROM types WHERE tid IN " +
            "<foreach collection='tids' item='tid' open='(' separator=',' close=')'>" +
            "#{tid}</foreach></script>")
    Collection<DefaultGrantType> selectGrantTypes(@Param("tids") Collection<Long> tids);

    @Select("<script>SELECT * FROM types WHERE name IN " +
            "<foreach collection='names' item='name' open='(' separator=',' close=')'>" +
            "#{name}</foreach></script>")
    Collection<DefaultGrantType> selectGrantTypesByNames(@Param("names") Collection<String> names);

    @Insert("INSERT INTO types (tid,name,description) VALUES (#{tid},#{name},#{description}) " +
            "ON DUPLICATE KEY UPDATE name=VALUES(name),description=VALUES(description)")
    Boolean insertType(@Param("tid") Long tid,
                       @Param("name") String name,
                       @Param("description") String description);

    @Insert("<script>INSERT INTO types (tid,name,description) VALUES " +
            "<foreach collection='types' item='type' separator=','>" +
            "(#{type.tid},#{type.name},#{type.description})" +
            "</foreach> ON DUPLICATE KEY UPDATE name=VALUES(name),description=VALUES(description)</script>")
    Boolean insertTypes(@Param("types") Collection<? extends GrantType> types);

    @Delete("DELETE FROM types WHERE tid=#{tid}")
    Boolean deleteType(@Param("tid") Long tid);

    @Delete("<script>DELETE FROM types WHERE tid IN " +
            "<foreach collection='tids' item='tid' open='(' separator=',' close=')'>" +
            "#{tid}</foreach></script>")
    Boolean deleteTypes(@Param("tids") Collection<Long> tids);

    /* ----------------------------------------------------------------------------------------------------- */

    /* ClientGrantType */

    @Select("SELECT types.* FROM types,client_type WHERE client_type.cid=#{cid} AND client_type.tid=types.tid")
    Collection<DefaultGrantType> listClientGrantTypes(@Param("cid") String cid);

    @Insert("<script>INSERT IGNORE INTO client_type (cid,tid) VALUES " +
            "<foreach collection='tids' item='tid' separator=','>" +
            "(#{cid},#{tid})</foreach></script>")
    Boolean insertClientGrantTypes(@Param("cid") String cid,
                                   @Param("tids") Collection<Long> tids);

    @Delete("<script>DELETE FROM client_type WHERE cid=#{cid} AND tid IN " +
            "<foreach collection='tids' item='tid' open='(' separator=',' close=')'>" +
            "#{tid}</foreach></script>")
    Boolean deleteClientGrantTypes(@Param("cid") String cid,
                                   @Param("tids") Collection<Long> tids);

    /* ----------------------------------------------------------------------------------------------------- */

}
