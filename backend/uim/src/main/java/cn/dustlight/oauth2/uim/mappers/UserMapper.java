package cn.dustlight.oauth2.uim.mappers;

import cn.dustlight.oauth2.uim.entities.v1.users.DefaultPublicUimUser;
import cn.dustlight.oauth2.uim.entities.v1.users.DefaultUimUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (uid,username,password,email,nickname,gender,accountExpiredAt,credentialsExpiredAt,unlockedAt,enabled) VALUES" +
            "(#{uid},#{username},#{password},#{email},#{nickname},#{gender},#{accountExpiredAt},#{credentialsExpiredAt},#{unlockedAt},#{enabled})")
    boolean insertUser(Long uid,
                       String username,
                       String password,
                       String email,
                       String nickname,
                       int gender,
                       Date accountExpiredAt,
                       Date credentialsExpiredAt,
                       Date unlockedAt,
                       boolean enabled);

    @Select("SELECT * FROM users WHERE username=#{uoe} OR email=#{uoe}")
    @Results(id = "User", value = {
            @Result(column = "uid", property = "uid"),
            @Result(column = "uid",
                    property = "roles",
                    many = @Many(select = "cn.dustlight.oauth2.uim.mappers.RoleMapper.listUserRoles"))
    })
    DefaultUimUser selectUserByUsernameOrEmail(String uoe);

    @Select("SELECT * FROM users WHERE uid=#{uid}")
    @ResultMap("User")
    DefaultUimUser selectUser(Long uid);

    @Select({"<script>SELECT * FROM users WHERE uid IN ",
            "<foreach collection='uids' item='uid' open='(' separator=',' close=')'>#{uid}</foreach>",
            "</script>"})
    Collection<DefaultPublicUimUser> selectUsersPublic(@Param("uids") Collection<Long> uids);

    @Select({"<script>SELECT * FROM users WHERE username IN ",
            "<foreach collection='usernames' item='username' open='(' separator=',' close=')'>#{username}</foreach>",
            "</script>"})
    Collection<DefaultUimUser> selectUsersByUsername(@Param("usernames") Collection<String> usernames);

    @Select({"<script>SELECT * FROM users WHERE uid IN ",
            "<foreach collection='uids' item='uid' open='(' separator=',' close=')'>#{uid}</foreach>",
            "</script>"})
    Collection<DefaultUimUser> selectUsersByUid(@Param("usernames") Collection<Long> uids);

    @Select("SELECT COUNT(uid) FROM users")
    int count();

    @Select("<script>SELECT users.* FROM users," +
            "(SELECT uid FROM users" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> OFFSET #{offset}</if></if>) AS tmp " +
            "WHERE users.uid=tmp.uid</script>")
    @ResultMap("User")
    Collection<DefaultUimUser> listUsers(String orderBy, Integer offset, Integer limit);

    @Select("SELECT count(uid) FROM users WHERE MATCH (username,email,nickname) AGAINST(#{keywords})")
    int countSearch(String keywords);

    @Select("<script>SELECT users.* FROM users," +
            "(SELECT uid FROM users WHERE MATCH (username,email,nickname) AGAINST(#{keywords})" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> OFFSET #{offset}</if></if>) AS tmp " +
            "WHERE users.uid=tmp.uid</script>")
    @ResultMap("User")
    Collection<DefaultUimUser> searchUsers(String keywords, String orderBy, Integer offset, Integer limit);

    @Select("<script>SELECT users.* FROM users," +
            "(SELECT uid FROM users WHERE MATCH (username,email,nickname) AGAINST(#{keywords})" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> OFFSET #{offset}</if></if>) AS tmp " +
            "WHERE users.uid=tmp.uid</script>")
    Collection<DefaultPublicUimUser> searchPublicUsers(String keywords, String orderBy, Integer offset, Integer limit);

    @Update("UPDATE users SET password=#{password} WHERE uid=#{uid}")
    boolean updatePassword(Long uid, String password);

    @Update("UPDATE users SET password=#{password} WHERE email=#{email}")
    boolean updatePasswordByEmail(String email, String password);

    @Update("UPDATE users SET nickname=#{nickname} WHERE uid=#{uid}")
    boolean updateNickname(Long uid, String nickname);

    @Update("UPDATE users SET email=#{email} WHERE uid=#{uid}")
    boolean updateEmail(Long uid, String email);

    @Update("UPDATE users SET gender=#{gender} WHERE uid=#{uid}")
    boolean updateGender(Long uid, Integer gender);

    @Update("<script>UPDATE users SET enabled=#{enabled} WHERE uid IN " +
            "<foreach collection='uids' item='uid' open='(' close=')'>#{uid}</foreach>" +
            "</script>")
    boolean updateEnabled(Collection<Long> uids, boolean enabled);

    @Update("<script>UPDATE users SET accountExpiredAt=#{accountExpiredAt} WHERE uid IN " +
            "<foreach collection='uids' item='uid' open='(' close=')'>#{uid}</foreach>" +
            "</script>")
    boolean updateAccountExpiredAt(Collection<Long> uids, Date accountExpiredAt);

    @Update("<script>UPDATE users SET credentialsExpiredAt=#{credentialsExpiredAt} WHERE uid IN " +
            "<foreach collection='uids' item='uid' open='(' close=')'>#{uid}</foreach>" +
            "</script>")
    boolean updateCredentialsExpiredAt(Collection<Long> uids, Date credentialsExpiredAt);

    @Update("<script>UPDATE users SET unlockedAt=#{unlockedAt} WHERE uid IN " +
            "<foreach collection='uids' item='uid' open='(' close=')'>#{uid}</foreach>" +
            "</script>")
    boolean updateUnlockedAt(Collection<Long> uids, Date unlockedAt);

    @Delete("<script>DELETE FROM users WHERE uid IN " +
            "<foreach collection='uids' item='uid' open='(' close=')'>#{uid}</foreach>" +
            "</script>")
    boolean deleteUsers(Collection<Long> uids);
}
