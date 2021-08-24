/**
 * 表数据
 * version: 0
 */

/* 用户 Users */
INSERT
IGNORE INTO `users` (`uid`,`username`,`password`,`enabled`) VALUES
(0,'root','$2a$10$GP57XrnKif0JriZlHCyu2OwfMvB3/VpOUEZPvvAnleVOPYpvMR79e',1);

/* 应用 Clients */
INSERT
IGNORE INTO `clients` (`cid`,`uid`,`secret`,`name`,`description`,`redirectUri`,`accessTokenValidity`,`refreshTokenValidity`,`additionalInformation`,`status`) VALUES
('default',0,NULL,'default','系统默认应用',NULL,7200,86400,NULL,0); -- 默认应用，提供授权中心使用

/* 权限 Authorities */
INSERT
IGNORE INTO `authorities` (`aid`,`authorityName`,`authorityDescription`,`cid`) VALUES
(0,'WRITE_AUTHORITY','创建、修改以及删除任意权限。','default'),
(1,'WRITE_TYPE','创建、修改以及删除任意授权模式。','default'),
(2,'WRITE_SCOPE','创建、修改以及删除任意授权作用域。','default'),
(3,'WRITE_ROLE','创建、修改以及删除任意角色。','default'),
(4,'WRITE_USER','修改任意用户的昵称、头像以及性别。','default'),
(5,'WRITE_USER_PASSWORD','修改任意用户的密码。','default'),
(6,'WRITE_USER_EMAIL','修改任意用户的邮箱地址。','default'),
(7,'WRITE_USER_PHONE','修改任意用户的手机号码。','default'),
(8,'WRITE_CLIENT','创建、修改以及删除任意应用，包括应用详情、应用权限、应用授权模式以及应用授权作用域。','default'),

(100,'READ_USER','读取任意用户的全部信息，包括个人详情和角色信息。','default'),
(101,'READ_CLIENT','读取任意应用的全部信息，包括应用详情、应用权限、应用授权模式以及应用授权作用域。','default'),

(200,'CREATE_USER','创建用户。','default'),
(201,'CREATE_CLIENT','创建用户应用。','default'),

(300,'DELETE_USER','删除用户。','default'),
(301,'LOCK_USER','封禁或锁定用户。','default'),

(400,'GRANT_ROLE','为角色添加或删除权限。','default'),
(401,'GRANT_CLIENT','为应用添加或删除权限。','default'),
(402,'GRANT_USER','为用户添加或删除角色。','default'),

(500,'AUTHORIZE','应用授权。','default');

/* 角色 Roles */
INSERT
IGNORE INTO `roles` (`rid`,`roleName`,`roleDescription`,`cid`) VALUES
(0,'User','用户','default'),
(1,'Developer','开发者','default'),
(2,'Administrator','管理员','default'),
(3,'Root','超级管理员','default');

/* 授权作用域 Scopes */
INSERT
IGNORE INTO `scopes` (`sid`,`name`,`subtitle`,`description`) VALUES
(0,'read:user','读取用户信息','读取您的所有个人资料，包括头像、用户名、昵称和邮箱等。'),
(1,'write:user','修改用户信息','此授权作用域暂无任何意义。');

/* 授权类型 GrantTypes */
INSERT
IGNORE INTO `types` (`tid`,`name`,`description`) VALUES
(0,'authorization_code','授权码模式'),
(1,'refresh_token','令牌刷新'),
(2,'implicit','隐式授权模式'),
(3,'client_credentials','客户端凭据模式');
-- (4,'password','密码模式'); 此授权模式不安全，可绕过验证直接登录。

/* 应用权限 ClientAuthorities */
INSERT
IGNORE INTO `client_authority`
SELECT 'default', aid
FROM `authorities`
WHERE aid BETWEEN 0 AND 999;

/* 应用授权作用域 ClientScopes */
INSERT
IGNORE INTO `client_scope`
SELECT 'default', sid, 1
FROM `scopes`
WHERE sid BETWEEN 0 AND 999;

/* 角色权限 RoleAuthorities */
INSERT
IGNORE INTO `role_authority` VALUES (1,201); /* 开发者拥有创建应用权限 */
INSERT
IGNORE INTO `role_authority` VALUES (2,4),(2,100),(2,101),(2,200),(2,201); /* 管理员拥有的权限：修改用户基本信息、读取任意用户和应用的所有信息、创建用户和应用 */
INSERT
IGNORE INTO `role_authority`
SELECT 3, aid
FROM `authorities`
WHERE aid BETWEEN 0 AND 999;
/* 超级管理员具有所有权限 */

/* 用户角色 UserRoles */
INSERT
IGNORE INTO `user_role` (`uid`,`rid`,`expiredAt`) VALUES (0,3,NULL); /* root用户具有超级管理员权限 */

