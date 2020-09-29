package cn.dustlight.oauth2.uim.configurations;

import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.oauth2.uim.mappers.*;
import cn.dustlight.oauth2.uim.services.authorities.AuthorityService;
import cn.dustlight.oauth2.uim.services.authorities.DefaultAuthorityService;
import cn.dustlight.oauth2.uim.services.clients.ClientService;
import cn.dustlight.oauth2.uim.services.clients.DefaultClientService;
import cn.dustlight.oauth2.uim.services.code.RedisAuthorizationCodeService;
import cn.dustlight.oauth2.uim.services.code.RedisVerificationCodeStoreService;
import cn.dustlight.oauth2.uim.services.code.VerificationCodeStoreService;
import cn.dustlight.oauth2.uim.services.roles.DefaultRoleService;
import cn.dustlight.oauth2.uim.services.roles.RoleService;
import cn.dustlight.oauth2.uim.services.scopes.DefaultScopeService;
import cn.dustlight.oauth2.uim.services.scopes.ScopeService;
import cn.dustlight.oauth2.uim.services.users.DefaultUserService;
import cn.dustlight.oauth2.uim.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;

public class ServicesConfiguration {

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
    public UserService userService(@Autowired UserMapper userMapper,
                                   @Autowired RoleMapper roleMapper,
                                   @Autowired PasswordEncoder passwordEncoder,
                                   @Autowired UniqueGenerator<Long> idGenerator) {
        return new DefaultUserService(userMapper, roleMapper, passwordEncoder, idGenerator);
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientService clientService(@Autowired ClientMapper mapper) {
        return new DefaultClientService(mapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorityService authorityService(@Autowired AuthorityMapper mapper,
                                             @Autowired UniqueGenerator<Long> generator) {
        return new DefaultAuthorityService(mapper, generator);
    }

    @Bean
    @ConditionalOnMissingBean
    public RoleService roleService(@Autowired RoleMapper roleMapper,
                                   @Autowired AuthorityMapper authorityMapper,
                                   @Autowired UniqueGenerator<Long> generator) {
        return new DefaultRoleService(generator, roleMapper, authorityMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public ScopeService scopeService(@Autowired ScopeMapper scopeMapper,
                                     @Autowired AuthorityMapper authorityMapper,
                                     @Autowired UniqueGenerator<Long> generator) {
        return new DefaultScopeService(generator, scopeMapper, authorityMapper);
    }
}
