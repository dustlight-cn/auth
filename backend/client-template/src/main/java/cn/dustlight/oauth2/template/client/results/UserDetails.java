package cn.dustlight.oauth2.template.client.results;

import java.util.HashMap;
import java.util.List;

public class UserDetails extends HashMap<String, Object> {

    private <T> T g(String key) {
        Object o = get(key);
        if (o == null)
            return null;
        return (T) o;
    }

    public String getNickname() {
        return g("nickname");
    }

    public void setNickname(String nickname) {
        put("nickname", nickname);
    }

    public String getUsername() {
        return g("username");
    }

    public void setUsername(String username) {
        put("username", username);
    }

    public String getEmail() {
        return g("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public List<String> getAuthorities() {
        return g("authoritiesString");
    }

    public void setAuthorities(List<String> authorities) {
        put("authoritiesString", authorities);
    }
}
