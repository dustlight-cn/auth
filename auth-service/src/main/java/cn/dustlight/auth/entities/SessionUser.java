package cn.dustlight.auth.entities;

public class SessionUser extends DefaultUser {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
