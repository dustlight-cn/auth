package cn.dustlight.oauth2.uim.configurations;

import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.generator.snowflake.SnowflakeIdGenerator;
import cn.dustlight.oauth2.uim.handlers.DefaultUimHandler;
import cn.dustlight.oauth2.uim.handlers.UimHandler;
import cn.dustlight.oauth2.uim.handlers.UimUserApprovalHandler;
import cn.dustlight.oauth2.uim.handlers.code.DefaultVerificationCodeGenerator;
import cn.dustlight.oauth2.uim.handlers.code.VerificationCodeGenerator;
import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import cn.dustlight.oauth2.uim.mappers.RoleMapper;
import cn.dustlight.oauth2.uim.mappers.UserMapper;
import cn.dustlight.oauth2.uim.services.AuthorityDetailsMapper;
import cn.dustlight.oauth2.uim.services.code.RedisAuthorizationCodeService;
import cn.dustlight.oauth2.uim.services.code.RedisVerificationCodeStoreService;
import cn.dustlight.oauth2.uim.services.code.VerificationCodeStoreService;
import cn.dustlight.oauth2.uim.services.users.DefaultUimUserDetailsService;
import cn.dustlight.oauth2.uim.services.users.UimUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
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
@OpenAPIDefinition(info = @Info(title = "统一身份管理 (UIM)", description = "主要包含用户管理，OAuth2应用管理等服务。", version = "1.0"),
        externalDocs = @ExternalDocumentation(description = "Github", url = "https://github.com/Hansin1997/Dustlight"))
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
                                                         @Autowired AuthorityDetailsMapper authorityDetailsMapper) {
        UimUserApprovalHandler approvalHandler = new UimUserApprovalHandler();
        approvalHandler.setClientDetailsService(clientDetailsService);
        approvalHandler.setApprovalStore(approvalStore);
        approvalHandler.setMapper(authorityDetailsMapper);
        return approvalHandler;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> javaRedisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.java());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.java());
        return redisTemplate;
    }


    @Bean("uimTokenStore")
    @ConditionalOnMissingBean
    public TokenStore tokenStore(@Autowired RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean("uimApprovalStore")
    @ConditionalOnMissingBean
    public ApprovalStore approvalStore(@Autowired TokenStore uimTokenStore) {
        TokenApprovalStore tokenApprovalStore = new TokenApprovalStore();
        tokenApprovalStore.setTokenStore(uimTokenStore);
        return tokenApprovalStore;
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationCodeServices authorizationCodeServices(@Autowired RedisTemplate<String, Object> redisTemplate) {
        return new RedisAuthorizationCodeService(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public VerificationCodeStoreService verificationCodeStoreService(@Autowired StringRedisTemplate redisTemplate) {
        return new RedisVerificationCodeStoreService(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public UimUser userDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null ||
                !(authentication.getPrincipal() instanceof UimUser))
            return null;
        return (UimUser) authentication.getPrincipal();
    }

    @Bean
    @ConditionalOnMissingBean
    public UimUserDetailsService uimUserDetailsService(@Autowired UserMapper userMapper,
                                                       @Autowired RoleMapper roleMapper,
                                                       @Autowired PasswordEncoder passwordEncoder,
                                                       @Autowired UniqueGenerator<Long> idGenerator) {
        return new DefaultUimUserDetailsService(userMapper, roleMapper, passwordEncoder, idGenerator);
    }
}
