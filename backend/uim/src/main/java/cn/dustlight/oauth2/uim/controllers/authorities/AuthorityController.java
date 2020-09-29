package cn.dustlight.oauth2.uim.controllers.authorities;

import cn.dustlight.oauth2.uim.Constants;
import cn.dustlight.oauth2.uim.entities.v1.authorities.Authority;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "权限管理业务", description = "权限的增删改查")
@RequestMapping(value = Constants.V1.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public interface AuthorityController<T extends Authority> {

    @Operation(summary = "获取权限")
    @GetMapping("authorities")
    Collection<? extends Authority> getAuthorities(@RequestParam(required = false) Collection<Long> id);

    @Operation(summary = "修改或添加权限")
    @PutMapping("authorities")
    @PreAuthorize("hasAnyAuthority('WRITE_AUTHORITY')")
    void setAuthorities(@RequestBody Collection<T> authorities);

    @Operation(summary = "删除权限")
    @DeleteMapping("authorities")
    @PreAuthorize("hasAnyAuthority('DELETE_AUTHORITY')")
    void deleteAuthorities(@RequestParam Collection<Long> id);
}
