package cn.dustlight.uim.services;

import cn.dustlight.uim.models.AppDetails;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@Service
@Mapper
public interface ClientMapper {

    @Insert("INSERT INTO oauth_client_details " +
            "(client_id,uid,client_name,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,additional_information,autoapprove) VALUES " +
            "(#{clientId},#{uid},#{clientName},#{resourceIds},#{clientSecret},#{scope},#{authorizedGrantTypes},#{redirectUri},#{authorities},#{additionalInformation},#{autoApprove})")
    boolean insertClient(String clientId,
                         long uid,
                         String clientName,
                         String resourceIds,
                         String clientSecret,
                         String scope,
                         String authorizedGrantTypes,
                         String redirectUri,
                         String authorities,
                         String additionalInformation,
                         String autoApprove);

    @Select("SELECT uid,client_id,client_name,scope,web_server_redirect_uri FROM oauth_client_details WHERE client_id=#{clientId}")
    @Results({
            @Result(property = "uid", column = "uid"),
            @Result(property = "appKey", column = "client_id"),
            @Result(property = "appName", column = "client_name"),
            @Result(property = "scope", column = "scope"),
            @Result(property = "redirectUri", column = "web_server_redirect_uri")
    })
    AppDetails getClient(String clientId);

    @Delete("DELETE FROM oauth_client_details WHERE client_id=#{clientId}")
    boolean deleteClient(String clientId);

    @Update("UPDATE oauth_client_details SET client_secret=#{clientSecret} WHERE client_id=#{clientId}")
    boolean updateSecret(String clientId, String clientSecret);
}
