package cn.dustlight.oauth2.uim.models;

import java.io.Serializable;
import java.util.Date;

public class UserPublicDetails implements Serializable {

    private long uid;

    private String username;

    private String nickname;

    private String email;

    private int gender;

    private Date createdAt;

    private String avatar;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
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

    public static UserPublicDetails fromUserDetails(UserDetails userDetails){
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
