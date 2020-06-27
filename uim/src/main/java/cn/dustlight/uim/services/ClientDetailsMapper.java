package cn.dustlight.uim.services;

import cn.dustlight.uim.models.ClientDetails;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheNamespace
@Service
@Mapper
public interface ClientDetailsMapper {

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
            @Result(property = "resourceIds", column = "client_id", many = @Many(select = "cn.dustlight.uim.services.ResourceDetailsMapper.getClientResourceIdsByClientId")),
            @Result(property = "scope", column = "client_id", many = @Many(select = "cn.dustlight.uim.services.ClientMapper.getClientScopeByClientId")),
            @Result(property = "authorizedGrantTypes", column = "client_id", many = @Many(select = "cn.dustlight.uim.services.GrantTypeMapper.getClientGrantTypesByClientId")),
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

}
