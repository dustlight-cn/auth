package cn.dustlight.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dustlight.auth.service.pattern")
public class PatternProperties {

    private String username = "^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+$",
            phone = "^\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$",
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
