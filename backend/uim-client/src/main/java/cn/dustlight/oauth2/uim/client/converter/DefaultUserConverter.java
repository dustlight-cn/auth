package cn.dustlight.oauth2.uim.client.converter;

import cn.dustlight.oauth2.uim.client.entity.IUimUser;
import cn.dustlight.oauth2.uim.client.entity.UimUser;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Map;

public class DefaultUserConverter implements IUimUserConverter {

    @Override
    public IUimUser convert(String clientName, String clientUser, Map<String, Object> data, OAuth2UserRequest userRequest) {
        UimUser uimUser = new UimUser();
        uimUser.setAttributes(data);
        uimUser.setOAuth2UserRequest(userRequest);
        uimUser.setUid(clientName + "-" + clientUser);
        uimUser.setOAuth2ClientName(clientName);
        uimUser.setOAuth2User(clientUser);
        if (data != null) {
            uimUser.setUsername(tryGet(data, clientUser, "username", "login"));
            uimUser.setEmail(tryGet(data, null, "email"));
            uimUser.setNickname(tryGet(data, uimUser.getUsername(), "nickname", "user", "name"));
            uimUser.setAvatarUrl(tryGet(data, uimUser.getUsername(), "avatar", "avatar_url", "picture", "head_pic"));
        }
        return uimUser;
    }

    private static <T> T tryGet(Map<String, ?> map, T defaultValue, String... key) {
        if (key != null)
            for (String k : key) {
                if (map.containsKey(k) &&
                        map.get(k) != null)
                    return (T) map.get(k);
            }
        return defaultValue;
    }
}
