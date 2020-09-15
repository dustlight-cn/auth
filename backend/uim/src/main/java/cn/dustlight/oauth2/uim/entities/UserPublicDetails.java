package cn.dustlight.oauth2.uim.entities;

import cn.dustlight.oauth2.uim.entities.v1.roles.UserRole;
import cn.dustlight.oauth2.uim.entities.v1.users.PublicUimUser;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class UserPublicDetails implements PublicUimUser {

    private long uid;

    private String username;

    private String nickname;

    private String email;

    private int gender;

    private Date createdAt;

    private String avatar;

    @Override
    public Long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public String getEmail() {
        return email;
    }

    @Override
    public Date getAccountExpiredAt() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Collection<UserRole> getUserRoles() {
        return null;
    }

    @Override
    public Date getCredentialsExpiredAt() {
        return null;
    }

    @Override
    public Date getUnlockedAt() {
        return null;
    }

    @Override
    public Date getUpdatedAt() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static UserPublicDetails fromUserDetails(User userDetails){
        UserPublicDetails result = new UserPublicDetails();
        result.setUid(userDetails.getUid());
        result.setUsername(userDetails.getUsername());
        result.setNickname(userDetails.getNickname());
        result.setGender(userDetails.getGender());
        result.setCreatedAt(userDetails.getCreatedAt());
        result.setEmail(userDetails.getEmail());
        result.setAvatar(userDetails.getAvatar());
        return result;
    }
}
