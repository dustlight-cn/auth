package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.entities.UserDetails;
import cn.dustlight.oauth2.uim.entities.UserPublicDetails;
import cn.dustlight.oauth2.uim.services.UserDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ResourceController implements IResourceController {

    @Autowired
    private UserController userController;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Override
    public UserPublicDetails getCurrentUserDetails(Principal principal) {
        UserDetails details = userDetailsMapper.loadUser(principal.getName());
        if (details == null)
            return null;
        UserPublicDetails result = UserPublicDetails.fromUserDetails(details);
        result.setAvatar(userController.generateAvatarUrl(details.getUid(), 256, null));
        return result;
    }
}
