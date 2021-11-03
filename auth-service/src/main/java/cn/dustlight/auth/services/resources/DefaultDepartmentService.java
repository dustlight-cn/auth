package cn.dustlight.auth.services.resources;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.DefaultDepartment;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.mappers.DepartmentMapper;
import cn.dustlight.auth.services.DepartmentService;
import cn.dustlight.auth.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DuplicateKeyException;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class DefaultDepartmentService implements DepartmentService {

    private DepartmentMapper departmentMapper;
    private UserService userService;
    private UniqueGenerator<Long> idGenerator;

    @Override
    public Collection<DefaultDepartment> getDepartments(Long org) {
        return departmentMapper.selectDepartmentByOrg(org);
    }

    @Override
    public Collection<DefaultDepartment> getDepartmentsWithChildren(Long org, Long did) {
        return null;
    }

    @Override
    public Collection<DefaultDepartment> getDepartmentsWithParents(Long org, Long did) {
        return null;
    }

    @Override
    public DefaultDepartment getDepartment(Long org, Long did) {
        return null;
    }

    @Override
    public DefaultDepartment getDepartment(Long did) {
        return null;
    }

    @Override
    public DefaultDepartment createDepartment(Long org, String name, String description, Long parent) {
        if (!userService.isOrganization(org))
            throw ErrorEnum.USER_NOT_ORGANIZATION.getException();
        try {
            Long id = idGenerator.generate();
            if (departmentMapper.insertDepartment(id, org, name, description, parent)) {
                Date d = new Date();
                DefaultDepartment defaultDepartment = new DefaultDepartment();
                defaultDepartment.setCreatedAt(d);
                defaultDepartment.setUpdatedAt(d);
                defaultDepartment.setDid(id);
                defaultDepartment.setDescription(description);
                defaultDepartment.setName(name);
                defaultDepartment.setOrg(org);
                defaultDepartment.setParent(parent);
                return defaultDepartment;
            }
            throw ErrorEnum.CREATE_DEPARTMENT_FAIL.getException();
        } catch (DuplicateKeyException duplicateKeyException) {
            throw ErrorEnum.DEPARTMENT_EXISTS.details(duplicateKeyException).getException();
        } catch (Throwable throwable) {
            throw ErrorEnum.CREATE_DEPARTMENT_FAIL.details(throwable).getException();
        }
    }

    @Override
    public DefaultDepartment createDepartment(Long org, String name, String description) {
        return createDepartment(org, name, description, null);
    }

    @Override
    public DefaultDepartment deleteDepartment(Long org, Long did) {
        return null;
    }

    @Override
    public DefaultDepartment deleteDepartment(Long did) {
        return null;
    }

}
