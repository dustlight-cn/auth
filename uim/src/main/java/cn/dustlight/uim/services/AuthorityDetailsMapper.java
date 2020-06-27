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


    @Select("SELECT authority_details.* FROM authority_details,scope_authority WHERE scope_authority.sid=#{scopeId} AND scope_authority.aid=authority_details.id")
    @ResultMap("AuthorityDetails")
    List<AuthorityDetails> getScopeAuthorities(Long scopeId);


    @Select("SELECT authority_name FROM authority_details,role_details,role_authority where role_details.id=#{id} and role_details.id=role_authority.rid and authority_details.id=role_authority.aid")
    String[] getAuthorityByRoleId(Long id);

}
