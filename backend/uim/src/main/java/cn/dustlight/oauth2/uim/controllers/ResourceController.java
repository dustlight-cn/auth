package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.RestfulResult;
import cn.dustlight.oauth2.uim.models.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ResourceController implements IResourceController{

    @Autowired
    private UserController userController;

    @Override
    public RestfulResult<UserDetails> getCurrentUserDetails(Principal principal) {
        return userController.getCurrentUserDetails(principal);
    }
}
