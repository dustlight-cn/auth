package cn.dustlight.oauth2.uim;

import cn.dustlight.oauth2.uim.entities.v1.users.DefaultPublicUimUser;
import cn.dustlight.oauth2.uim.entities.v1.users.PublicUimUser;
import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import cn.dustlight.oauth2.uim.mappers.v1.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

@SpringBootTest
public class MapperTest {

    @Autowired
    private ObjectMapper objectMapper;
    private static final Logger logger = Logger.getLogger(MapperTest.class.getName());

    @Test
    public void userMapperTest(@Autowired UserMapper userMapper) throws JsonProcessingException {
        UimUser user = userMapper.selectUserByUsernameOrEmail("root");
        Assert.notNull(user, "select user fail!");
        logger.info(objectMapper.writeValueAsString(user));
        Collection<DefaultPublicUimUser> users = userMapper.selectUsersPublic(Arrays.asList(0L, 1L, 2L));
        logger.info(objectMapper.writeValueAsString(users));
    }
}
