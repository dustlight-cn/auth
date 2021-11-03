package cn.dustlight.auth.services;

import cn.dustlight.auth.entities.Department;

import java.util.Collection;

/**
 * 部门相关服务
 */
public interface DepartmentService {

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
     * 获取部门以及父部门
     *
     * @param org 组织 ID
     * @param did 部门 ID
     * @return
     */
    Collection<? extends Department> getDepartmentsWithParents(Long org,
                                            Long did);

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
     * 删除部门
     *
     * @param org 组织 ID
     * @param did 部门 ID
     * @return 被删除的部门对象
     */
    Department deleteDepartment(Long org,
                       Long did);

    /**
     * 删除部门
     *
     * @param did 部门 ID
     * @return 被删除的部门对象
     */
    Department deleteDepartment(Long did);

}
