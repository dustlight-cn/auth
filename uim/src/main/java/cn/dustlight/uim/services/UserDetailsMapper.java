package cn.dustlight.uim.services;

import cn.dustlight.uim.models.UserDetails;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

/**
 * User Details Mapper
 */
@Mapper
@Service
public interface UserDetailsMapper {

    @Select("SELECT password,role,enabled,account_expired,credentials_expired,account_locked FROM user_details WHERE username=#{uoe} OR email=#{uoe}")
    @Results({@Result(property = "password", column = "password"),
            @Result(property = "role", column = "role"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "accountExpired", column = "account_expired"),
            @Result(property = "credentialsExpired", column = "credentials_expired"),
            @Result(property = "accountLocked", column = "account_locked")})
    UserDetails loadUserOAuth(String uoe);

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
}
