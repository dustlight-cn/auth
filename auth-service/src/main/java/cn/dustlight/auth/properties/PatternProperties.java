package cn.dustlight.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dustlight.auth.service.pattern")
public class PatternProperties {

    private String username = "^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+$",
            email = "^\\S+@\\S+$",
            password = "^.{6,20}$";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
