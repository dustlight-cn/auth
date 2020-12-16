package cn.dustlight.auth.entities;


import cn.dustlight.auth.util.Datable;
import cn.dustlight.auth.util.ToStringCollectionSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 应用详情
 */
public interface Client extends ClientDetails, Datable, Serializable {

    /**
     * 获取应用所属用户ID
     *
     * @return 应用所属用户ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getUid();

    /**
     * 获取应用名
     *
     * @return 应用名
     */
    String getName();

    /**
     * 获取应用描述
     *
     * @return 应用描述
     */
    String getDescription();

    /**
     * 获取应用Logo
     *
     * @return 应用Logo
     */
    String getLogo();

    /**
     * 获取应用状态
     *
     * @return 应用状态
     */
    Integer getStatus();

    /**
     * 获取授权作用域
     *
     * @return 授权作用域
     */
    Collection<? extends ClientScope> getScopes();

    @JsonIgnore
    @Override
    boolean isAutoApprove(String s);

    @JsonIgnore
    @Override
    boolean isScoped();

    @JsonIgnore
    @Override
    boolean isSecretRequired();

    @JsonIgnore
    @Override
    Set<String> getScope();

    @ArraySchema(schema = @Schema(type = "string"))
    @JsonSerialize(using = ToStringCollectionSerializer.class)
    @Override
    Collection<GrantedAuthority> getAuthorities();

    @Schema(name = "cid")
    @JsonProperty("cid")
    @Override
    String getClientId();

    @Schema(name = "secret")
    @JsonProperty("secret")
    @Override
    String getClientSecret();

    @Schema(name = "redirectUri")
    @JsonProperty("redirectUri")
    @Override
    Set<String> getRegisteredRedirectUri();

    @Schema(name = "grantTypes")
    @JsonProperty("grantTypes")
    @Override
    Set<String> getAuthorizedGrantTypes();

    @Schema(name = "resources")
    @JsonProperty("resources")
    @Override
    Set<String> getResourceIds();

    @Schema(name = "accessTokenValidity")
    @JsonProperty("accessTokenValidity")
    @Override
    Integer getAccessTokenValiditySeconds();

    @Schema(name = "refreshTokenValidity")
    @JsonProperty("refreshTokenValidity")
    @Override
    Integer getRefreshTokenValiditySeconds();

    @Schema(name = "extra")
    @JsonProperty("extra")
    @Override
    Map<String, Object> getAdditionalInformation();
}
