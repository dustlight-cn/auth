package cn.dustlight.auth.mappers;

import cn.dustlight.auth.entities.DefaultPublicUser;
import cn.dustlight.auth.entities.DefaultUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (uid,username,password,email,nickname,gender,accountExpiredAt,credentialsExpiredAt,unlockedAt,enabled) VALUES" +
            "(#{uid},#{username},#{password},#{email},#{nickname},#{gender},#{accountExpiredAt},#{credentialsExpiredAt},#{unlockedAt},#{enabled})")
    boolean insertUser(@Param("uid") Long uid,
                       @Param("username") String username,
                       @Param("password") String password,
                       @Param("email") String email,
                       @Param("nickname") String nickname,
                       @Param("gender") int gender,
                       @Param("accountExpiredAt") Date accountExpiredAt,
                       @Param("credentialsExpiredAt") Date credentialsExpiredAt,
                       @Param("unlockedAt") Date unlockedAt,
                       @Param("enabled") boolean enabled);

    @Select("SELECT * FROM users WHERE username=#{uoe} OR email=#{uoe} LIMIT 1")
    @Results(id = "User", value = {
            @Result(column = "uid", property = "uid"),
            @Result(column = "uid",
                    property = "roles",
                    many = @Many(select = "cn.dustlight.auth.mappers.RoleMapper.listUserRoles"))
    })
    DefaultUser selectUserByUsernameOrEmail(@Param("uoe") String uoe);

    @Select("SELECT * FROM users WHERE uid=#{uid} LIMIT 1")
    @ResultMap("User")
    DefaultUser selectUser(@Param("uid") Long uid);

    @Select({"<script>SELECT * FROM users WHERE uid IN ",
            "<foreach collection='uids' item='uid' open='(' separator=',' close=')'>#{uid}</foreach>",
            "</script>"})
    Collection<DefaultPublicUser> selectUsersPublic(@Param("uids") Collection<Long> uids);

    @Select({"<script>SELECT * FROM users WHERE username IN ",
            "<foreach collection='usernames' item='username' open='(' separator=',' close=')'>#{username}</foreach>",
            "</script>"})
    Collection<DefaultUser> selectUsersByUsername(@Param("usernames") Collection<String> usernames);

    @Select({"<script>SELECT * FROM users WHERE uid IN ",
            "<foreach collection='uids' item='uid' open='(' separator=',' close=')'>#{uid}</foreach>",
            "</script>"})
    Collection<DefaultUser> selectUsersByUid(@Param("uids") Collection<Long> uids);

    @Select("SELECT COUNT(uid) FROM users")
    int count();

    @Select("<script>SELECT users.* FROM users," +
            "(SELECT uid FROM users" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> OFFSET #{offset}</if></if>) AS tmp " +
            "WHERE users.uid=tmp.uid</script>")
    @ResultMap("User")
    Collection<DefaultUser> listUsers(@Param("orderBy") String orderBy,
                                      @Param("offset") Integer offset,
                                      @Param("limit") Integer limit);

    @Select("SELECT count(uid) FROM users WHERE MATCH (username,email,nickname) AGAINST(#{keywords})")
    int countSearch(@Param("keywords") String keywords);

    @Select("<script>SELECT users.* FROM users," +
            "(SELECT uid FROM users WHERE MATCH (username,email,nickname) AGAINST(#{keywords})" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> OFFSET #{offset}</if></if>) AS tmp " +
            "WHERE users.uid=tmp.uid</script>")
    @ResultMap("User")
    Collection<DefaultUser> searchUsers(@Param("keywords") String keywords,
                                        @Param("orderBy") String orderBy,
                                        @Param("offset") Integer offset,
                                        @Param("limit") Integer limit);

    @Select("<script>SELECT users.* FROM users," +
            "(SELECT uid FROM users WHERE MATCH (username,email,nickname) AGAINST(#{keywords})" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> OFFSET #{offset}</if></if>) AS tmp " +
            "WHERE users.uid=tmp.uid</script>")
    Collection<DefaultPublicUser> searchPublicUsers(@Param("keywords") String keywords,
                                                    @Param("orderBy") String orderBy,
                                                    @Param("offset") Integer offset,
                                                    @Param("limit") Integer limit);

    @Update("UPDATE users SET password=#{password} WHERE uid=#{uid}")
    boolean updatePassword(@Param("uid") Long uid,
                           @Param("password") String password);

    @Update("UPDATE users SET password=#{password} WHERE email=#{email}")
    boolean updatePasswordByEmail(@Param("email") String email,
                                  @Param("password") String password);

    @Update("UPDATE users SET nickname=#{nickname} WHERE uid=#{uid}")
    boolean updateNickname(@Param("uid") Long uid,
                           @Param("nickname") String nickname);

    @Update("UPDATE users SET email=#{email} WHERE uid=#{uid}")
    boolean updateEmail(@Param("uid") Long uid,
                        @Param("email") String email);

    @Update("UPDATE users SET gender=#{gender} WHERE uid=#{uid}")
    boolean updateGender(@Param("uid") Long uid,
                         @Param("gender") Integer gender);

    @Update("<script>UPDATE users SET enabled=#{enabled} WHERE uid IN " +
            "<foreach collection='uids' item='uid' open='(' close=')'>#{uid}</foreach>" +
            "</script>")
    boolean updateEnabled(@Param("uids") Collection<Long> uids,
                          @Param("enabled") boolean enabled);

    @Update("<script>UPDATE users SET accountExpiredAt=#{accountExpiredAt} WHERE uid IN " +
            "<foreach collection='uids' item='uid' open='(' close=')'>#{uid}</foreach>" +
            "</script>")
    boolean updateAccountExpiredAt(@Param("uids") Collection<Long> uids,
                                   @Param("accountExpiredAt") Date accountExpiredAt);

    @Update("<script>UPDATE users SET credentialsExpiredAt=#{credentialsExpiredAt} WHERE uid IN " +
            "<foreach collection='uids' item='uid' open='(' close=')'>#{uid}</foreach>" +
            "</script>")
    boolean updateCredentialsExpiredAt(@Param("uids") Collection<Long> uids,
                                       @Param("credentialsExpiredAt") Date credentialsExpiredAt);

    @Update("<script>UPDATE users SET unlockedAt=#{unlockedAt} WHERE uid IN " +
            "<foreach collection='uids' item='uid' open='(' close=')'>#{uid}</foreach>" +
            "</script>")
    boolean updateUnlockedAt(@Param("uids") Collection<Long> uids,
                             @Param("unlockedAt") Date unlockedAt);

    @Delete("<script>DELETE FROM users WHERE uid IN " +
            "<foreach collection='uids' item='uid' open='(' close=')'>#{uid}</foreach>" +
            "</script>")
    boolean deleteUsers(@Param("uids") Collection<Long> uids);
}
