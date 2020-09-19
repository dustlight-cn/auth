package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultResourceController implements ResourceController {

//    @Autowired
//    private UimUserDetailsService uimUserDetailsService;

    @Autowired
    private UimUser user;

    @Override
    public UimUser getUser() {
        return user;
    }
}
