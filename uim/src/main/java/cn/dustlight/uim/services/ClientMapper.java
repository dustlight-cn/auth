package cn.dustlight.uim.services;

import cn.dustlight.uim.models.AuthorityDetails;
import cn.dustlight.uim.models.ClientDetails;
import cn.dustlight.uim.models.ScopeDetails;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheNamespace
@Service
@Mapper
public interface ClientMapper {

    @Select("SELECT * FROM authority_details")
    @Results(id = "AuthorityDetails", value = {
            @Result(property = "name", column = "authority_name")
    })
    List<AuthorityDetails> getAuthorities();

//    boolean updateAuthority(Long authorit);

    @Select("SELECT * FROM scope_details")
    @Results(id = "ScopeDetails", value = {
            @Result(property = "name", column = "scope_name")
    })
    List<ScopeDetails> getScopes();

    @Select("SELECT authority_name FROM authority_details,client_authority WHERE client_authority.cid=#{clientId} AND authority_details.id=client_authority.aid")
    String[] getAuthoritiesByClientId(String clientId);

    @Select("SELECT authority_name FROM authority_details,scope_authority WHERE scope_authority.sid=#{scopeId} AND authority_details.id=scope_authority.aid")
    String[] getAuthoritiesByScopeId(Long scopeId);

    @Select({"<script>SELECT authority_name FROM authority_details,scope_authority,scope_details WHERE authority_details.id=scope_authority.aid AND scope_details.id=scope_authority.sid AND scope_details.scope_name IN " +
            "<foreach collection='names' item='name' open='(' separator=',' close=')'>",
            "#{name}",
            "</foreach></script>"})
    String[] getAuthoritiesByScopeNames(@Param("names") String[] names);

    @Select("SELECT grant_type FROM grant_types,client_grant_types WHERE client_grant_types.cid=#{clientId} AND grant_types.id=client_grant_types.tid")
    List<String> getClientGrantTypesByClientId(String clientId);

    @Select("SELECT resource_name FROM resource_details,client_resource WHERE client_resource.cid=#{clientId} AND resource_details.id=client_resource.rid")
    List<String> getClientResourceIdsByClientId(String clientId);

    @Select("SELECT scope_name,auto_approve,scope_details.description as des FROM scope_details,client_scope WHERE client_scope.cid=#{clientId} AND scope_details.id=client_scope.sid")
    @Results(id = "ClientScope", value = {
            @Result(property = "scopeName", column = "scope_name"),
            @Result(property = "autoApprove", column = "auto_approve"),
            @Result(property = "scopeDescription", column = "des")
    })
    List<ClientDetails.ClientScope> getClientScopeByClientId(String clientId);

    @Select("SELECT client_id,uid,client_secret,client_name,redirect_uri,access_token_validity,refresh_token_validity,additional_information,enabled,description,createdAt,updatedAt FROM oauth_client_details WHERE client_id=#{clientId}")
    @Results(id = "ClientDetails", value = {
            @Result(property = "clientId", column = "client_id", id = true),
            @Result(property = "uid", column = "uid"),
            @Result(property = "clientSecret", column = "client_secret"),
            @Result(property = "clientName", column = "client_name"),
            @Result(property = "resourceIds", column = "client_id", many = @Many(select = "cn.dustlight.uim.services.ClientMapper.getClientResourceIdsByClientId")),
            @Result(property = "scope", column = "client_id", many = @Many(select = "cn.dustlight.uim.services.ClientMapper.getClientScopeByClientId")),
            @Result(property = "authorizedGrantTypes", column = "client_id", many = @Many(select = "cn.dustlight.uim.services.ClientMapper.getClientGrantTypesByClientId")),
            @Result(property = "authorities", column = "client_id", many = @Many(select = "cn.dustlight.uim.services.ClientMapper.getAuthoritiesByClientId")),
            @Result(property = "redirectUri", column = "redirect_uri"),
            @Result(property = "accessTokenValidity", column = "access_token_validity"),
            @Result(property = "refreshTokenValidity", column = "refresh_token_validity"),
            @Result(property = "additionalInformation", column = "additional_information"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "description", column = "description"),
            @Result(property = "createdAt", column = "createdAt"),
            @Result(property = "updatedAt", column = "updatedAt")
    })
    ClientDetails loadClientByClientId(String clientId);

    @Select("SELECT client_id,client_name,description FROM oauth_client_details WHERE client_id=#{clientId}")
    @ResultMap("ClientDetails")
    ClientDetails loadClientDescription(String clientId);

    @Select("SELECT client_id,uid,client_name,redirect_uri,access_token_validity,refresh_token_validity,additional_information,enabled,description,createdAt,updatedAt FROM oauth_client_details WHERE uid=#{uid}")
    @ResultMap("ClientDetails")
    List<ClientDetails> loadClientsByUserId(Long uid);

    @Select("SELECT authority_details.* FROM authority_details,role_authority WHERE role_authority.rid=#{roleId} AND role_authority.aid=authority_details.id")
    @ResultMap("AuthorityDetails")
    List<AuthorityDetails> getRoleAuthorities(Long roleId);

    @Select("SELECT authority_details.* FROM authority_details,scope_authority WHERE scope_authority.sid=#{scopeId} AND scope_authority.aid=authority_details.id")
    @ResultMap("AuthorityDetails")
    List<AuthorityDetails> getScopeAuthorities(Long scopeId);
//
//    //    @Insert("INSERT INTO oauth_client_details " +
////            "(client_id,uid,client_name,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,additional_information,autoapprove) VALUES " +
////            "(#{clientId},#{uid},#{clientName},#{resourceIds},#{clientSecret},#{scope},#{authorizedGrantTypes},#{redirectUri},#{authorities},#{additionalInformation},#{autoApprove})")
//    boolean insertClient(String clientId,
//                         long uid,
//
//                         String clientName,
//                         String resourceIds,
//                         String clientSecret,
//                         Set<String> scope,
//                         String authorizedGrantTypes,
//                         Set<String> redirectUri,
//                         String authorities,
//                         String additionalInformation,
//                         String autoApprove);
//
//    //    @Select("SELECT uid,client_id,client_name,scope,web_server_redirect_uri FROM oauth_client_details WHERE client_id=#{clientId}")
////    @Results(value = {
////            @Result(property = "uid", column = "uid"),
////            @Result(property = "appKey", column = "client_id"),
////            @Result(property = "appName", column = "client_name"),
////            @Result(property = "scope", column = "scope"),
////            @Result(property = "redirectUri", column = "web_server_redirect_uri")
////    }, id = "AppDetails")
//    ClientDetails getClient(String clientId);
//
//    @Delete("DELETE FROM oauth_client_details WHERE client_id=#{clientId}")
//    boolean deleteClient(String clientId);
//
//    @Update("UPDATE oauth_client_details SET client_secret=#{clientSecret} WHERE client_id=#{clientId}")
//    boolean updateSecret(String clientId, String clientSecret);
//
//    @Update("UPDATE oauth_client_details SET client_name=#{clientName} WHERE client_id=#{clientId}")
//    boolean updateName(String clientId, String clientName);
//
//    @Update("UPDATE oauth_client_details SET scope=#{scope} WHERE client_id=#{clientId}")
//    boolean updateScope(String clientId, String scope);
//
//    @Update("UPDATE oauth_client_details SET web_server_redirect_uri=#{redirectUri} WHERE client_id=#{clientId}")
//    boolean updateRedirectUri(String clientId, String redirectUri);
//
//    //    @Select("SELECT uid,client_id,client_name,scope,web_server_redirect_uri FROM oauth_client_details")
////    @ResultMap("AppDetails")
//    List<ClientDetails> getAllClients();
//
//    //    @Select("SELECT uid,client_id,client_name,scope,web_server_redirect_uri FROM oauth_client_details WHERE uid=#{uid}")
////    @ResultMap("AppDetails")
//    List<ClientDetails> getClientsByUid(long uid);
//
//    //    @Select("SELECT client_id,client_name,scope,web_server_redirect_uri FROM oauth_client_details,user_details WHERE oauth_client_details.uid=user_details.uid and user_details.username=#{username}")
////    @ResultMap("AppDetails")
//    List<ClientDetails> getClientsByUsername(String username);
}
