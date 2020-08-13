package cn.dustlight.uim.services;

import cn.dustlight.uim.models.GrantType;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheNamespace
@Service
@Mapper
public interface GrantTypeMapper {

    @Select("SELECT grant_type FROM client_grant_types,grant_types WHERE client_grant_types.cid=#{clientId} AND grant_types.id=client_grant_types.tid")
    List<String> getClientGrantTypesByClientId(String clientId);

    @Insert("<script>INSERT IGNORE INTO client_grant_types(cid,tid) VALUES" +
            "<foreach collection='types' item='type' separator=','>" +
            "(#{clientId},#{type})" +
            "</foreach></script>")
    boolean insertClientGrantTypes(@Param("clientId") String clientId, @Param("types") List<Long> types);

    @Delete("<script>DELETE FROM client_grant_types " +
            "WHERE cid=#{clientId} AND tid IN (" +
            "<foreach collection='types' item='type' separator=','>" +
            "#{type}" +
            "</foreach>)</script>")
    boolean deleteClientGrantTypes(@Param("clientId") String clientId, @Param("types") List<Long> types);

    @Select("SELECT id,grant_type FROM grant_types")
    @Results(id = "GrantType", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "grant_type")
    })
    List<GrantType> getGrantTypes();
}
