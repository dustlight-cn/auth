package cn.dustlight.oauth2.uim.entities.v1.clients;

import cn.dustlight.oauth2.uim.entities.v1.scopes.ClientScope;
import cn.dustlight.oauth2.uim.handlers.serializers.ToStringCollectionSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 应用详情
 */
public interface Client extends ClientDetails {

    /**
     * 获取应用所属用户ID
     *
     * @return
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getUid();

    /**
     * 获取应用名
     *
     * @return
     */
    String getName();

    /**
     * 获取应用描述
     *
     * @return
     */
    String getDescription();

    /**
     * 获取应用Logo
     *
     * @return
     */
    String getLogo();

    /**
     * 获取应用状态
     *
     * @return
     */
    Integer getStatus();

    /**
     * 获取创建时间
     *
     * @return
     */
    Date getCreatedAt();

    /**
     * 获取更新时间
     *
     * @return
     */
    Date getUpdatedAt();

    /**
     * 获取授权作用域
     *
     * @return
     */
    Collection<ClientScope> getScopes();

    @JsonIgnore
    @Override
    boolean isAutoApprove(String s);

    @JsonIgnore
    @Override
    boolean isScoped();

    @JsonIgnore
    @Override
    boolean isSecretRequired();

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
