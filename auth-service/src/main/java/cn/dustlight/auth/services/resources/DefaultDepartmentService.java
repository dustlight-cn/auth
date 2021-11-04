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
        return departmentMapper.selectDepartmentsByOrg(org);
    }

    @Override
    public Collection<DefaultDepartment> getDepartmentsWithChildren(Long org, Long did) {
        Collection<DefaultDepartment> result = departmentMapper.selectDepartmentWithChildrenWithOrg(org, did);
        if (result.size() == 0)
            throw ErrorEnum.DEPARTMENT_NOT_FOUND.getException();
        return result;
    }

    @Override
    public Collection<DefaultDepartment> getDepartmentsWithChildren(Long did) {
        Collection<DefaultDepartment> result = departmentMapper.selectDepartmentWithChildren(did);
        if (result.size() == 0)
            throw ErrorEnum.DEPARTMENT_NOT_FOUND.getException();
        return result;
    }

    @Override
    public Collection<DefaultDepartment> getDepartmentsWithParents(Long org, Long did) {
        Collection<DefaultDepartment> result = departmentMapper.selectDepartmentWithParentWithOrg(org, did);
        if (result.size() == 0)
            throw ErrorEnum.DEPARTMENT_NOT_FOUND.getException();
        return result;
    }

    @Override
    public Collection<DefaultDepartment> getDepartmentsWithParents(Long did) {
        Collection<DefaultDepartment> result = departmentMapper.selectDepartmentWithParent(did);
        if (result.size() == 0)
            throw ErrorEnum.DEPARTMENT_NOT_FOUND.getException();
        return result;
    }

    @Override
    public DefaultDepartment getDepartment(Long org, Long did) {
        DefaultDepartment dept = departmentMapper.selectDepartmentByIdAndOrg(org, did);
        if (dept == null)
            throw ErrorEnum.DEPARTMENT_NOT_FOUND.getException();
        return dept;
    }

    @Override
    public DefaultDepartment getDepartment(Long did) {
        DefaultDepartment dept = departmentMapper.selectDepartmentById(did);
        if (dept == null)
            throw ErrorEnum.DEPARTMENT_NOT_FOUND.getException();
        return dept;
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
    public void updateDepartment(Long org, Long did, String name, String description) {
        if (!departmentMapper.updateDepartmentWithOrg(did, org, name, description))
            throw ErrorEnum.UPDATE_DEPARTMENT_FAIL.getException();
    }

    @Override
    public void updateDepartment(Long did, String name, String description) {
        if (!departmentMapper.updateDepartment(did, name, description))
            throw ErrorEnum.UPDATE_DEPARTMENT_FAIL.getException();
    }

    @Override
    public void updateDepartmentParent(Long did, Long org, Long parent) {

    }

    @Override
    public void updateDepartmentParent(Long did, Long parent) {

    }

    @Override
    public void deleteDepartment(Long org, Long did) {
        if (!departmentMapper.deleteDepartmentByIdAndOrg(org, did))
            throw ErrorEnum.DELETE_DEPARTMENT_FAIL.getException();
    }

    @Override
    public void deleteDepartment(Long did) {
        if (!departmentMapper.deleteDepartmentById(did))
            throw ErrorEnum.DELETE_DEPARTMENT_FAIL.getException();
    }

}
