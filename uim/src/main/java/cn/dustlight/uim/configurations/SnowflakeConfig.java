package cn.dustlight.uim.configurations;

import cn.dustlight.uim.utils.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.SocketException;

@Configuration
public class SnowflakeConfig {

    @Bean
    public Snowflake getSnowflake(@Autowired UimProperties uimProperties) throws SocketException {
        if(uimProperties.getSnowflakeWorkerId() >= 0)
            return new Snowflake(uimProperties.getSnowflakeWorkerId());
        else
            return new Snowflake();
    }
}
