package cn.dustlight.uim.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * User Details
 */
public class UserDetails implements IUserDetails {

    private long uid;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String phone;

    private int gender;

    private Long role;

    private boolean enabled;

    private boolean accountExpired;

    private boolean credentialsExpired;

    private boolean accountLocked;

    private Date createdAt;

    private Date updatedAt;

    private String[] authorities;

    private RoleDetails roleDetails;

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    @Override
    public long getUid() {
        return this.uid;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getPhone() {
        return this.phone;
    }

    @Override
    public int getGender() {
        return this.gender;
    }

    @Override
    public Long getRole() {
        return this.role;
    }

    @Override
    public String getRoleName() {
        return this.roleDetails != null ? this.roleDetails.getName() : null;
    }

    @Override
    public String getRoleDescription() {
        return this.roleDetails != null ? this.roleDetails.getDescription() : null;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.authorities == null)
            return null;
        List<GrantedAuthority> result = AuthorityUtils.createAuthorityList(authorities);
        if (getRoleName() != null)
            result.add(new SimpleGrantedAuthority(getRoleName()));
        return result;
    }

    @Override
    public Set<String> getAuthoritiesString() {
        Collection<? extends GrantedAuthority> authorities = getAuthorities();
        if (authorities == null)
            return null;
        return AuthorityUtils.authorityListToSet(authorities);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
