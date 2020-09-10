package cn.dustlight.oauth2.uim.client;

import cn.dustlight.oauth2.uim.client.converter.IUimUserConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "dustlight.uim-client")
public class UimClientProperties {

    /**
     * 用户信息路径，例如第三方登录返回数据为 {code: 200,data:{user:{username: name,email: email},extend:123}}，则用户信息路径为 data/user
     */
    private Map<String, String> userDataPath = new LinkedHashMap<>();
    /**
     * 自定义用户转换
     */
    private Map<String, Class<? extends IUimUserConverter>> customConverter = new LinkedHashMap<>();
    /**
     * 用户信息映射，用于默认转换
     */
    private Map<String, UserDetailsMapping> userDetailsMapping = new LinkedHashMap<>();
    /**
     * 是否启用Restful模式，启用后登录认证等将不进行重定向改为返回Json。
     */
    public boolean restfulEnabled = true;

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

    public boolean isRestfulEnabled() {
        return restfulEnabled;
    }

    public void setRestfulEnabled(boolean restfulEnabled) {
        this.restfulEnabled = restfulEnabled;
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
