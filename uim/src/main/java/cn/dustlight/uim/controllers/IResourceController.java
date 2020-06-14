package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.models.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/api/res")
public interface IResourceController {

    @PreAuthorize("#oauth2.hasAnyScope('userinfo') and isAuthenticated()")
    @GetMapping("/details")
    RestfulResult<UserDetails> getCurrentUserDetails(Principal principal);
}
