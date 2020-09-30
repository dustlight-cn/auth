package cn.dustlight.oauth2.uim.services.clients;

import cn.dustlight.oauth2.uim.entities.results.IntQueryResults;
import cn.dustlight.oauth2.uim.entities.v1.clients.Client;
import cn.dustlight.oauth2.uim.entities.v1.scopes.Scope;
import cn.dustlight.oauth2.uim.entities.v1.types.GrantType;
import cn.dustlight.oauth2.uim.entities.v1.users.User;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import java.util.Collection;

/**
 * 应用相关service
 */
public interface ClientService extends ClientDetailsService {

    /**
     * 获取应用
     *
     * @param cid 应用ID
     * @return 应用
     */
    @Override
    Client loadClientByClientId(String cid);

    /**
     * 列举应用
     *
     * @param orderBy 排序
     * @param offset  偏移
     * @param limit   上限
     * @return 查询结果
     */
    IntQueryResults<? extends Client> list(Collection<String> orderBy, Integer offset, Integer limit);

    /**
     * 列举用户应用
     *
     * @param uid     用户id
     * @param orderBy 排序
     * @param offset  偏移
     * @param limit   上限
     * @return 查询结果
     */
    IntQueryResults<? extends Client> list(Long uid, Collection<String> orderBy, Integer offset, Integer limit);

    /**
     * 搜索应用
     *
     * @param keywords 关键词
     * @param orderBy  排序
     * @param offset   偏移
     * @param limit    上限
     * @return 查询结果
     */
    IntQueryResults<? extends Client> search(String keywords, Collection<String> orderBy, Integer offset, Integer limit);

    /**
     * 搜索应用
     *
     * @param keywords 关键词
     * @param uid      用户id
     * @param orderBy  排序
     * @param offset   偏移
     * @param limit    上限
     * @return 查询结果
     */
    IntQueryResults<? extends Client> search(String keywords, Long uid, Collection<String> orderBy, Integer offset, Integer limit);

    /**
     * 创建应用
     *
     * @param uid                   用户id
     * @param name                  应用名称
     * @param description           应用描述
     * @param redirectUri           回调地址
     * @param scopes                授权作用域
     * @param grantTypes            授权模式
     * @param accessTokenValidity   access token 有效时间（秒）
     * @param refreshTokenValidity  refresh token 有效时间（秒）
     * @param additionalInformation 附加信息（Json）
     * @param status                状态
     * @return Client
     */
    Client create(Long uid, String name, String description, String redirectUri, Collection<Long> scopes, Collection<Long> grantTypes,
                  Integer accessTokenValidity, Integer refreshTokenValidity, String additionalInformation, int status);

    /**
     * 创建应用
     *
     * @param uid         用户id
     * @param name        应用名称
     * @param description 应用描述
     * @param redirectUri 回调地址
     * @param scopes      授权作用域
     * @param grantTypes  授权模式
     * @return 应用
     */
    Client create(Long uid, String name, String description, String redirectUri, Collection<Long> scopes, Collection<Long> grantTypes);

    /**
     * 获取应用所属用户
     *
     * @param cid 应用ID
     * @return 用户
     */
    User getOwner(String cid);

    /**
     * 更新应用密钥
     *
     * @param cid 应用ID
     * @return 新密钥
     */
    String updateSecret(String cid);

    /**
     * 更新应用密钥
     *
     * @param cid 应用ID
     * @param uid 用户ID
     * @return 新密钥
     */
    String updateSecret(String cid, Long uid);

    /**
     * 更新应用名称
     *
     * @param cid  应用ID
     * @param name 应用名称
     */
    void updateName(String cid, String name);

    /**
     * 更新应用名称
     *
     * @param cid  应用ID
     * @param uid  用户ID
     * @param name 应用名称
     */
    void updateName(String cid, Long uid, String name);

    /**
     * 更新应用描述
     *
     * @param cid         应用ID
     * @param description 应用描述
     */
    void updateDescription(String cid, String description);

    /**
     * 更新应用描述
     *
     * @param cid         应用ID
     * @param uid         用户ID
     * @param description 应用描述
     */
    void updateDescription(String cid, Long uid, String description);

    /**
     * 更新应用回调地址
     *
     * @param cid         应用ID
     * @param redirectUri 回调地址
     */
    void updateRedirectUri(String cid, String redirectUri);

    /**
     * 更新应用回调地址
     *
     * @param cid         应用ID
     * @param uid         用户ID
     * @param redirectUri 回调地址
     */
    void updateRedirectUri(String cid, Long uid, String redirectUri);

    /**
     * 更新应用Access Token有效时间（秒）
     *
     * @param cid                 应用ID
     * @param accessTokenValidity access token 有效时间（秒）
     */
    void updateAccessTokenValidity(String cid, Integer accessTokenValidity);

    /**
     * 更新应用Refresh Token有效时间（秒）
     *
     * @param cid                  应用ID
     * @param refreshTokenValidity refresh token 有效时间（秒）
     */
    void updateRefreshTokenValidity(String cid, Integer refreshTokenValidity);

    /**
     * 更新应用附加信息
     *
     * @param cid                   应用ID
     * @param additionalInformation 附加信息
     */
    void updateAdditionalInformation(String cid, String additionalInformation);

    /**
     * 更新应用状态
     *
     * @param cid    应用ID
     * @param status 状态
     */
    void updateStatus(String cid, Integer status);

    /**
     * 删除应用
     *
     * @param cids 应用ID 集合
     */
    void delete(Collection<String> cids);

    /**
     * 删除应用
     *
     * @param uid  用户ID
     * @param cids 应用ID 集合
     */
    void delete(Long uid, Collection<String> cids);

    /**
     * 列举应用授权作用域
     *
     * @param cid 应用ID
     * @return 授权作用域集合
     */
    Collection<? extends Scope> listScopes(String cid);

    /**
     * 列举应用授权作用域
     *
     * @param cid 应用ID
     * @param uid 用户id
     * @return 授权作用域集合
     */
    Collection<? extends Scope> listScopes(String cid, Long uid);

    /**
     * 为应用添加授权作用域
     *
     * @param cid  应用ID
     * @param sids 授权作用域ID集合
     */
    void addScopes(String cid, Collection<Long> sids);

    /**
     * 为应用添加授权作用域
     *
     * @param cid  应用ID
     * @param uid  用户id
     * @param sids 授权作用域ID集合
     */
    void addScopes(String cid, Long uid, Collection<Long> sids);

    /**
     * 删除应用授权作用域
     *
     * @param cid  应用ID
     * @param sids 授权作用域ID集合
     */
    void removeScopes(String cid, Collection<Long> sids);

    /**
     * 删除应用授权作用域
     *
     * @param cid  应用ID
     * @param uid  用户ID
     * @param sids 授权作用域ID集合
     */
    void removeScopes(String cid, Long uid, Collection<Long> sids);

    /**
     * 列举应用授权模式
     *
     * @param cid 应用ID
     * @return 授权模式集合
     */
    Collection<? extends GrantType> listGrantTypes(String cid);

    /**
     * 列举应用授权模式
     *
     * @param cid 应用ID
     * @param uid 用户id
     * @return 授权作用域集合
     */
    Collection<? extends GrantType> listGrantTypes(String cid, Long uid);

    /**
     * 为应用添加授权模式
     *
     * @param cid  应用ID
     * @param tids 授权模式ID集合
     */
    void addGrantTypes(String cid, Collection<Long> tids);

    /**
     * 为应用添加授权模式
     *
     * @param cid  应用ID
     * @param uid  用户id
     * @param tids 授权模式ID集合
     */
    void addGrantTypes(String cid, Long uid, Collection<Long> tids);

    /**
     * 删除应用授权模式
     *
     * @param cid  应用ID
     * @param tids 授权模式ID集合
     */
    void removeGrantTypes(String cid, Collection<Long> tids);

    /**
     * 删除应用授权模式
     *
     * @param cid  应用ID
     * @param uid  用户ID
     * @param tids 授权模式ID集合
     */
    void removeGrantTypes(String cid, Long uid, Collection<Long> tids);
}
