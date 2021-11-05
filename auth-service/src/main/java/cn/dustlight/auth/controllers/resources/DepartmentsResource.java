package cn.dustlight.auth.controllers.resources;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.Department;
import cn.dustlight.auth.entities.User;
import cn.dustlight.auth.services.DepartmentService;
import cn.dustlight.auth.util.Constants;
import cn.dustlight.auth.util.UserExpressionMethods;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "Departments", description = "部门资源")
@SecurityRequirement(name = "AccessToken")
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@CrossOrigin(origins = Constants.CrossOrigin.origin, allowCredentials = Constants.CrossOrigin.allowCredentials)
public class DepartmentsResource {

    @Autowired
    private DepartmentService departmentService;

    //    @PreAuthorize("")
    @GetMapping("/departments")
    public Collection<? extends Department> getDepartments(OAuth2Authentication authentication) {
        return departmentService.getDepartments(UserExpressionMethods.obtainUser(authentication).getUid());
    }

    //    @PreAuthorize("")
    @PostMapping("/departments")
    public Department createDepartment(@RequestParam(name = "name") String name,
                                       @RequestParam(name = "description", required = false) String description,
                                       @RequestParam(name = "parent", required = false) Long parent,
                                       OAuth2Authentication authentication) {
        User user = UserExpressionMethods.obtainUser(authentication);
        if (!user.isOrganization())
            throw ErrorEnum.USER_NOT_ORGANIZATION.getException();
        return departmentService.createDepartment(user.getUid(), name, description, parent);
    }

    //    @PreAuthorize("")
    @GetMapping("/departments/{did}")
    public Department getDepartment(@PathVariable(name = "did") Long did) {
        return departmentService.getDepartment(did);
    }

    //    @PreAuthorize("")
    @GetMapping("/departments/{did}/parents")
    public Collection<? extends Department> getParentDepartments(@PathVariable(name = "did") Long did) {
        return departmentService.getDepartmentsWithParents(did);
    }

    //    @PreAuthorize("")
    @GetMapping("/departments/{did}/children")
    public Collection<? extends Department> getChildrenDepartments(@PathVariable(name = "did") Long did) {
        return departmentService.getDepartmentsWithChildren(did);
    }

    //    @PreAuthorize("")
    @PutMapping("/departments/{did}")
    public void updateDepartment(@PathVariable(name = "did") Long did,
                                 @RequestParam(name = "name", required = false) String name,
                                 @RequestParam(name = "description", required = false) String description) {
        departmentService.updateDepartment(did, name, description);
    }

    //    @PreAuthorize("")
    @PutMapping("/departments/{did}/parent")
    public void updateDepartmentParent(@PathVariable(name = "did") Long did,
                                       @RequestParam(name = "parent") Long parent) {
        departmentService.updateDepartmentParent(did, parent);
    }

}
