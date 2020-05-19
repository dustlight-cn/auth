package cn.dustlight.uim.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class OAuthUserDetailService implements UserDetailsService {

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        cn.dustlight.uim.models.UserDetails u = userDetailsMapper.loadUserOAuth(username);
        if(u == null)
            throw new UsernameNotFoundException("Username or email: '" + username + "' not found!");
        u.setUsername(username);
        return u;
    }
}
