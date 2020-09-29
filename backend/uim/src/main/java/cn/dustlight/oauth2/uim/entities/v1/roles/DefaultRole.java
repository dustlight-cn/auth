package cn.dustlight.oauth2.uim.entities.v1.roles;

import java.util.Collection;
import java.util.Date;

public class DefaultRole implements Role {

    private Long rid;
    private String roleName;
    private String roleDescription;

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
    public Date getCreatedAt() {
        return null;
    }

    @Override
    public Date getUpdatedAt() {
        return null;
    }

    public Long getRid() {
        return rid;
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
}
