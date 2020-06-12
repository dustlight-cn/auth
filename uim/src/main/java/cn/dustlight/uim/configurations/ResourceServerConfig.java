package cn.dustlight.uim.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private UimProperties properties;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("server-auth-resource");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        for (String resPath : properties.getResourcePaths()) {
            http.antMatcher(resPath);
        }
        http.authorizeRequests()
                .anyRequest().authenticated();
    }
}
