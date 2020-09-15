package cn.dustlight.oauth2.uim.services;

import cn.dustlight.oauth2.uim.entities.User;
import cn.dustlight.oauth2.uim.entities.UserPublicDetails;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User Details Mapper
 */
@CacheNamespace
@Mapper
@Service
public interface UserDetailsMapper {

    @Select("SELECT username,email,uid,password,role,enabled,account_expired,credentials_expired,account_locked FROM user_details WHERE username=#{uoe} OR email=#{uoe}")
    @ResultMap("UserDetails")
    User loadUserOAuth(String uoe);

    @Select("SELECT uid,username,email,nickname,phone,gender,createdAt,updatedAt,role,enabled,account_expired,credentials_expired,account_locked FROM user_details WHERE username=#{username}")
    @Results(id = "UserDetails", value = {@Result(property = "uid", column = "uid"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "createdAt", column = "createdAt"),
            @Result(property = "updatedAt", column = "updatedAt"),
            @Result(property = "role", column = "role"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "accountExpired", column = "account_expired"),
            @Result(property = "credentialsExpired", column = "credentials_expired"),
            @Result(property = "accountLocked", column = "account_locked"),
            @Result(property = "roleDetails", column = "role", one = @One(select = "cn.dustlight.oauth2.uim.services.RoleDetailsMapper.getRoleNameAndDescription")),
            @Result(property = "authorities", column = "role", many = @Many(select = "cn.dustlight.oauth2.uim.services.AuthorityDetailsMapper.getAuthorityByRoleId"))
    })
    User loadUser(String username);

    @Select({"<script>SELECT uid,username,nickname,gender,createdAt,updatedAt,role,enabled,account_expired,credentials_expired,account_locked FROM user_details WHERE username IN ",
            "<foreach collection='usernameArray' item='username' open='(' separator=',' close=')'>#{username}</foreach>",
            "</script>"})
    @ResultMap("UserDetails")
    List<User> loadUsers(@Param("usernameArray") List<String> usernameArray);

    @Select({"<script>SELECT uid,username,nickname,gender,createdAt FROM user_details WHERE username IN ",
            "<foreach collection='usernameArray' item='username' open='(' separator=',' close=')'>#{username}</foreach>",
            "</script>"})
    @Results(id = "UserPublicDetails", value = {@Result(property = "uid", column = "uid"),
            @Result(property = "username", column = "username"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "createdAt", column = "createdAt")
    })
    List<UserPublicDetails> loadUsersPublic(@Param("usernameArray") List<String> usernameArray);

    @Select("SELECT COUNT(*) FROM user_details WHERE email=#{email}")
    boolean isEmailExist(String email);

    @Select("SELECT COUNT(*) FROM user_details WHERE username=#{username}")
    boolean isUsernameExist(String username);

    @Select("SELECT COUNT(*) FROM user_details WHERE phone=#{phone}")
    boolean isPhoneExist(String phone);

    @Insert("INSERT INTO user_details SET uid=#{uid},username=#{username},password=#{password},email=#{email},nickname=#{nickname}")
    boolean insertUser(long uid, String username, String password, String email, String nickname);

    @Update("UPDATE user_details SET email=#{email} WHERE uid=#{uid}")
    boolean changeEmail(Long uid, String email);

    @Update("UPDATE user_details SET password=#{password} WHERE uid=#{uid}")
    boolean changePassword(Long uid, String password);

    @Update("UPDATE user_details SET role=#{roleId} WHERE uid=#{uid}")
    boolean changeRole(Long uid, Long roleId);

    @Update("UPDATE user_details,role_details SET user_details.role=role_details.id WHERE user_details.uid=#{uid} AND role_details.role_name=#{roleName}")
    boolean changeRoleByRoleName(Long uid, String roleName);

    @Update("UPDATE user_details SET password=#{password} WHERE email=#{email}")
    boolean changePasswordByEmail(String email, String password);

    @Update("UPDATE user_details SET nickname=#{nickname} WHERE uid=#{uid}")
    boolean changeNickname(Long uid, String nickname);

    @Update("UPDATE user_details SET gender=#{gender} WHERE uid=#{uid}")
    boolean changeGender(Long uid, int gender);
}
