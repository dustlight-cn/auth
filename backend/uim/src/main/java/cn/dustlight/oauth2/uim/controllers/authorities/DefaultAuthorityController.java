package cn.dustlight.oauth2.uim.controllers.authorities;

import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.oauth2.uim.entities.v1.authorities.Authority;
import cn.dustlight.oauth2.uim.entities.v1.authorities.DefaultAuthority;
import cn.dustlight.oauth2.uim.services.authorities.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class DefaultAuthorityController implements AuthorityController<DefaultAuthority> {

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UniqueGenerator<Long> idGenerator;

    @Override
    public Collection<? extends Authority> getAuthorities(Collection<Long> id) {
        if (id == null)
            return authorityService.listAuthorities();
        return authorityService.getAuthorities(id);
    }

    @Override
    public void setAuthorities(Collection<DefaultAuthority> authorities) {
        for (DefaultAuthority authority : authorities)
            if (authority.getAid() == null)
                authority.setAid(idGenerator.generate());
        authorityService.createAuthorities(authorities);
    }

    @Override
    public void deleteAuthorities(Collection<Long> id) {
        authorityService.removeAuthorities(id);
    }
}
