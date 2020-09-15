package cn.dustlight.oauth2.uim.entities.v1.roles;

import java.util.Collection;
import java.util.Date;

public class DefaultUserRole implements UserRole {

    private Long rid;
    private String roleName;
    private String roleDescription;
    private Date expiredAt;
    private Collection<String> authorities;

    @Override
    public Long getRoleId() {
        return rid;
    }

    @Override
    public String getRoleName() {
        return roleName;
    }

    @Override
    public String getRoleDescription() {
        return roleDescription;
    }

    @Override
    public Collection<String> getAuthorities() {
        return authorities;
    }

    @Override
    public Date getExpiredAt() {
        return expiredAt;
    }

    @Override
    public boolean isExpired() {
        return expiredAt == null ? false : expiredAt.before(new Date());
    }
}
