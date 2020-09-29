package cn.dustlight.oauth2.uim.controllers.resources;

import cn.dustlight.oauth2.uim.entities.v1.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultResourceController implements ResourceController {

    @Autowired
    private User user;

    @Override
    public User getUser() {
        return user;
    }
}
