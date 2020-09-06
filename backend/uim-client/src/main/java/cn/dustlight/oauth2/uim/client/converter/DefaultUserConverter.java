package cn.dustlight.oauth2.uim.client.converter;

import cn.dustlight.oauth2.uim.client.entity.IUimUser;
import cn.dustlight.oauth2.uim.client.entity.UimUser;

import java.util.Map;

public class DefaultUserConverter implements IUimUserConverter {

    @Override
    public IUimUser convert(String clientName, String clientUser, Map<String, Object> data) {
        UimUser uimUser = new UimUser();
        uimUser.setAttributes(data);
        uimUser.setUid(clientName + "-" + clientUser);
        uimUser.setOAuth2ClientName(clientName);
        uimUser.setOAuth2User(clientUser);
        if (data != null) {
            uimUser.setUsername((String) data.getOrDefault("username", data.getOrDefault("user", clientUser)));
            uimUser.setEmail((String) data.get("email"));
            uimUser.setNickname((String) data.getOrDefault("nickname", data.getOrDefault("login", clientUser)));
        }
        return uimUser;
    }
}
