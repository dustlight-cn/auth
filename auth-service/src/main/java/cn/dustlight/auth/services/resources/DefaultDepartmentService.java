package cn.dustlight.auth.services.resources;

import cn.dustlight.auth.AuthException;
import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.*;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.mappers.DepartmentMapper;
import cn.dustlight.auth.services.DepartmentService;
import cn.dustlight.auth.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DuplicateKeyException;

import java.util.*;

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
            if (parent != null ?
                    departmentMapper.insertDepartment(id, org, name, description, parent) :
                    departmentMapper.insertDepartmentWithoutParent(id, org, name, description)) {
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
            if (throwable instanceof AuthException && ((AuthException) throwable).getErrorDetails().getCode() == ErrorEnum.CREATE_DEPARTMENT_FAIL.getCode())
                throw throwable;
            throw ErrorEnum.CREATE_DEPARTMENT_FAIL.details(throwable).getException();
        }
    }

    @Override
    public DefaultDepartment createDepartment(Long org, String name, String description) {
        return createDepartment(org, name, description, null);
    }

    @Override
    public void updateDepartment(Long org, Long did, String name, String description) {
        try {
            if (!departmentMapper.updateDepartmentWithOrg(did, org, name, description))
                throw ErrorEnum.UPDATE_DEPARTMENT_FAIL.getException();
        } catch (DuplicateKeyException e) {
            throw ErrorEnum.DEPARTMENT_EXISTS.details(e).getException();
        }
    }

    @Override
    public void updateDepartment(Long did, String name, String description) {
        try {
            if (!departmentMapper.updateDepartment(did, name, description))
                throw ErrorEnum.UPDATE_DEPARTMENT_FAIL.getException();
        } catch (DuplicateKeyException e) {
            throw ErrorEnum.DEPARTMENT_EXISTS.details(e).getException();
        }
    }

    @Override
    public void updateDepartmentParent(Long did, Long org, Long parent) {
        if (parent != null ?
                !departmentMapper.updateDepartmentParentWithOrg(did, org, parent) :
                !departmentMapper.updateDepartmentParentToNullWithOrg(did, org))
            throw ErrorEnum.UPDATE_DEPARTMENT_FAIL.getException();
    }

    @Override
    public void updateDepartmentParent(Long did, Long parent) {
        if (parent != null ?
                !departmentMapper.updateDepartmentParent(did, parent) :
                !departmentMapper.updateDepartmentParentToNull(did))
            throw ErrorEnum.UPDATE_DEPARTMENT_FAIL.getException();
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

    @Override
    public Map<Long, Collection<DefaultDepartmentUser>> getOrganizationUsers(Long org) {
        return transformUserDepartmentMap(departmentMapper.selectOrganizationUsers(org));
    }

    @Override
    public Map<Long, Collection<DefaultDepartmentPublicUser>> getOrganizationPublicUsers(Long org) {
        return transformUserDepartmentMap(departmentMapper.selectOrganizationPublicUsers(org));
    }

    @Override
    public Collection<DefaultDepartmentUser> getDepartmentUsers(Collection<Long> dids) {
        if (dids == null || dids.size() == 0)
            return Collections.emptyList();
        return departmentMapper.selectDepartmentUsers(dids);
    }

    @Override
    public Collection<DefaultDepartmentUser> getDepartmentUsers(Collection<Long> dids, Long org) {
        if (dids == null || dids.size() == 0)
            return Collections.emptyList();
        return departmentMapper.selectDepartmentUsersWithOrg(dids, org);
    }

    @Override
    public Collection<DefaultDepartmentPublicUser> getDepartmentPublicUsers(Collection<Long> dids) {
        if (dids == null || dids.size() == 0)
            return Collections.emptyList();
        return departmentMapper.selectDepartmentPublicUsers(dids);
    }

    @Override
    public Collection<DefaultDepartmentPublicUser> getDepartmentPublicUsers(Collection<Long> dids, Long org) {
        if (dids == null || dids.size() == 0)
            return Collections.emptyList();
        return departmentMapper.selectDepartmentPublicUsersWithOrg(dids, org);
    }

    @Override
    public void addDepartmentUsers(Long did, Collection<Long> users) {
        if (!departmentMapper.insertDepartmentUsers(did, users))
            throw ErrorEnum.CREATE_DEPARTMENT_USER_FAIL.getException();
    }

    @Override
    public void addDepartmentUsers(Long did, Long org, Collection<Long> users) {
        if (!departmentMapper.isDepartmentWithOrgExists(did, org))
            throw ErrorEnum.DEPARTMENT_NOT_FOUND.getException();
        addDepartmentUsers(did, users);
    }

    @Override
    public void removeDepartmentUsers(Long did, Collection<Long> users) {
        if (!departmentMapper.deleteDepartmentUsers(did, users))
            throw ErrorEnum.DELETE_DEPARTMENT_USER_FAIL.getException();
    }

    @Override
    public void removeDepartmentUsers(Long did, Long org, Collection<Long> users) {
        if (!departmentMapper.isDepartmentWithOrgExists(did, org))
            throw ErrorEnum.DEPARTMENT_NOT_FOUND.getException();
        removeDepartmentUsers(did, users);
    }

    public static <T extends DepartmentUser> Map<Long, Collection<T>> transformUserDepartmentMap(Collection<T> users) {
        if (users == null || users.size() == 0)
            return Collections.emptyMap();
        Map<Long, Collection<T>> result = new HashMap<>();
        for (T user : users) {
            Long deptId = user.getDepartmentId();
            Collection<T> collection = result.get(deptId);
            if (collection == null)
                result.put(deptId, (collection = new HashSet<>()));
            collection.add(user);
        }
        return result;
    }
}
