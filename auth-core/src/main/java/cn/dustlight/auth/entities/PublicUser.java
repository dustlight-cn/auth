package cn.dustlight.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 用户公开信息
 */
public interface PublicUser extends User, Serializable {

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
