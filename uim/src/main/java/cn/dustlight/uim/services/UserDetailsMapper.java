package cn.dustlight.uim.services;

import cn.dustlight.uim.models.UserDetails;
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

    @Select("SELECT uid,password,role,enabled,account_expired,credentials_expired,account_locked FROM user_details WHERE username=#{uoe} OR email=#{uoe}")
    @ResultMap("UserDetails")
    UserDetails loadUserOAuth(String uoe);

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
            @Result(property = "roleName", column = "role", one = @One(select = "cn.dustlight.uim.services.RoleDetailsMapper.getRoleNameByRoleId")),
            @Result(property = "authorities", column = "role", many = @Many(select = "cn.dustlight.uim.services.AuthorityDetailsMapper.getAuthorityByRoleId"))
    })
    UserDetails loadUser(String username);

    @Select({"<script>SELECT uid,username,nickname,gender,createdAt,updatedAt,role,enabled,account_expired,credentials_expired,account_locked FROM user_details WHERE username IN ",
            "<foreach collection='usernameArray' item='username' open='(' separator=',' close=')'>#{username}</foreach>",
            "</script>"})
    @ResultMap("UserDetails")
    List<UserDetails> loadUsers(@Param("usernameArray") List<String> usernameArray);

    @Select("SELECT COUNT(*) FROM user_details WHERE email=#{email}")
    boolean isEmailExist(String email);

    @Select("SELECT COUNT(*) FROM user_details WHERE username=#{username}")
    boolean isUsernameExist(String username);

    @Select("SELECT COUNT(*) FROM user_details WHERE phone=#{phone}")
    boolean isPhoneExist(String phone);

    @Insert("INSERT INTO user_details SET uid=#{uid},username=#{username},password=#{password},email=#{email},nickname=#{nickname}")
    boolean insertUser(long uid, String username, String password, String email, String nickname);

    @Update("UPDATE user_details SET email=#{email} WHERE username=#{username}")
    boolean changeEmail(String username, String email);

    @Update("UPDATE user_details SET password=#{password} WHERE email=#{email}")
    boolean changePasswordByEmail(String email, String password);

    @Update("UPDATE user_details SET nickname=#{nickname} WHERE username=#{username}")
    boolean changeNickname(String username, String nickname);

    @Update("UPDATE user_details SET gender=#{gender} WHERE username=#{username}")
    boolean changeGender(String username, int gender);
}
