package cn.dustlight.oauth2.uim.client.converter;

import cn.dustlight.oauth2.uim.client.entity.IUimUser;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Map;

public interface IUimUserConverter {

    IUimUser convert(String clientName, String clientUser, Map<String, Object> data, OAuth2UserRequest userRequest);

}
