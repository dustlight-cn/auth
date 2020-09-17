package cn.dustlight.oauth2.uim;

import cn.dustlight.generator.Generator;
import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.oauth2.uim.entities.v1.roles.DefaultUserRole;
import cn.dustlight.oauth2.uim.entities.v1.roles.UserRole;
import cn.dustlight.oauth2.uim.entities.v1.users.DefaultPublicUimUser;
import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import cn.dustlight.oauth2.uim.mappers.RoleMapper;
import cn.dustlight.oauth2.uim.mappers.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.logging.Logger;

@SpringBootTest
public class MapperTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UniqueGenerator<Long> generator;
    @Autowired
    private Generator<String> stringGenerator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final static Long DEFAULT_ID = 503694347777703936L;
    private final static String[] ROLE_NAMES = new String[]{"User", "Developer", "Tester"};

    private static final Logger logger = Logger.getLogger(MapperTest.class.getName());

    @Transactional
    @Test
    public void userMapperTest(@Autowired UserMapper userMapper,
                               @Autowired RoleMapper roleMapper) throws JsonProcessingException {
        Long id = DEFAULT_ID;//generator.generate();
        String username = UUID.randomUUID().toString().substring(16);
        String password = stringGenerator.generate();
        String nickname = stringGenerator.generate();
        Date expiredAt = new Date(System.currentTimeMillis() + 1000 * 60 * 1);

        Assert.isTrue(userMapper.insertUser(id,
                username,
                passwordEncoder.encode(password),
                username,
                nickname,
                0,
                expiredAt,
                null,
                null,
                true), "insert user fail!");
        Collection<UserRole> roles = new LinkedHashSet<>();
        for (int i = 0; i < ROLE_NAMES.length; i++) {
            DefaultUserRole r = new DefaultUserRole();
            r.setRoleName(ROLE_NAMES[i]);
            r.setExpiredAt(expiredAt);
            roles.add(r);
        }

        Assert.isTrue(roleMapper.insertUserRoles(id, roles), "insert user role fail!");
        UimUser user = userMapper.selectUserByUsernameOrEmail(username);
        Assert.notNull(user, "select user fail!");
        logger.info(objectMapper.writeValueAsString(user));
        Collection<DefaultPublicUimUser> users = userMapper.selectUsersPublic(Arrays.asList(0L, id));
        logger.info(objectMapper.writeValueAsString(users));
    }

    @Transactional
    @Rollback(false)
    @Test
    public void updateUserRoles(@Autowired UserMapper userMapper,
                                @Autowired RoleMapper roleMapper) {
        Long uid = DEFAULT_ID;
        Date expiredAt = new Date(System.currentTimeMillis() + 1000 * 60 * 1);
        Collection<DefaultUserRole> roles = new LinkedHashSet<>();
        for (String roleName : ROLE_NAMES) {
            DefaultUserRole role = new DefaultUserRole();
            role.setRoleName(roleName);
            role.setExpiredAt(expiredAt);
            roles.add(role);
        }
        roleMapper.insertUserRoles(uid, roles);
    }

    @Transactional
    @Rollback(false)
    @Test
    public void deleteUserRoles(@Autowired UserMapper userMapper,
                                @Autowired RoleMapper roleMapper) {
        Long uid = DEFAULT_ID;
        boolean s = roleMapper.deleteUserRolesByRoleNames(uid, Arrays.asList(ROLE_NAMES));
        Assert.isTrue(s, "delete user role fail!");
        Collection<String> roleNames = roleMapper.listUserRoleNames(uid);
        if (roleNames != null) {
            Collection<String> targets = Sets.newLinkedHashSet(ROLE_NAMES);
            for (String roleName : roleNames) {
                Assert.isTrue(!targets.contains(roleName), "role: '" + roleName + "' still exists after delete!");
            }
        }
    }

    @Test
    public void listUser(@Autowired UserMapper userMapper) throws JsonProcessingException {
        int count = userMapper.count();
        Collection<? extends UimUser>
                coll1 = userMapper.listUsers("-createdAt", 0, 1),
                coll2 = userMapper.listUsers("-createdAt", 1, 1);
        logger.info("Count: " + count);
        logger.info("coll1: " + objectMapper.writeValueAsString(coll1));
        logger.info("coll2: " + objectMapper.writeValueAsString(coll2));
    }

    @Test
    public void searchUser(@Autowired UserMapper userMapper) throws JsonProcessingException {
        String keywords = "ee";

        Collection<? extends UimUser>
                users = userMapper.searchUsers(keywords, "-createdAt", 0, 1),
                pubUsers = userMapper.searchPublicUsers(keywords, "-createdAt", 0, 1);
        int count = userMapper.countSearch(keywords);
        logger.info("Count: " + count);
        logger.info("users: " + objectMapper.writeValueAsString(users));
        logger.info("pubUsers: " + objectMapper.writeValueAsString(pubUsers));
    }

}
