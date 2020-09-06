package cn.dustlight.oauth2.uim.client.entity;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;

public interface IUimUser extends OAuth2User, Serializable {

    Object getUid();

    String getUsername();

    String getNickname();

    String getEmail();

    String getAvatarUrl();

    String getOAuth2ClientName();

    String getOAuth2User();
}
