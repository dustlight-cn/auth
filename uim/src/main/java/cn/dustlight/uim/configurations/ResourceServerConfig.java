package cn.dustlight.uim.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import java.util.Arrays;
import java.util.logging.Logger;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private UimProperties properties;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] resourcePaths = properties.getResourcePaths();
        Logger.getLogger(getClass().getName()).info("Resources paths: " + Arrays.toString(resourcePaths));

        http.requestMatchers().antMatchers(resourcePaths)
                .and().authorizeRequests()
                .antMatchers(resourcePaths).authenticated();
    }
}