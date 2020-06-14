package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.models.UserDetails;
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
