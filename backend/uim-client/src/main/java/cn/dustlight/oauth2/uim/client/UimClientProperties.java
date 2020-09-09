package cn.dustlight.oauth2.uim.client;

import cn.dustlight.oauth2.uim.client.converter.IUimUserConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "dustlight.uim-client")
public class UimClientProperties {

    private Map<String, String> userDataPath = new LinkedHashMap<>();
    private Map<String, Class<? extends IUimUserConverter>> customConverter = new LinkedHashMap<>();
    private Map<String, UserDetailsMapping> userDetailsMapping = new LinkedHashMap<>();

    public Map<String, Class<? extends IUimUserConverter>> getCustomConverter() {
        return customConverter;
    }

    public void setCustomConverter(Map<String, Class<? extends IUimUserConverter>> customConverter) {
        this.customConverter = customConverter;
    }

    public Map<String, String> getUserDataPath() {
        return userDataPath;
    }

    public void setUserDataPath(Map<String, String> userDataPath) {
        this.userDataPath = userDataPath;
    }

    public Map<String, UserDetailsMapping> getUserDetailsMapping() {
        return userDetailsMapping;
    }

    public void setUserDetailsMapping(Map<String, UserDetailsMapping> userDetailsMapping) {
        this.userDetailsMapping = userDetailsMapping;
    }

    public static class UserDetailsMapping {
        public String username, nickname, email, avatar;

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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
