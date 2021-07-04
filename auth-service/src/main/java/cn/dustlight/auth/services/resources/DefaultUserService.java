package cn.dustlight.auth.services.resources;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.DefaultPublicUser;
import cn.dustlight.auth.entities.DefaultUser;
import cn.dustlight.auth.entities.Role;
import cn.dustlight.auth.entities.UserRole;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.mappers.RoleMapper;
import cn.dustlight.auth.mappers.UserMapper;
import cn.dustlight.auth.services.UserService;
import cn.dustlight.auth.util.OrderBySqlBuilder;
import cn.dustlight.auth.util.QueryResults;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

public class DefaultUserService implements UserService<DefaultUser, DefaultPublicUser> {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final UniqueGenerator<Long> idGenerator;
    private static final Log logger = LogFactory.getLog(DefaultUserService.class.getName());

    /* 表单正则 */
    private Pattern usernamePattern;
    private Pattern emailPattern;
    private Pattern phonePattern;
    private Pattern passwordPattern;

    private final OrderBySqlBuilder orderBySqlBuilder = OrderBySqlBuilder.create
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
        if (!StringUtils.hasText(s))
            ErrorEnum.USERNAME_INVALID.throwException();
        DefaultUser u = userMapper.selectUserByUsernameOrEmail(s);
        if (u == null)
            throw new UsernameNotFoundException("user not found");
        return u;
    }

    @Transactional
    @Override
    public void createUser(String username, String password,  String phone, String email, String nickname, int gender,
                           Collection<UserRole> roles, Date accountExpiredAt, Date credentialsExpiredAt, Date unlockedAt, boolean enabled) {
        Long id = idGenerator.generate();
        if (usernamePattern != null && !usernamePattern.matcher(username).matches())
            ErrorEnum.USERNAME_INVALID.throwException();
        if (passwordPattern != null && !passwordPattern.matcher(password).matches())
            ErrorEnum.PASSWORD_INVALID.throwException();
        if (StringUtils.hasText(email) && emailPattern != null && !emailPattern.matcher(email).matches())
            ErrorEnum.EMAIL_INVALID.throwException();
        if(StringUtils.hasText(phone) && phonePattern != null && !phonePattern.matcher(phone).matches())
            ErrorEnum.PHONE_INVALID.throwException();

        if (!StringUtils.hasText(email) && !StringUtils.hasText(phone))
            ErrorEnum.INPUT_INVALID.details("Phone and email is empty").throwException();

        try {
            // 先创建用户
            if (!userMapper.insertUser(id, username, encodePassword(password),phone, email, nickname, gender,
                    accountExpiredAt, credentialsExpiredAt, unlockedAt, enabled))
                ErrorEnum.CREATE_USER_FAIL.throwException();
            // 再添加角色
            if (roles != null && roles.size() > 0 && !roleMapper.insertUserRoles(id, roles))
                ErrorEnum.CREATE_ROLE_FAIL.details("fail to insert user roles").throwException();
        } catch (DuplicateKeyException e) {
            ErrorEnum.USER_EXISTS.throwException();
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
    public QueryResults<DefaultUser> listUsers(Collection<String> orderBy, Integer offset, Integer limit) {
        QueryResults<DefaultUser> result = new QueryResults<>();
        result.setData(userMapper.listUsers(orderBySqlBuilder.build(orderBy), offset, limit));
        result.setCount(userMapper.count());
        return result;
    }

    @Override
    public QueryResults<DefaultUser> searchUsers(String keywords, Collection<String> orderBy, Integer offset, Integer limit) {
        QueryResults<DefaultUser> result = new QueryResults<>();
        result.setData(userMapper.searchUsers(keywords, orderBySqlBuilder.build(orderBy), offset, limit));
        result.setCount(userMapper.countSearch(keywords));
        return result;
    }

    @Override
    public QueryResults<DefaultPublicUser> searchPublicUsers(String keywords, Collection<String> orderBy, Integer offset, Integer limit) {
        QueryResults<DefaultPublicUser> result = new QueryResults<>();
        result.setData(userMapper.searchPublicUsers(keywords, orderBySqlBuilder.build(orderBy), offset, limit));
        result.setCount(userMapper.countSearch(keywords));
        return result;
    }

    @Override
    public void updatePassword(Long uid, String password) {
        if (passwordPattern != null && !passwordPattern.matcher(password).matches())
            ErrorEnum.PASSWORD_INVALID.throwException();
        if (!userMapper.updatePassword(uid, encodePassword(password)))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update password").throwException();
    }

    @Override
    public void updatePasswordByEmail(String email, String password) {
        if (passwordPattern != null && !passwordPattern.matcher(password).matches())
            ErrorEnum.PASSWORD_INVALID.throwException();
        if (emailPattern != null && !emailPattern.matcher(email).matches())
            ErrorEnum.EMAIL_INVALID.throwException();
        if (!userMapper.updatePasswordByEmail(email, encodePassword(password)))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update password by email").throwException();
    }

    @Override
    public void updatePasswordByPhone(String phone, String password) {
        if (passwordPattern != null && !passwordPattern.matcher(password).matches())
            ErrorEnum.PASSWORD_INVALID.throwException();
        if(phonePattern != null && !phonePattern.matcher(phone).matches())
            ErrorEnum.PHONE_INVALID.throwException();
        if (!userMapper.updatePasswordByPhone(phone, encodePassword(password)))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update password by phone").throwException();
    }

    @Override
    public void updateNickname(Long uid, String nickname) {
        if (nickname == null || nickname.trim().isEmpty())
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update nickname").throwException();
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
        if (emailPattern != null && !emailPattern.matcher(email).matches())
            ErrorEnum.EMAIL_INVALID.throwException();
        if (!userMapper.updateEmail(uid, email))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update email").throwException();
    }

    @Override
    public void updatePhone(Long uid, String phone) {
        if(phonePattern != null && !phonePattern.matcher(phone).matches())
            ErrorEnum.PHONE_INVALID.throwException();
        if (!userMapper.updatePhone(uid, phone))
            ErrorEnum.UPDATE_USER_FAIL.details("fail to update phone").throwException();
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
    public Collection<? extends Role> getRoles(Long uid) {
        return roleMapper.listUserRoles(uid);
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

    @Override
    public boolean isUsernameExists(String username) {
        if (usernamePattern != null && !usernamePattern.matcher(username).matches())
            ErrorEnum.USERNAME_INVALID.throwException();
        return userMapper.isUsernameExists(username);
    }

    @Override
    public boolean isEmailExists(String email) {
        if (emailPattern != null && !emailPattern.matcher(email).matches())
            ErrorEnum.EMAIL_INVALID.throwException();
        return userMapper.isEmailExists(email);
    }

    @Override
    public boolean isPhoneExists(String phone) {
        if(phonePattern != null && !phonePattern.matcher(phone).matches())
            ErrorEnum.PHONE_INVALID.throwException();
        return userMapper.isPhoneExists(phone);
    }

    /* --------------------------------------------------------------------------------------------------- */

    public void setPhonePattern(Pattern phonePattern) {
        this.phonePattern = phonePattern;
    }

    public void setEmailPattern(Pattern emailPattern) {
        this.emailPattern = emailPattern;
    }

    public void setPasswordPattern(Pattern passwordPattern) {
        this.passwordPattern = passwordPattern;
    }

    public void setUsernamePattern(Pattern usernamePattern) {
        this.usernamePattern = usernamePattern;
    }

    public Pattern getPhonePattern() {
        return phonePattern;
    }

    public Pattern getEmailPattern() {
        return emailPattern;
    }

    public Pattern getPasswordPattern() {
        return passwordPattern;
    }

    public Pattern getUsernamePattern() {
        return usernamePattern;
    }

    /* --------------------------------------------------------------------------------------------------- */
}
