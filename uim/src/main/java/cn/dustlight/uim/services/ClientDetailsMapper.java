package cn.dustlight.uim.services;

import cn.dustlight.uim.models.ClientDetails.ClientScope;
import cn.dustlight.uim.models.ClientDetails;
import cn.dustlight.uim.models.GrantType;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheNamespace
@Service
@Mapper
public interface ClientDetailsMapper {

    @Select("SELECT sid,scope_name,auto_approve,scope_details.description as des FROM scope_details,client_scope WHERE client_scope.cid=#{clientId} AND scope_details.id=client_scope.sid")
    @Results(id = "ClientScope", value = {
            @Result(property = "id", column = "sid"),
            @Result(property = "scopeName", column = "scope_name"),
            @Result(property = "autoApprove", column = "auto_approve"),
            @Result(property = "scopeDescription", column = "des")
    })
    List<ClientScope> getClientScopeByClientId(String clientId);

    @Select("SELECT client_id,uid,client_secret,client_name,redirect_uri,access_token_validity,refresh_token_validity,additional_information,enabled,description,createdAt,updatedAt FROM oauth_client_details WHERE client_id=#{clientId}")
    @Results(id = "ClientDetails", value = {
            @Result(property = "clientId", column = "client_id", id = true),
            @Result(property = "uid", column = "uid"),
            @Result(property = "clientSecret", column = "client_secret"),
            @Result(property = "clientName", column = "client_name"),
            @Result(property = "resourceIds", column = "client_id", many = @Many(select = "cn.dustlight.uim.services.ResourceDetailsMapper.getClientResourceIdsByClientId")),
            @Result(property = "scope", column = "client_id", many = @Many(select = "cn.dustlight.uim.services.ClientDetailsMapper.getClientScopeByClientId")),
            @Result(property = "authorizedGrantTypes", column = "client_id", many = @Many(select = "cn.dustlight.uim.services.ClientDetailsMapper.getClientGrantTypesByClientId")),
            @Result(property = "authorities", column = "client_id", many = @Many(select = "cn.dustlight.uim.services.AuthorityDetailsMapper.getAuthoritiesByClientId")),
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

    @Insert("INSERT INTO oauth_client_details(client_id,uid,client_secret,client_name,redirect_uri,access_token_validity,refresh_token_validity,additional_information,enabled,description) VALUES" +
            "(#{clientId},#{uid},#{clientSecret},#{clientName},#{redirectUri},#{accessTokenValidity},#{refreshTokenValidity},#{additional},#{enabled},#{description})")
    boolean insertClient(String clientId, Long uid, String clientSecret, String clientName, String redirectUri, Integer accessTokenValidity, Integer refreshTokenValidity, String additional, boolean enabled, String description);

    @Delete("DELETE FROM oauth_client_details WHERE client_id=#{clientId}")
    boolean deleteClient(String clientId);

    @Delete("DELETE FROM oauth_client_details WHERE client_id=#{clientId} AND uid=#{uid}")
    boolean deleteClientWithUid(String clientId, Long uid);

    @Update("UPDATE oauth_client_details SET client_secret=#{clientSecret} WHERE client_id=#{clientId}")
    boolean updateClientSecret(String clientId, String clientSecret);

    @Update("UPDATE oauth_client_details SET client_secret=#{clientSecret} WHERE client_id=#{clientId} AND uid=#{uid}")
    boolean updateClientSecretWithUid(String clientId, String clientSecret, Long uid);

    @Update("UPDATE oauth_client_details SET client_name=#{clientName} WHERE client_id=#{clientId}")
    boolean updateClientName(String clientId, String clientName);

    @Update("UPDATE oauth_client_details SET client_name=#{clientName} WHERE client_id=#{clientId} AND uid=#{uid}")
    boolean updateClientNameWithUid(String clientId, String clientName, Long uid);

    @Update("UPDATE oauth_client_details SET description=#{description} WHERE client_id=#{clientId}")
    boolean updateClientDescription(String clientId, String description);

    @Update("UPDATE oauth_client_details SET description=#{description} WHERE client_id=#{clientId} AND uid=#{uid}")
    boolean updateClientDescriptionWithUid(String clientId, String description, Long uid);

    @Update("UPDATE oauth_client_details SET redirect_uri=#{redirectUri} WHERE client_id=#{clientId}")
    boolean updateClientRedirectUri(String clientId, String redirectUri);

    @Update("UPDATE oauth_client_details SET redirect_uri=#{redirectUri} WHERE client_id=#{clientId} AND uid=#{uid}")
    boolean updateClientRedirectUriWithUid(String clientId, String redirectUri, Long uid);


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

    @Select("SELECT grant_types.id as id,grant_types.grant_type as grant_type FROM client_grant_types,grant_types WHERE client_grant_types.cid=#{clientId} AND grant_types.id=client_grant_types.tid")
    @Results(id = "GrantType", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "grant_type")
    })
    List<GrantType> getClientGrantTypesByClientId(String clientId);

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

}
