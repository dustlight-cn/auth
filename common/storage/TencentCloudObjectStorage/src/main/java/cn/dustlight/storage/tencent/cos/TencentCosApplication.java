package cn.dustlight.storage.tencent.cos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class TencentCosApplication {

    public static void main(String[] args) {
        SpringApplication.run(TencentCosApplication.class, args);
    }
}
