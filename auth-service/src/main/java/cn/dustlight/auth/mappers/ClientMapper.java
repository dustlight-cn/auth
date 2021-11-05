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
            "<if test='orderBy!=null'> ORDER BY ${orderBy}</if>" +
            "<if test='limit!=null'> LIMIT #{limit}<if test='offset!=null'> offset #{offset}</if></if>) AS c" +
            " WHERE clients.cid=c.cid</script>")
    Collection<DefaultClient> listClients(@Param("orderBy") String orderBy,
                                          @Param("offset") Integer offset,
                                          @Param("limit") Integer limit);

    @Select("SELECT COUNT(cid) FROM clients")
    Integer countClients();

    @Select("<script>SELECT clients.* FROM clients," +
            "(SELECT cid FROM clients WHERE MATCH(name,description,redirectUri,cid) AGAINST(#{key})" +
            "<if test='orderBy!=null'> ORDER BY ${orderBy}</if>" +
            "<if test='limit!=null'> LIMIT #{limit}<if test='offset!=null'> offset #{offset}</if></if>) AS c" +
            " WHERE clients.cid=c.cid</script>")
    Collection<DefaultClient> searchClients(@Param("key") String key,
                                            @Param("orderBy") String orderBy,
                                            @Param("offset") Integer offset,
                                            @Param("limit") Integer limit);

    @Select("SELECT COUNT(cid) FROM clients WHERE MATCH(name,description,redirectUri,cid) AGAINST(#{key})")
    Integer countSearchClients(@Param("key") String key);

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
            @Result(column = "cid", property = "scopes", many = @Many(select = "cn.dustlight.auth.mappers.ScopeMapper.listClientScopes")),
            @Result(column = "cid", property = "authorities", many = @Many(select = "cn.dustlight.auth.mappers.AuthorityMapper.listClientAuthorities")),
            @Result(column = "cid", property = "members", many = @Many(select = "cn.dustlight.auth.mappers.ClientMapper.getClientMemberIds"))
    })
    DefaultClient loadClient(@Param("clientId") String clientId);

    @Select("<script>" +
            "SELECT clients.* FROM clients," +
            "((SELECT cid FROM clients WHERE uid=#{uid}) " +
            "UNION " +
            "(SELECT cid FROM client_members WHERE uid=#{uid})) AS c" +
            " WHERE clients.cid=c.cid" +
            "<if test='orderBy!=null'> ORDER BY ${orderBy}</if>" +
            "<if test='limit!=null'> LIMIT #{limit}<if test='offset!=null'> OFFSET #{offset}</if></if>" +
            "</script>")
    Collection<DefaultClient> listUserClients(@Param("uid") Long uid,
                                              @Param("orderBy") String orderBy,
                                              @Param("offset") Integer offset,
                                              @Param("limit") Integer limit);

    @Select("SELECT COUNT(cid) FROM " +
            "((SELECT cid FROM clients WHERE uid=#{uid}) " +
            "UNION " +
            "(SELECT cid FROM client_members WHERE uid=#{uid})) " +
            "AS c")
    Integer countUserClients(@Param("uid") Long uid);

    @Select("<script>SELECT clients.* FROM clients," +
            "(SELECT cid FROM " +
            "(SELECT * FROM clients WHERE cid IN ((SELECT cid FROM clients WHERE uid=#{uid}) UNION (SELECT cid FROM client_members WHERE uid=#{uid}))) AS tmp " +
            "WHERE MATCH(name,description,redirectUri,cid) AGAINST(#{key})" +
            "<if test='orderBy!=null'> ORDER BY ${orderBy}</if>" +
            "<if test='limit!=null'> LIMIT #{limit}<if test='offset!=null'> offset #{offset}</if></if>) AS c" +
            " WHERE clients.cid=c.cid</script>")
    Collection<DefaultClient> searchUserClients(@Param("uid") Long uid,
                                                @Param("key") String key,
                                                @Param("orderBy") String orderBy,
                                                @Param("offset") Integer offset,
                                                @Param("limit") Integer limit);

    @Select("SELECT COUNT(cid) FROM " +
            "(SELECT * FROM clients WHERE cid IN ((SELECT cid FROM clients WHERE uid=#{uid}) UNION (SELECT cid FROM client_members WHERE uid=#{uid}))) AS tmp " +
            "WHERE MATCH(name,description,redirectUri,cid) AGAINST(#{key})")
    Integer countSearchUserClients(@Param("uid") Long uid,
                                   @Param("key") String key);

    @Select("SELECT COUNT(uid) FROM clients WHERE cid=#{cid} AND uid=#{uid} LIMIT 1")
    Boolean isClientOwner(@Param("cid") String cid,
                          @Param("uid") Long uid);

    @Select("SELECT users.* FROM users,clients WHERE clients.cid=#{cid} AND clients.uid=users.uid LIMIT 1")
    DefaultUser getClientOwner(@Param("cid") String cid);

    @Select("SELECT users.* FROM users,clients WHERE clients.cid=#{cid} AND clients.uid=users.uid LIMIT 1")
    DefaultPublicUser getClientOwnerPublic(@Param("cid") String cid);

    /* --------------------------------------------------------------------------------------------------------- */

    /* Insert */

    @Insert("INSERT INTO clients (cid,uid,secret,name,description,redirectUri,accessTokenValidity,refreshTokenValidity,additionalInformation,status) " +
            "VALUES (#{cid},#{uid},#{secret},#{name},#{description},#{redirectUri},#{accessTokenValidity},#{refreshTokenValidity},#{additionalInformation},#{status})")
    Boolean insertClient(@Param("cid") String cid,
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
    Boolean insertClientDefault(@Param("cid") String cid,
                                @Param("uid") Long uid,
                                @Param("secret") String secret,
                                @Param("name") String name,
                                @Param("description") String description,
                                @Param("redirectUri") String redirectUri);

    /* --------------------------------------------------------------------------------------------------------- */

    /* Update */

    @Update("UPDATE clients SET secret=#{secret} WHERE cid=#{cid}")
    Boolean updateClientSecret(@Param("cid") String cid,
                               @Param("secret") String secret);

    @Update("UPDATE clients SET name=#{name} WHERE cid=#{cid}")
    Boolean updateClientName(@Param("cid") String cid,
                             @Param("name") String name);

    @Update("UPDATE clients SET description=#{description} WHERE cid=#{cid}")
    Boolean updateClientDescription(@Param("cid") String cid,
                                    @Param("description") String description);

    @Update("UPDATE clients SET redirectUri=#{redirectUri} WHERE cid=#{cid}")
    Boolean updateClientRedirectUri(@Param("cid") String cid,
                                    @Param("redirectUri") String redirectUri);

    @Update("UPDATE clients SET accessTokenValidity=#{accessTokenValidity} WHERE cid=#{cid}")
    Boolean updateClientAccessTokenValidity(@Param("cid") String cid,
                                            @Param("accessTokenValidity") Integer accessTokenValidity);

    @Update("UPDATE clients SET refreshTokenValidity=#{refreshTokenValidity} WHERE cid=#{cid}")
    Boolean updateClientRefreshTokenValidity(@Param("cid") String cid,
                                             @Param("refreshTokenValidity") Integer refreshTokenValidity);

    @Update("UPDATE clients SET additionalInformation=#{additionalInformation} WHERE cid=#{cid}")
    Boolean updateClientAdditionalInformation(@Param("cid") String cid,
                                              @Param("additionalInformation") String additionalInformation);

    @Update("UPDATE clients SET status=#{status} WHERE cid=#{cid}")
    Boolean updateClientStatus(@Param("cid") String cid,
                               @Param("status") Integer status);

    /* --------------------------------------------------------------------------------------------------------- */

    /* Delete */

    @Delete("<script>DELETE FROM clients WHERE cid IN " +
            "<foreach collection='cids' item='cid' open='(' separator=',' close=')'>" +
            "#{cid}</foreach></script>")
    Boolean deleteClient(@Param("cids") Collection<String> cids);

    @Delete("<script>DELETE FROM clients WHERE uid=#{uid} AND cid IN " +
            "<foreach collection='cids' item='cid' open='(' separator=',' close=')'>" +
            "#{cid}</foreach></script>")
    Boolean deleteUserClient(@Param("uid") Long uid,
                             @Param("cids") Collection<String> cids);

    /* --------------------------------------------------------------------------------------------------------- */

    @Select("SELECT COUNT(uid) FROM " +
            "((SELECT uid FROM clients WHERE cid=#{cid}) " +
            "UNION " +
            "(SELECT uid FROM client_members WHERE cid=#{cid})) AS c " +
            "WHERE uid=#{uid} " +
            "LIMIT 1")
    Boolean isClientOwnerOrMember(@Param("cid") String cid,
                                  @Param("uid") Long uid);

    @Select("SELECT uid FROM client_members WHERE cid=#{cid}")
    Collection<Long> getClientMemberIds(@Param("cid") String cid);

    @Insert("<script>" +
            "INSERT IGNORE INTO client_members (cid,uid) VALUES " +
            "<foreach collection='uids' item='uid' separator=','>" +
            "(#{cid},#{uid})" +
            "</foreach> " +
            "</script>")
    Boolean addClientMembers(@Param("cid") String cid,
                             @Param("uids") Collection<Long> uids);

    @Insert("<script>" +
            "DELETE FROM client_members WHERE cid=#{cid} AND uid IN " +
            "<foreach collection='uids' item='uid' separator=',' open='(' close=')'>" +
            "#{uid}" +
            "</foreach>" +
            "</script>")
    Boolean removeClientMembers(@Param("cid") String cid,
                             @Param("uids") Collection<Long> uids);

    /* --------------------------------------------------------------------------------------------------------- */
}
