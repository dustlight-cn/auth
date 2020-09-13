package cn.dustlight.oauth2.uim.configurations;

import cn.dustlight.generator.snowflake.SnowflakeIdGenerator;
import cn.dustlight.oauth2.uim.handlers.DefaultUimHandler;
import cn.dustlight.oauth2.uim.handlers.UimHandler;
import cn.dustlight.oauth2.uim.handlers.UimUserApprovalHandler;
import cn.dustlight.oauth2.uim.handlers.code.DefaultVerificationCodeGenerator;
import cn.dustlight.oauth2.uim.handlers.code.VerificationCodeGenerator;
import cn.dustlight.oauth2.uim.services.AuthorityDetailsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@EnableConfigurationProperties(UimProperties.class)
@EnableRedisHttpSession
public class UimConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UimHandler uimHandler(@Autowired ObjectMapper objectMapper) {
        return new DefaultUimHandler(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SnowflakeIdGenerator snowflakeIdGenerator(@Autowired UimProperties properties) throws SocketException {
        long machineId, dataCenterId;
        if (properties.getSnowflake().getMachineId() == null) {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> e = null;
            e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                sb.append(ni.toString());
            }
            machineId = sb.toString().hashCode();
            if (machineId < 0)
                machineId = -machineId;
            if (machineId > SnowflakeIdGenerator.MAX_MACHINE_NUM)
                machineId = machineId & SnowflakeIdGenerator.MAX_MACHINE_NUM;
        } else {
            machineId = properties.getSnowflake().getMachineId();
        }
        if (properties.getSnowflake().getDataCenterId() == null) {
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            dataCenterId = Long.valueOf(runtimeMXBean.getName().split("@")[0]).longValue();
            if (dataCenterId < 0)
                dataCenterId = -dataCenterId;
            if (dataCenterId > SnowflakeIdGenerator.MAX_DATACENTER_NUM)
                dataCenterId = dataCenterId & SnowflakeIdGenerator.MAX_DATACENTER_NUM;
        } else {
            dataCenterId = properties.getSnowflake().getDataCenterId();
        }
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(dataCenterId, machineId);
        if (properties.getSnowflake().getStartTimestamp() != null)
            generator.setStartTimestamp(properties.getSnowflake().getStartTimestamp());
        return generator;
    }

    @Bean
    @ConditionalOnMissingBean
    public VerificationCodeGenerator verificationCodeGenerator() {
        return new DefaultVerificationCodeGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public UimUserApprovalHandler uimUserApprovalHandler(@Autowired ClientDetailsService clientDetailsService,
                                                         @Autowired ApprovalStore approvalStore,
                                                         @Autowired AuthorityDetailsMapper authorityDetailsMapper){
        UimUserApprovalHandler approvalHandler = new UimUserApprovalHandler();
        approvalHandler.setClientDetailsService(clientDetailsService);
        approvalHandler.setApprovalStore(approvalStore);
        approvalHandler.setMapper(authorityDetailsMapper);
        return approvalHandler;
    }

    @Bean
    public TokenStore redisTokenStore(@Autowired RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public ApprovalStore redisTokenApprovalStore(@Autowired TokenStore redisTokenStore) {
        TokenApprovalStore tokenApprovalStore = new TokenApprovalStore();
        tokenApprovalStore.setTokenStore(redisTokenStore);
        return tokenApprovalStore;
    }
}
