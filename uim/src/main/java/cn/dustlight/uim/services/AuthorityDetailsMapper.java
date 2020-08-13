package cn.dustlight.uim.services;

import cn.dustlight.uim.models.AuthorityDetails;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheNamespace
@Service
@Mapper
public interface AuthorityDetailsMapper {

    @Select("SELECT * FROM authority_details")
    @Results(id = "AuthorityDetails", value = {
            @Result(property = "name", column = "authority_name")
    })
    List<AuthorityDetails> getAuthorities();

    @Update("UPDATE authority_details SET authority_name=#{name},description=#{description} WHERE id=#{id}")
    boolean updateAuthority(Long id, String name, String description);

    @Delete("DELETE FROM authority_details WHERE id=#{id}")
    boolean deleteAuthority(Long id);

    @Insert("INSERT INTO authority_details(id,authority_name,description) VALUES (#{id},#{name},#{description})")
    boolean insertAuthority(Long id, String name, String description);

    @Select("SELECT authority_name FROM authority_details,client_authority WHERE client_authority.cid=#{clientId} AND authority_details.id=client_authority.aid")
    String[] getAuthoritiesByClientId(String clientId);

    @Select("SELECT authority_name FROM authority_details,scope_authority WHERE scope_authority.sid=#{scopeId} AND authority_details.id=scope_authority.aid")
    String[] getAuthoritiesByScopeId(Long scopeId);

    @Select({"<script>SELECT authority_name FROM authority_details,scope_authority,scope_details WHERE authority_details.id=scope_authority.aid AND scope_details.id=scope_authority.sid AND scope_details.scope_name IN " +
            "<foreach collection='names' item='name' open='(' separator=',' close=')'>",
            "#{name}",
            "</foreach></script>"})
    String[] getAuthoritiesByScopeNames(@Param("names") String[] names);

    @Select("SELECT authority_details.* FROM authority_details,role_authority WHERE role_authority.rid=#{roleId} AND role_authority.aid=authority_details.id")
    @ResultMap("AuthorityDetails")
    List<AuthorityDetails> getRoleAuthorities(Long roleId);

    @Delete("DELETE FROM role_authority WHERE rid=#{roleId} AND aid=#{authorityId}")
    boolean removeRoleAuthority(Long roleId, Long authorityId);

    @Insert("INSERT INTO role_authority SET rid=#{roleId},aid=#{authorityId}")
    boolean insertRoleAuthority(Long roleId, Long authorityId);

    @Delete("DELETE FROM scope_authority WHERE sid=#{scopeId} AND aid=#{authorityId}")
    boolean removeScopeAuthority(Long scopeId, Long authorityId);

    @Insert("INSERT INTO scope_authority SET sid=#{scopeId},aid=#{authorityId}")
    boolean insertScopeAuthority(Long scopeId, Long authorityId);

    @Select("SELECT authority_details.* FROM authority_details,scope_authority WHERE scope_authority.sid=#{scopeId} AND scope_authority.aid=authority_details.id")
    @ResultMap("AuthorityDetails")
    List<AuthorityDetails> getScopeAuthorities(Long scopeId);

    @Select("SELECT authority_name FROM authority_details,role_details,role_authority where role_details.id=#{id} and role_details.id=role_authority.rid and authority_details.id=role_authority.aid")
    String[] getAuthorityByRoleId(Long id);
}
