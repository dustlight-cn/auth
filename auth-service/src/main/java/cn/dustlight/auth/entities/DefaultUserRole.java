package cn.dustlight.auth.entities;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;
import java.util.Date;

@Schema(name = "UserRole")
public class DefaultUserRole implements UserRole {

    private Long rid;
    private String roleName;
    private String roleDescription;
    private Date expiredAt;
    private Collection<String> authorities;

    @Override
    public Long getRid() {
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
    public Date getCreatedAt() {
        return null;
    }

    @Override
    public Date getUpdatedAt() {
        return null;
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
        return expiredAt != null && expiredAt.before(new Date());
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    public void setAuthorities(Collection<String> authorities) {
        this.authorities = authorities;
    }
}
