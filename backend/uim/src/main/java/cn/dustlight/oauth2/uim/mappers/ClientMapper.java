package cn.dustlight.oauth2.uim.mappers;

import cn.dustlight.oauth2.uim.entities.v1.clients.DefaultClient;
import cn.dustlight.oauth2.uim.entities.v1.users.DefaultPublicUser;
import cn.dustlight.oauth2.uim.entities.v1.users.DefaultUser;
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
    Collection<DefaultClient> listClients(String orderBy, Integer offset, Integer limit);

    @Select("SELECT COUNT(cid) FROM clients")
    int countClients();

    @Select("<script>SELECT clients.* FROM clients," +
            "(SELECT cid FROM clients WHERE MATCH(name,description,redirectUri,cid) AGAINST(#{key})" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> offset #{offset}</if></if>) AS c" +
            " WHERE clients.cid=c.cid</script>")
    Collection<DefaultClient> searchClients(String key, String orderBy, Integer offset, Integer limit);

    @Select("SELECT COUNT(cid) FROM clients WHERE MATCH(name,description,redirectUri,cid) AGAINST(#{key})")
    int countSearchClients(String key);

    @Select("<script>SELECT * FROM clients WHERE cid IN " +
            "<foreach collection='cids' item='cid' open='(' separator=',' close=')'>" +
            "#{cid}</foreach></script>")
    Collection<DefaultClient> selectClients(Collection<Long> cids);

    @Select("SELECT * FROM clients WHERE cid=#{clientId} LIMIT 1")
    DefaultClient selectClient(String clientId);

    @Select("SELECT * FROM clients WHERE cid=#{clientId} LIMIT 1")
    @Results(id = "Client", value = {
            @Result(column = "cid", property = "cid"),
            @Result(column = "cid", property = "types", many = @Many(select = "cn.dustlight.oauth2.uim.mappers.GrantTypeMapper.listClientGrantTypes")),
            @Result(column = "cid", property = "scopes", many = @Many(select = "cn.dustlight.oauth2.uim.mappers.ScopeMapper.listClientScopes"))
    })
    DefaultClient loadClient(String clientId);

    @Select("<script>SELECT clients.* FROM clients," +
            "(SELECT cid FROM clients WHERE uid=#{uid}" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> offset #{offset}</if></if>) AS c" +
            " WHERE clients.cid=c.cid</script>")
    Collection<DefaultClient> listUserClients(Long uid, String orderBy, Integer offset, Integer limit);

    @Select("SELECT COUNT(cid) FROM clients WHERE uid=#{uid}")
    int countUserClients(Long uid);

    @Select("<script>SELECT clients.* FROM clients," +
            "(SELECT cid FROM clients WHERE MATCH(name,description,redirectUri,cid) AGAINST(#{key}) AND uid=#{uid}" +
            "<if test='orderBy'> ORDER BY ${orderBy}</if>" +
            "<if test='limit'> LIMIT #{limit}<if test='offset'> offset #{offset}</if></if>) AS c" +
            " WHERE clients.cid=c.cid</script>")
    Collection<DefaultClient> searchUserClients(Long uid, String key, String orderBy, Integer offset, Integer limit);

    @Select("SELECT COUNT(cid) FROM clients WHERE uid=#{uid} AND MATCH(name,description,redirectUri,cid) AGAINST(#{key})")
    int countSearchUserClients(Long uid, String key);

    @Select("SELECT COUNT(uid) FROM clients WHERE cid=#{cid} AND uid=#{uid} LIMIT 1")
    boolean isClientOwner(String cid, Long uid);

    @Select("SELECT users.* FROM users,clients WHERE clients.cid=#{cid} AND clients.uid=users.uid LIMIT 1")
    DefaultUser getClientOwner(String cid);

    @Select("SELECT users.* FROM users,clients WHERE clients.cid=#{cid} AND clients.uid=users.uid LIMIT 1")
    DefaultPublicUser getClientOwnerPublic(String cid);

    /* --------------------------------------------------------------------------------------------------------- */

    /* Insert */

    @Insert("INSERT INTO clients (cid,uid,secret,name,description,redirectUri,accessTokenValidity,refreshTokenValidity,additionalInformation,status) " +
            "VALUES (#{cid},#{uid},#{secret},#{name},#{description},#{redirectUri},#{accessTokenValidity},#{refreshTokenValidity},#{additionalInformation},#{status})")
    boolean insertClient(String cid, Long uid, String secret, String name, String description, String redirectUri,
                         Integer accessTokenValidity, Integer refreshTokenValidity, String additionalInformation,
                         Integer status);

    @Insert("INSERT INTO clients (cid,uid,secret,name,description,redirectUri) " +
            "VALUES (#{cid},#{uid},#{secret},#{name},#{description},#{redirectUri})")
    boolean insertClientDefault(String cid, Long uid, String secret, String name, String description, String redirectUri);

    /* --------------------------------------------------------------------------------------------------------- */

    /* Update */

    @Update("UPDATE clients SET secret=#{secret} WHERE cid=#{cid}")
    boolean updateClientSecret(String cid, String secret);

    @Update("UPDATE clients SET secret=#{secret} WHERE cid=#{cid} AND uid=#{uid}")
    boolean updateUserClientSecret(String cid, Long uid, String secret);

    @Update("UPDATE clients SET name=#{name} WHERE cid=#{cid}")
    boolean updateClientName(String cid, String name);

    @Update("UPDATE clients SET name=#{name} WHERE cid=#{cid} AND uid=#{uid}")
    boolean updateUserClientName(String cid, Long uid, String name);

    @Update("UPDATE clients SET description=#{description} WHERE cid=#{cid}")
    boolean updateClientDescription(String cid, String description);

    @Update("UPDATE clients SET description=#{description} WHERE cid=#{cid} AND uid=#{uid}")
    boolean updateUserClientDescription(String cid, Long uid, String description);

    @Update("UPDATE clients SET redirectUri=#{redirectUri} WHERE cid=#{cid}")
    boolean updateClientRedirectUri(String cid, String redirectUri);

    @Update("UPDATE clients SET redirectUri=#{redirectUri} WHERE cid=#{cid} AND uid=#{uid}")
    boolean updateUserClientRedirectUri(String cid, Long uid, String redirectUri);

    @Update("UPDATE clients SET accessTokenValidity=#{accessTokenValidity} WHERE cid=#{cid}")
    boolean updateClientAccessTokenValidity(String cid, Integer accessTokenValidity);

    @Update("UPDATE clients SET refreshTokenValidity=#{refreshTokenValidity} WHERE cid=#{cid}")
    boolean updateClientRefreshTokenValidity(String cid, Integer refreshTokenValidity);

    @Update("UPDATE clients SET additionalInformation=#{additionalInformation} WHERE cid=#{cid}")
    boolean updateClientAdditionalInformation(String cid, String additionalInformation);

    @Update("UPDATE clients SET status=#{status} WHERE cid=#{cid}")
    boolean updateClientStatus(String cid, Integer status);

    /* --------------------------------------------------------------------------------------------------------- */

    /* Delete */

    @Delete("<script>DELETE FROM clients WHERE cid IN " +
            "<foreach collection='cids' item='cid' open='(' separator=',' close=')'>" +
            "#{cid}</foreach></script>")
    boolean deleteClient(Collection<String> cids);

    @Delete("<script>DELETE FROM clients WHERE uid=#{uid} AND cid IN " +
            "<foreach collection='cids' item='cid' open='(' separator=',' close=')'>" +
            "#{cid}</foreach></script>")
    boolean deleteUserClient(Long uid, Collection<String> cids);

    /* --------------------------------------------------------------------------------------------------------- */

}
