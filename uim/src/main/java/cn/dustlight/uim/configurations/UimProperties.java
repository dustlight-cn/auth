package cn.dustlight.uim.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dustlight.uim")
public class UimProperties {

    private String[] publicPaths = {};
    private String[] resourcePaths = {};
    private String[] redirectToLoginPath = {};
    @Autowired
    private FormLogin formLogin;
    @Autowired
    private Logout logout;
    private boolean csrfEnabled = true;
    private boolean httpBasicEnabled = true;
    private int snowflakeWorkerId = -1;
    private int registerVerificationCodeLength = 6;

    private int resetPasswordVerificationCodeLength = 12;

    public int getResetPasswordVerificationCodeLength() {
        return resetPasswordVerificationCodeLength;
    }

    public void setResetPasswordVerificationCodeLength(int resetPasswordVerificationCodeLength) {
        this.resetPasswordVerificationCodeLength = resetPasswordVerificationCodeLength;
    }

    public String[] getResourcePaths() {
        return resourcePaths;
    }

    public void setResourcePaths(String[] resourcePaths) {
        this.resourcePaths = resourcePaths;
    }

    public int getSnowflakeWorkerId() {
        return snowflakeWorkerId;
    }

    public void setSnowflakeWorkerId(int snowflakeWorkerId) {
        this.snowflakeWorkerId = snowflakeWorkerId;
    }

    public int getRegisterVerificationCodeLength() {
        return registerVerificationCodeLength;
    }

    public void setRegisterVerificationCodeLength(int registerVerificationCodeLength) {
        this.registerVerificationCodeLength = registerVerificationCodeLength;
    }

    public String[] getRedirectToLoginPath() {
        return redirectToLoginPath;
    }

    public void setRedirectToLoginPath(String[] redirectToLoginPath) {
        this.redirectToLoginPath = redirectToLoginPath;
    }

    public boolean isHttpBasicEnabled() {
        return httpBasicEnabled;
    }

    public void setHttpBasicEnabled(boolean httpBasicEnabled) {
        this.httpBasicEnabled = httpBasicEnabled;
    }

    public String[] getPublicPaths() {
        return publicPaths;
    }

    public void setPublicPaths(String[] publicPaths) {
        this.publicPaths = publicPaths;
    }

    public FormLogin getFormLogin() {
        return formLogin;
    }

    public void setFormLogin(FormLogin formLogin) {
        this.formLogin = formLogin;
    }

    public Logout getLogout() {
        return logout;
    }

    public boolean isCsrfEnabled() {
        return csrfEnabled;
    }

    public void setCsrfEnabled(boolean csrfEnabled) {
        this.csrfEnabled = csrfEnabled;
    }

    public void setLogout(Logout logout) {
        this.logout = logout;
    }


    @Component
    @ConfigurationProperties(prefix = "uim.form-login")
    public static class FormLogin {

        private String loginPage = "/login";
        private String loginProcessingUrl = "/login";
        private String usernameParameter = "username";
        private String passwordParameter = "password";

        public String getPasswordParameter() {
            return passwordParameter;
        }

        public void setPasswordParameter(String passwordParameter) {
            this.passwordParameter = passwordParameter;
        }

        public String getUsernameParameter() {
            return usernameParameter;
        }

        public void setUsernameParameter(String usernameParameter) {
            this.usernameParameter = usernameParameter;
        }

        public String getLoginPage() {
            return loginPage;
        }

        public void setLoginPage(String loginPage) {
            this.loginPage = loginPage;
        }

        public String getLoginProcessingUrl() {
            return loginProcessingUrl;
        }

        public void setLoginProcessingUrl(String loginProcessingUrl) {
            this.loginProcessingUrl = loginProcessingUrl;
        }
    }

    @Component
    @ConfigurationProperties(prefix = "uim.logout")
    public static class Logout {

        private String logoutUrl = "/logout";
        private String[] deleteCookies = {};

        public String getLogoutUrl() {
            return logoutUrl;
        }

        public void setLogoutUrl(String logoutUrl) {
            this.logoutUrl = logoutUrl;
        }

        public String[] getDeleteCookies() {
            return deleteCookies;
        }

        public void setDeleteCookies(String[] deleteCookies) {
            this.deleteCookies = deleteCookies;
        }
    }
}