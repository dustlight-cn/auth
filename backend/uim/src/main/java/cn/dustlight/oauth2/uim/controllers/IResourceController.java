package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.models.UserPublicDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/api/res")
public interface IResourceController {

    @GetMapping("/details")
    @PreAuthorize("hasAuthority('READ_USERINFO') and #oauth2.clientHasRole('READ_USERINFO')")
    UserPublicDetails getCurrentUserDetails(Principal principal);

}
