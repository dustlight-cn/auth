package cn.dustlight.oauth2.uim.services.userdetails;

import cn.dustlight.oauth2.uim.services.UserDetailsMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultUimUserDetailsService implements UimUserDetailsService {

    private UserDetailsMapper mapper;

    public DefaultUimUserDetailsService(UserDetailsMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String s) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetails u = mapper.loadUserOAuth(s);
        if (u == null)
            throw new UsernameNotFoundException("username or email '" + s + "' not found.");
        return u;
    }
}
