package cn.dustlight.uim.services;

import cn.dustlight.uim.models.ScopeDetails;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheNamespace
@Service
@Mapper
public interface ScopeDetailsMapper {


    @Select("SELECT * FROM scope_details")
    @Results(id = "ScopeDetails", value = {
            @Result(property = "name", column = "scope_name")
    })
    List<ScopeDetails> getScopes();

    @Insert("INSERT INTO scope_details(id,scope_name,description) VALUES(#{id},#{scopeName},#{description})")
    boolean insertScope(Long id, String scopeName, String description);

    @Delete("DELETE FROM scope_details WHERE id=#{id}")
    boolean removeScope(Long id);

    @Update("UPDATE scope_details SET id=#{id},scope_name=#{name},description=#{description} WHERE id=#{id}")
    boolean updateScope(Long id, String name, String description);


    @Insert("<script>INSERT IGNORE INTO client_scope(cid,sid) VALUES" +
            "<foreach collection='scopes' item='scope' separator=','>" +
            "(#{clientId},#{scope})" +
            "</foreach></script>")
    boolean insertClientScopes(@Param("clientId") String clientId, @Param("scopes") List<Long> Scopes);

    @Delete("<script>DELETE FROM client_scope " +
            "WHERE cid=#{clientId} AND sid IN (" +
            "<foreach collection='scopes' item='scope' separator=','>" +
            "#{scope}" +
            "</foreach>)</script>")
    boolean deleteClientScopes(@Param("clientId") String clientId, @Param("scopes") List<Long> Scopes);
}
