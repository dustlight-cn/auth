package cn.dustlight.uim.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;

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

    private String roleName;

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

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public String getRoleName() {
        return this.roleName;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.authorities == null)
            return null;
        List<GrantedAuthority> result = AuthorityUtils.createAuthorityList(authorities);
        if (roleName != null)
            result.add(new SimpleGrantedAuthority(roleName));
        return result;
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
