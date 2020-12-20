package cn.dustlight.auth.entities;

import java.io.Serializable;

public class Session implements Serializable {

    private String token;
    private User user;

    public Session() {

    }

    public Session(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
