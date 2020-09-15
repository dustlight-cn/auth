package cn.dustlight.oauth2.uim.services.userdetails;

import cn.dustlight.oauth2.uim.entities.v1.roles.UserRole;
import cn.dustlight.oauth2.uim.entities.v1.users.DefaultPublicUimUser;
import cn.dustlight.oauth2.uim.entities.v1.users.PublicUimUser;
import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import cn.dustlight.oauth2.uim.mappers.v1.UserMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;

public class DefaultUimUserDetailsService implements UimUserDetailsService {

    private UserMapper mapper;

    public DefaultUimUserDetailsService(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UimUser loadUserByUsername(String s) throws UsernameNotFoundException {
        UimUser u = mapper.selectUserByUsernameOrEmail(s);
        if (u == null)
            throw new UsernameNotFoundException("username or email '" + s + "' not found.");
        return u;
    }

    @Override
    public UimUser createUser(String username,
                              String password,
                              String email,
                              int gender,
                              Collection<UserRole> roles,
                              Date accountExpiredAt,
                              Date credentialsExpiredAt,
                              Date unlocked,
                              boolean enabled) {
        return null;
    }

    @Override
    public UimUser loadUser(Long uid) {
        return mapper.selectUser(uid);
    }

    @Override
    public Collection<PublicUimUser> loadUserPublic(Collection<Long> uidArray) {
        Collection<DefaultPublicUimUser> tmp = mapper.selectUsersPublic(uidArray);
        if (tmp == null)
            return null;
        LinkedHashSet<PublicUimUser> results = new LinkedHashSet<>(tmp.size());
        for (DefaultPublicUimUser u : tmp)
            results.add(u);
        return results;
    }

    @Override
    public Collection<UimUser> loadUsersByUsername(Collection<String> usernames) {
        return null;
    }

    @Override
    public Collection<UimUser> loadUsers(Collection<Long> uidArray) {
        return null;
    }

    @Override
    public boolean updatePassword(Long uid, String password) {
        return false;
    }

    @Override
    public boolean updatePasswordByEmail(String email, String password) {
        return false;
    }

    @Override
    public boolean updateNickname(Long uid, String nickname) {
        return false;
    }

    @Override
    public boolean updateGender(Long uid, int gender) {
        return false;
    }

    @Override
    public boolean updateEmail(Long uid, String email) {
        return false;
    }

    @Override
    public boolean addRoles(Long uid, Collection<UserRole> roles) {
        return false;
    }

    @Override
    public boolean removeRoles(Long uid, Collection<Long> roleIds) {
        return false;
    }

    @Override
    public boolean updateRoleByRoleName(Long uid, String roleName) {
        return false;
    }

    @Override
    public boolean updateUnlockedAt(Long uid, Date unlockedAt) {
        return false;
    }

    @Override
    public boolean updateAccountExpiredAt(Long uid, Date expiredAt) {
        return false;
    }

    @Override
    public boolean updateCredentialsExpiredAt(Long uid, Date expiredAt) {
        return false;
    }

    @Override
    public boolean updateEnabled(Long uid, boolean enabled) {
        return false;
    }
}
