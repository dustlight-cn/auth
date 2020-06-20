package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.models.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/api/res")
public interface IResourceController {

    @GetMapping("/details")
    @PreAuthorize("hasAuthority('READ_USERINFO')")
    RestfulResult<UserDetails> getCurrentUserDetails(Principal principal);

}
