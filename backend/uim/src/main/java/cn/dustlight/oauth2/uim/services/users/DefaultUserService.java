package cn.dustlight.oauth2.uim.services.users;

import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.oauth2.uim.entities.results.IntQueryResults;
import cn.dustlight.oauth2.uim.entities.v1.roles.UserRole;
import cn.dustlight.oauth2.uim.entities.v1.users.DefaultPublicUser;
import cn.dustlight.oauth2.uim.entities.v1.users.DefaultUser;
import cn.dustlight.oauth2.uim.mappers.RoleMapper;
import cn.dustlight.oauth2.uim.mappers.UserMapper;
import cn.dustlight.oauth2.uim.utils.OrderBySqlBuilder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;

public class DefaultUserService implements UserService<DefaultUser, DefaultPublicUser> {

    private UserMapper userMapper;
    private RoleMapper roleMapper;
    private PasswordEncoder passwordEncoder;
    private UniqueGenerator<Long> idGenerator;

    private OrderBySqlBuilder orderBySqlBuilder = OrderBySqlBuilder.create
            ("uid", "createdAt", "updatedAt", "accountExpiredAt", "credentialsExpiredAt", "unlockedAt");

    public DefaultUserService(UserMapper userMapper,
                              RoleMapper roleMapper,
                              PasswordEncoder passwordEncoder,
                              UniqueGenerator<Long> idGenerator) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.idGenerator = idGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    private String encodePassword(String raw) {
        if (passwordEncoder == null)
            return raw;
        return passwordEncoder.encode(raw);
    }

    @Override
    public DefaultUser loadUserByUsername(String s) {
        DefaultUser u = userMapper.selectUserByUsernameOrEmail(s);
        if (u == null)
            throw new UsernameNotFoundException("user not found");
        return u;
    }

    @Transactional
    @Override
    public void createUser(String username, String password, String email, String nickname, int gender,
                           Collection<UserRole> roles, Date accountExpiredAt, Date credentialsExpiredAt, Date unlockedAt, boolean enabled) {
        Long id = idGenerator.generate();
        try {
            // 先创建用户
            if (!userMapper.insertUser(id, username, encodePassword(password), email, nickname, gender,
                    accountExpiredAt, credentialsExpiredAt, unlockedAt, enabled))
                ErrorEnum.CREATE_USER_FAIL.throwException();
            // 再添加角色
            if (roles != null && roles.size() > 0 && !roleMapper.insertUserRoles(id, roles))
                ErrorEnum.CREATE_ROLE_FAIL.details("fail to insert user roles").throwException();
        } catch (DuplicateKeyException e) {
            ErrorEnum.USER_EXISTS.details(e.getMessage()).throwException();
        }
    }

    @Override
    public DefaultUser loadUser(Long uid) {
        return userMapper.selectUser(uid);
    }

    @Override
    public Collection<DefaultPublicUser> loadPublicUserByUid(Collection<Long> uidArray) {
        return userMapper.selectUsersPublic(uidArray);
    }

    @Override
    public Collection<DefaultUser> loadUsersByUsername(Collection<String> usernames) {
        return userMapper.selectUsersByUsername(usernames);
    }

    @Override
    public Collection<DefaultUser> loadUsers(Collection<Long> uids) {
        return userMapper.selectUsersByUid(uids);
    }

    @Override
    public IntQueryResults<DefaultUser> listUsers(Collection<String> orderBy, Integer offset, Integer limit) {
        IntQueryResults<DefaultUser> result = new IntQueryResults<>();
        result.setData(userMapper.listUsers(orderBySqlBuilder.build(orderBy), offset, limit));
        result.setCount(userMapper.count());
        return result;
    }

    @Override
    public IntQueryResults<DefaultUser> searchUsers(String keywords, Collection<String> orderBy, Integer offset, Integer limit) {
        IntQueryResults<DefaultUser> result = new IntQueryResults<>();
        result.setData(userMapper.searchUsers(keywords, orderBySqlBuilder.build(orderBy), offset, limit));
        result.setCount(userMapper.countSearch(keywords));
        return result;
    }

    @Override
    public IntQueryResults<DefaultPublicUser> searchPublicUsers(String keywords, Collection<String> orderBy, Integer offset, Integer limit) {
        IntQueryResults<DefaultPublicUser> result = new IntQueryResults<>();
        result.setData(userMapper.searchPublicUsers(keywords, orderBySqlBuilder.build(orderBy), offset, limit));
        result.setCount(userMapper.countSearch(keywords));
        return result;
    }

    @Override
    public void updatePassword(Long uid, String password) {
        if (!userMapper.updatePassword(uid, password))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update password").throwException();
    }

    @Override
    public void updatePasswordByEmail(String email, String password) {
        if (!userMapper.updatePasswordByEmail(email, encodePassword(password)))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update password by email").throwException();
    }

    @Override
    public void updateNickname(Long uid, String nickname) {
        if (!userMapper.updateNickname(uid, nickname))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update nickname").throwException();
    }

    @Override
    public void updateGender(Long uid, int gender) {
        if (!userMapper.updateGender(uid, gender))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update gender").throwException();
    }

    @Override
    public void updateEmail(Long uid, String email) {
        if (!userMapper.updateEmail(uid, email))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update email").throwException();
    }

    @Override
    public void addRoles(Long uid, Collection<UserRole> roles) {
        if (!roleMapper.insertUserRoles(uid, roles))
            ErrorEnum.CREATE_ROLE_FAIL.details("fail to add user roles").throwException();
    }

    @Override
    public void removeRoles(Long uid, Collection<Long> roleIds) {
        if (!roleMapper.deleteUserRolesByRoleIds(uid, roleIds))
            ErrorEnum.DELETE_ROLE_FAIL.details("fail to remove user roles").throwException();
    }

    @Override
    public Collection<String> getRoles(Long uid) {
        return roleMapper.listUserRoleNames(uid);
    }

    @Override
    public void updateUnlockedAt(Collection<Long> uids, Date unlockedAt) {
        if (!userMapper.updateUnlockedAt(uids, unlockedAt))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update unlocked date").throwException();
    }

    @Override
    public void updateAccountExpiredAt(Collection<Long> uids, Date expiredAt) {
        if (!userMapper.updateAccountExpiredAt(uids, expiredAt))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update account expired date").throwException();
    }

    @Override
    public void updateCredentialsExpiredAt(Collection<Long> uids, Date expiredAt) {
        if (!userMapper.updateCredentialsExpiredAt(uids, expiredAt))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update credentials expired date").throwException();
    }

    @Override
    public void updateEnabled(Collection<Long> uids, boolean enabled) {
        if (!userMapper.updateEnabled(uids, enabled))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update enabled").throwException();
    }

    @Override
    public void deleteUsers(Collection<Long> uids) {
        if (!userMapper.deleteUsers(uids))
            ErrorEnum.DELETE_USER_FAIL.throwException();
    }
}
