package cn.dustlight.auth.services;

import cn.dustlight.auth.entities.*;

import java.util.Collection;
import java.util.Map;

/**
 * 部门相关服务
 */
public interface DepartmentService {

    /* ----------------------------------------- 部门增删改查 ------------------------------------------------------ */

    /**
     * 获取组织的所有部门
     *
     * @param org 组织 ID
     * @return
     */
    Collection<? extends Department> getDepartments(Long org);

    /**
     * 获取部门以及子部门
     *
     * @param org 组织 ID
     * @param did 部门 ID
     * @return
     */
    Collection<? extends Department> getDepartmentsWithChildren(Long org,
                                                                Long did);

    /**
     * 获取部门以及子部门
     *
     * @param did 部门 ID
     * @return
     */
    Collection<? extends Department> getDepartmentsWithChildren(Long did);

    /**
     * 获取部门以及父部门
     *
     * @param org 组织 ID
     * @param did 部门 ID
     * @return
     */
    Collection<? extends Department> getDepartmentsWithParents(Long org,
                                                               Long did);

    /**
     * 获取部门以及父部门
     *
     * @param did 部门 ID
     * @return
     */
    Collection<? extends Department> getDepartmentsWithParents(Long did);

    /**
     * 获取部门
     *
     * @param org 组织 ID
     * @param did 部门 ID
     * @return
     */
    Department getDepartment(Long org,
                             Long did);

    /**
     * 获取部门
     *
     * @param did 部门 ID
     * @return
     */
    Department getDepartment(Long did);

    /**
     * 创建部门
     *
     * @param org         组织 ID
     * @param name        部门名称
     * @param description 部门简介
     * @param parent      父部门
     * @return 新建的部门对象
     */
    Department createDepartment(Long org,
                                String name,
                                String description,
                                Long parent);

    /**
     * 创建部门
     *
     * @param org         组织 ID
     * @param name        部门名称
     * @param description 部门简介
     * @return 新建的部门对象
     */
    Department createDepartment(Long org,
                                String name,
                                String description);

    /**
     * 更新部门
     *
     * @param org         组织 ID
     * @param did         部门 ID
     * @param name        部门名称
     * @param description 部门简介
     */
    void updateDepartment(Long org,
                          Long did,
                          String name,
                          String description);

    /**
     * 更新部门
     *
     * @param did         部门 ID
     * @param name        部门名称
     * @param description 部门简介
     */
    void updateDepartment(Long did,
                          String name,
                          String description);

    /**
     * 更新部门父级部门
     *
     * @param did    部门 ID
     * @param org    组织 ID
     * @param parent 父级部门
     */
    void updateDepartmentParent(Long did,
                                Long org,
                                Long parent);

    /**
     * 更新部门父级部门
     *
     * @param did    部门 ID
     * @param parent 父级部门
     */
    void updateDepartmentParent(Long did,
                                Long parent);

    /**
     * 删除部门
     *
     * @param org 组织 ID
     * @param did 部门 ID
     * @return 被删除的部门对象
     */
    void deleteDepartment(Long org,
                          Long did);

    /**
     * 删除部门
     *
     * @param did 部门 ID
     * @return 被删除的部门对象
     */
    void deleteDepartment(Long did);

    /* ----------------------------------------------------------------------------------------------------------- */

    /* ----------------------------------------- 部门用户增删改查 --------------------------------------------------- */

    Map<Long, ? extends Collection<? extends DepartmentUser>> getOrganizationUsers(Long org);

    Map<Long, ? extends Collection<? extends DepartmentPublicUser>> getOrganizationPublicUsers(Long org);


    Collection<? extends DepartmentUser> getDepartmentUsers(Collection<Long> dids);

    Collection<? extends DepartmentUser> getDepartmentUsers(Collection<Long> dids, Long org);

    Collection<? extends DepartmentPublicUser> getDepartmentPublicUsers(Collection<Long> dids);

    Collection<? extends DepartmentPublicUser> getDepartmentPublicUsers(Collection<Long> dids, Long org);


    Map<Long, ? extends Collection<? extends DepartmentUser>> getDepartmentWithChildrenUsers(Collection<Long> dids);

    Map<Long, ? extends Collection<? extends DepartmentUser>> getDepartmentWithChildrenUsers(Collection<Long> dids, Long org);

    Map<Long, ? extends Collection<? extends DepartmentPublicUser>> getDepartmentWithChildrenPublicUsers(Collection<Long> dids);

    Map<Long, ? extends Collection<? extends DepartmentPublicUser>> getDepartmentWithChildrenPublicUsers(Collection<Long> dids, Long org);


    Map<Long, ? extends Collection<? extends DepartmentUser>> getDepartmentWithParentUsers(Collection<Long> dids);

    Map<Long, ? extends Collection<? extends DepartmentUser>> getDepartmentWithParentUsers(Collection<Long> dids, Long org);

    Map<Long, ? extends Collection<? extends DepartmentPublicUser>> getDepartmentWithParentPublicUsers(Collection<Long> dids);

    Map<Long, ? extends Collection<? extends DepartmentPublicUser>> getDepartmentWithParentPublicUsers(Collection<Long> dids, Long org);


    void addDepartmentUsers(Long did, Collection<Long> users);

    void addDepartmentUsers(Long did, Long org, Collection<Long> users);

    void removeDepartmentUsers(Long did, Collection<Long> users);

    void removeDepartmentUsers(Long did, Long org, Collection<Long> users);

    /* ----------------------------------------------------------------------------------------------------------- */

}
