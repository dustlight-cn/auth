package cn.dustlight.auth.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

@Schema(name = "User")
public class DefaultUser implements User {

    private Long uid;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private int gender;
    private Date createdAt;
    private Date updatedAt;
    private Date accountExpiredAt;
    private Date credentialsExpiredAt;
    private Date unlockedAt;
    private Collection<? extends UserRole> roles;
    private boolean enabled;
    private boolean organization;

    @Override
    public Long getUid() {
        return uid;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public int getGender() {
        return gender;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public Collection<? extends UserRole> getUserRoles() {
        return roles;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public Date getAccountExpiredAt() {
        return accountExpiredAt;
    }

    @Override
    public Date getCredentialsExpiredAt() {
        return credentialsExpiredAt;
    }

    @Override
    public Date getUnlockedAt() {
        return unlockedAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null)
            return null;
        Collection<String> results = new LinkedHashSet<>();
        for (UserRole role : roles) {
            if (role == null || role.isExpired())
                continue;
            Collection<String> roleAuthorities = role.getAuthorities();
            if (roleAuthorities != null) {
                for (String authority : roleAuthorities) {
                    if (authority == null)
                        continue;
                    results.add(authority);
                }
            }
            String roleName = role.getRoleName();
            if (roleName == null || roleName.isEmpty())
                continue;
            if (roleName.startsWith("ROLE_"))
                results.add(roleName);
            else
                results.add("ROLE_" + roleName);
        }
        return AuthorityUtils.createAuthorityList(results.toArray(new String[0]));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpiredAt == null || accountExpiredAt.after(new Date());
    }

    @Override
    public boolean isAccountNonLocked() {
        return unlockedAt == null || unlockedAt.before(new Date());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpiredAt == null || credentialsExpiredAt.after(new Date());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAccountExpiredAt(Date accountExpiredAt) {
        this.accountExpiredAt = accountExpiredAt;
    }

    public void setCredentialsExpiredAt(Date credentialsExpiredAt) {
        this.credentialsExpiredAt = credentialsExpiredAt;
    }

    public void setRoles(Collection<? extends UserRole> roles) {
        this.roles = roles;
    }

    public void setUnlockedAt(Date unlockedAt) {
        this.unlockedAt = unlockedAt;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setOrganization(boolean organization) {
        this.organization = organization;
    }

    @Override
    public boolean isOrganization() {
        return organization;
    }
}
