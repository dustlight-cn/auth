package cn.dustlight.oauth2.uim.client.converter;

import cn.dustlight.oauth2.uim.client.confgurations.UimClientProperties;
import cn.dustlight.oauth2.uim.client.entity.IUimUser;
import cn.dustlight.oauth2.uim.client.entity.UimUser;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Map;

public class DefaultUserConverter implements IUimUserConverter {

    @Override
    public IUimUser convert(String clientName, String clientUser,
                            Map<String, Object> data,
                            OAuth2UserRequest userRequest,
                            UimClientProperties.UserDetailsMapping mapping) {
        UimUser uimUser = new UimUser();
        uimUser.setAttributes(data);
        uimUser.setOAuth2UserRequest(userRequest);
        uimUser.setUid(clientName + "-" + clientUser);
        uimUser.setOAuth2ClientName(clientName);
        uimUser.setOAuth2User(clientUser);
        if (data != null) {
            if (mapping == null) {
                uimUser.setUsername(tryGet(data, clientUser, "username"));
                uimUser.setEmail(tryGet(data, null, "email"));
                uimUser.setNickname(tryGet(data, uimUser.getUsername(), "nickname"));
                uimUser.setAvatarUrl(tryGet(data, null, "avatar"));
            } else {
                uimUser.setUsername(tryGet(data, clientUser, mapping.username, "username"));
                uimUser.setEmail(tryGet(data, null, mapping.email, "email"));
                uimUser.setNickname(tryGet(data, uimUser.getUsername(), mapping.nickname, "nickname"));
                uimUser.setAvatarUrl(tryGet(data, null, mapping.avatar, "avatar"));
            }
        }
        return uimUser;
    }

    private static <T> T tryGet(Map<String, ?> map, T defaultValue, String... key) {
        if (key != null)
            for (String k : key) {
                if (k == null)
                    continue;
                if (map.containsKey(k) &&
                        map.get(k) != null)
                    return (T) map.get(k);
            }
        return defaultValue;
    }
}
