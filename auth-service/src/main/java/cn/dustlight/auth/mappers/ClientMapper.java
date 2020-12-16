package cn.dustlight.auth.mappers;

import cn.dustlight.auth.entities.DefaultClient;
import cn.dustlight.auth.entities.DefaultPublicUser;
import cn.dustlight.auth.entities.DefaultUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Mapper
public interface ClientMapper {

    /* --------------------------------------------------------------------------------------------------------- */

    /* Select */

    @Select("<script>SELECT clients.* FROM clients," +
            "(SELECT cid FROM clients" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> offset #{offset}</if></if>) AS c" +
            " WHERE clients.cid=c.cid</script>")
    Collection<DefaultClient> listClients(@Param("orderBy") String orderBy,
                                          @Param("offset") Integer offset,
                                          @Param("limit") Integer limit);

    @Select("SELECT COUNT(cid) FROM clients")
    int countClients();

    @Select("<script>SELECT clients.* FROM clients," +
            "(SELECT cid FROM clients WHERE MATCH(name,description,redirectUri,cid) AGAINST(#{key})" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> offset #{offset}</if></if>) AS c" +
            " WHERE clients.cid=c.cid</script>")
    Collection<DefaultClient> searchClients(@Param("key") String key,
                                            @Param("orderBy") String orderBy,
                                            @Param("offset") Integer offset,
                                            @Param("limit") Integer limit);

    @Select("SELECT COUNT(cid) FROM clients WHERE MATCH(name,description,redirectUri,cid) AGAINST(#{key})")
    int countSearchClients(@Param("key") String key);

    @Select("<script>SELECT * FROM clients WHERE cid IN " +
            "<foreach collection='cids' item='cid' open='(' separator=',' close=')'>" +
            "#{cid}</foreach></script>")
    Collection<DefaultClient> selectClients(@Param("cids") Collection<Long> cids);

    @Select("SELECT * FROM clients WHERE cid=#{clientId} LIMIT 1")
    DefaultClient selectClient(@Param("clientId") String clientId);

    @Select("SELECT * FROM clients WHERE cid=#{clientId} LIMIT 1")
    @Results(id = "Client", value = {
            @Result(column = "cid", property = "cid"),
            @Result(column = "cid", property = "types", many = @Many(select = "cn.dustlight.auth.mappers.GrantTypeMapper.listClientGrantTypes")),
            @Result(column = "cid", property = "scopes", many = @Many(select = "cn.dustlight.auth.mappers.ScopeMapper.listClientScopes"))
    })
    DefaultClient loadClient(@Param("clientId") String clientId);

    @Select("<script>SELECT clients.* FROM clients," +
            "(SELECT cid FROM clients WHERE uid=#{uid}" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> offset #{offset}</if></if>) AS c" +
            " WHERE clients.cid=c.cid</script>")
    Collection<DefaultClient> listUserClients(@Param("uid") Long uid,
                                              @Param("orderBy") String orderBy,
                                              @Param("offset") Integer offset,
                                              @Param("limit") Integer limit);

    @Select("SELECT COUNT(cid) FROM clients WHERE uid=#{uid}")
    int countUserClients(@Param("uid") Long uid);

    @Select("<script>SELECT clients.* FROM clients," +
            "(SELECT cid FROM clients WHERE MATCH(name,description,redirectUri,cid) AGAINST(#{key}) AND uid=#{uid}" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> offset #{offset}</if></if>) AS c" +
            " WHERE clients.cid=c.cid</script>")
    Collection<DefaultClient> searchUserClients(@Param("uid") Long uid,
                                                @Param("key") String key,
                                                @Param("orderBy") String orderBy,
                                                @Param("offset") Integer offset,
                                                @Param("limit") Integer limit);

    @Select("SELECT COUNT(cid) FROM clients WHERE uid=#{uid} AND MATCH(name,description,redirectUri,cid) AGAINST(#{key})")
    int countSearchUserClients(@Param("uid") Long uid,
                               @Param("key") String key);

    @Select("SELECT COUNT(uid) FROM clients WHERE cid=#{cid} AND uid=#{uid} LIMIT 1")
    boolean isClientOwner(@Param("cid") String cid,
                          @Param("uid") Long uid);

    @Select("SELECT users.* FROM users,clients WHERE clients.cid=#{cid} AND clients.uid=users.uid LIMIT 1")
    DefaultUser getClientOwner(@Param("cid") String cid);

    @Select("SELECT users.* FROM users,clients WHERE clients.cid=#{cid} AND clients.uid=users.uid LIMIT 1")
    DefaultPublicUser getClientOwnerPublic(@Param("cid") String cid);

    /* --------------------------------------------------------------------------------------------------------- */

    /* Insert */

    @Insert("INSERT INTO clients (cid,uid,secret,name,description,redirectUri,accessTokenValidity,refreshTokenValidity,additionalInformation,status) " +
            "VALUES (#{cid},#{uid},#{secret},#{name},#{description},#{redirectUri},#{accessTokenValidity},#{refreshTokenValidity},#{additionalInformation},#{status})")
    boolean insertClient(@Param("cid") String cid,
                         @Param("uid") Long uid,
                         @Param("secret") String secret,
                         @Param("name") String name,
                         @Param("description") String description,
                         @Param("redirectUri") String redirectUri,
                         @Param("accessTokenValidity") Integer accessTokenValidity,
                         @Param("refreshTokenValidity") Integer refreshTokenValidity,
                         @Param("additionalInformation") String additionalInformation,
                         @Param("status") Integer status);

    @Insert("INSERT INTO clients (cid,uid,secret,name,description,redirectUri) " +
            "VALUES (#{cid},#{uid},#{secret},#{name},#{description},#{redirectUri})")
    boolean insertClientDefault(@Param("cid") String cid,
                                @Param("uid") Long uid,
                                @Param("secret") String secret,
                                @Param("name") String name,
                                @Param("description") String description,
                                @Param("redirectUri") String redirectUri);

    /* --------------------------------------------------------------------------------------------------------- */

    /* Update */

    @Update("UPDATE clients SET secret=#{secret} WHERE cid=#{cid}")
    boolean updateClientSecret(@Param("cid") String cid,
                               @Param("secret") String secret);

    @Update("UPDATE clients SET secret=#{secret} WHERE cid=#{cid} AND uid=#{uid}")
    boolean updateUserClientSecret(@Param("cid") String cid,
                                   @Param("uid") Long uid,
                                   @Param("secret") String secret);

    @Update("UPDATE clients SET name=#{name} WHERE cid=#{cid}")
    boolean updateClientName(@Param("cid") String cid,
                             @Param("name") String name);

    @Update("UPDATE clients SET name=#{name} WHERE cid=#{cid} AND uid=#{uid}")
    boolean updateUserClientName(@Param("cid") String cid,
                                 @Param("uid") Long uid,
                                 @Param("name") String name);

    @Update("UPDATE clients SET description=#{description} WHERE cid=#{cid}")
    boolean updateClientDescription(@Param("cid") String cid,
                                    @Param("description") String description);

    @Update("UPDATE clients SET description=#{description} WHERE cid=#{cid} AND uid=#{uid}")
    boolean updateUserClientDescription(@Param("cid") String cid,
                                        @Param("uid") Long uid,
                                        @Param("description") String description);

    @Update("UPDATE clients SET redirectUri=#{redirectUri} WHERE cid=#{cid}")
    boolean updateClientRedirectUri(@Param("cid") String cid,
                                    @Param("redirectUri") String redirectUri);

    @Update("UPDATE clients SET redirectUri=#{redirectUri} WHERE cid=#{cid} AND uid=#{uid}")
    boolean updateUserClientRedirectUri(@Param("cid") String cid,
                                        @Param("uid") Long uid,
                                        @Param("redirectUri") String redirectUri);

    @Update("UPDATE clients SET accessTokenValidity=#{accessTokenValidity} WHERE cid=#{cid}")
    boolean updateClientAccessTokenValidity(@Param("cid") String cid,
                                            @Param("accessTokenValidity") Integer accessTokenValidity);

    @Update("UPDATE clients SET refreshTokenValidity=#{refreshTokenValidity} WHERE cid=#{cid}")
    boolean updateClientRefreshTokenValidity(@Param("cid") String cid,
                                             @Param("refreshTokenValidity") Integer refreshTokenValidity);

    @Update("UPDATE clients SET additionalInformation=#{additionalInformation} WHERE cid=#{cid}")
    boolean updateClientAdditionalInformation(@Param("cid") String cid,
                                              @Param("additionalInformation") String additionalInformation);

    @Update("UPDATE clients SET status=#{status} WHERE cid=#{cid}")
    boolean updateClientStatus(@Param("cid") String cid,
                               @Param("status") Integer status);

    /* --------------------------------------------------------------------------------------------------------- */

    /* Delete */

    @Delete("<script>DELETE FROM clients WHERE cid IN " +
            "<foreach collection='cids' item='cid' open='(' separator=',' close=')'>" +
            "#{cid}</foreach></script>")
    boolean deleteClient(@Param("cids") Collection<String> cids);

    @Delete("<script>DELETE FROM clients WHERE uid=#{uid} AND cid IN " +
            "<foreach collection='cids' item='cid' open='(' separator=',' close=')'>" +
            "#{cid}</foreach></script>")
    boolean deleteUserClient(@Param("uid") Long uid,
                             @Param("cids") Collection<String> cids);

    /* --------------------------------------------------------------------------------------------------------- */

}
