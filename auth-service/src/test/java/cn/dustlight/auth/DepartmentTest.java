package cn.dustlight.auth;

import cn.dustlight.auth.entities.Department;
import cn.dustlight.auth.entities.User;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.services.DepartmentService;
import cn.dustlight.auth.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@SpringBootTest
public class DepartmentTest {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    UniqueGenerator<Long> idGenerator;


    Log logger = LogFactory.getLog(getClass());

    @Test
    public void createTest() {
        Collection<? extends Department> departments = departmentService.getDepartments(-1L);
        Assert.isTrue(departments.size() == 0, "Org '-1' should not have department!");

        String username = "a" + idGenerator.generate().toString();
        User user = null;
        try {
            userService.createUser(username, username, null, username + "@" + username, username, 0,
                    Collections.emptyList(), null, null, null, true);
            user = userService.loadUserByUsername(username);
            logger.info("Create User\n" + toJson(user));
            Department d = null;
            try {
                d = departmentService.createDepartment(user.getUid(), "人力资源部", "");
                throw new RuntimeException("Should not success create department cause user is not organization");
            } catch (AuthException e) {
                System.out.println(e.getMessage());
            }
            userService.updateOrganization(Arrays.asList(user.getUid()), true);
            user = userService.loadUserByUsername(username);
            logger.info("Update Organization\n" + toJson(user));

            d = departmentService.createDepartment(user.getUid(), "人力资源部", "");

            logger.info("Create Department\n" + toJson(d));

            departments = departmentService.getDepartments(user.getUid());

            logger.info("List Department\n" + toJson(departments));
            Assert.isTrue(departments.size() > 0, "Departments is empty");

            d = departmentService.getDepartment(d.getDid());

            logger.info("Department\n" + toJson(d));
            Assert.notNull(d, "Departments is null");
        } finally {
            if (user == null)
                user = userService.loadUserByUsername(username);
            userService.deleteUsers(Arrays.asList(user.getUid()));
        }


    }

    @Test
    public void childrenAndParentTest() {
        Collection<? extends Department> departments = departmentService.getDepartmentsWithParents(0L, 6L);
        logger.info(toJson(departments));
    }

    @Test
    public void updateDepartmentTest() {
        departmentService.updateDepartment(0L, 2L, "服务端开发", null);
        logger.info(toJson(departmentService.getDepartment(0L, 2L)));
    }

    @Test
    public void updateDepartmentParentTest() {
        logger.info(toJson(departmentService.getDepartmentsWithChildren(0L, 2L)));
        departmentService.updateDepartmentParent(3L, 0L, 2L);
        logger.info(toJson(departmentService.getDepartmentsWithChildren(0L, 2L)));
    }

    @Test
    public void addDepartmentUserTest() {
        departmentService.addDepartmentUsers(2L, Arrays.asList(541707601734467584L));
        logger.info(toJson(departmentService.getDepartmentUsers(Arrays.asList(2L), 0L)));
    }

    @Test
    public void removeDepartmentUserTest() {
        departmentService.removeDepartmentUsers(2L, Arrays.asList(541707601734467584L));
        logger.info(toJson(departmentService.getDepartmentUsers(Arrays.asList(2L), 0L)));
    }

    @Test
    public void listOrganizationUsers() {
        logger.info(toJson(departmentService.getOrganizationUsers(0L)));
    }

    @Test
    public void listOrganizationPublicUsers() {
        logger.info(toJson(departmentService.getOrganizationPublicUsers(0L)));
    }

    @Test
    public void listDepartmentUsers() {
        logger.info(toJson(departmentService.getDepartmentUsers(Arrays.asList(2L), 0L)));
    }

    @Test
    public void listDepartmentPublicUsers() {
        logger.info(toJson(departmentService.getDepartmentPublicUsers(Arrays.asList(2L), 0L)));
    }

    @SneakyThrows
    private String toJson(Object... obj) {
        if (obj == null || obj.length == 0)
            return "";
        if (obj.length == 1)
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj[0]);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}
