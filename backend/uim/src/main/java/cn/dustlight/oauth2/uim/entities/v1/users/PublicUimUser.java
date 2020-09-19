package cn.dustlight.oauth2.uim.entities.v1.users;

import cn.dustlight.oauth2.uim.entities.v1.roles.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

/**
 * 用户公开信息
 */
public interface PublicUimUser extends UimUser {

    @JsonIgnore
    @Override
    String getEmail();

    @JsonIgnore
    @Override
    Date getAccountExpiredAt();

    @JsonIgnore
    @Override
    Collection<? extends GrantedAuthority> getAuthorities();

    @JsonIgnore
    @Override
    Collection<UserRole> getUserRoles();

    @JsonIgnore
    @Override
    Date getCredentialsExpiredAt();

    @JsonIgnore
    @Override
    Date getUpdatedAt();

    @JsonIgnore
    @Override
    String getPassword();
}
