package cn.dustlight.oauth2.uim.client.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Collection;
import java.util.Map;

public class UimUser implements IUimUser {

    private Object uid;
    private String username;
    private String nickname;
    private String email;
    private String avatarUrl;
    private String oauth2ClientName;
    private String oauth2User;
    private Map<String, Object> attributes;
    private String[] authorities;
    private OAuth2UserRequest oAuth2UserRequest;

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    @Override
    public Object getUid() {
        return uid;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String getOAuth2ClientName() {
        return oauth2ClientName;
    }

    @Override
    @JsonIgnore
    public OAuth2UserRequest getOAuth2UserRequest() {
        return oAuth2UserRequest;
    }

    @Override
    public String getOAuth2User() {
        return oauth2User;
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities == null ? null : AuthorityUtils.createAuthorityList(authorities);
    }

    @Override
    @JsonIgnore
    public String getName() {
        return oauth2User;
    }

    public void setUid(Object uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setOAuth2ClientName(String oauth2ClientName) {
        this.oauth2ClientName = oauth2ClientName;
    }

    public void setOAuth2User(String oauth2User) {
        this.oauth2User = oauth2User;
    }

    public void setOAuth2UserRequest(OAuth2UserRequest oAuth2UserRequest) {
        this.oAuth2UserRequest = oAuth2UserRequest;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String[] getAuthoritiesString() {
        return authorities;
    }

    public void setAuthoritiesString(String[] authorities) {
        this.authorities = authorities;
    }
}
