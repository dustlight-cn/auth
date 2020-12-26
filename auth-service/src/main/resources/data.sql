/**
 * 表数据
 * version: 0
 */

/* 用户 Users */
INSERT IGNORE INTO `users` (`uid`,`username`,`password`,`enabled`) VALUES
(0,'root','$2a$10$GP57XrnKif0JriZlHCyu2OwfMvB3/VpOUEZPvvAnleVOPYpvMR79e',1);

/* 权限 Authorities */
INSERT IGNORE INTO `authorities` (`aid`,`authorityName`,`authorityDescription`) VALUES
(0,'READ_USER','读取用户'),
(1,'WRITE_USER','修改用户'),
(2,'READ_USER_ANY','读取任意用户'),
(3,'WRITE_USER_ANY','修改任意用户'),
(5,'DELETE_USER','删除用户'),
(6,'DELETE_USER_ANY','删除任意用户'),
(100,'WRITE_AUTHORITY','修改权限'),
(101,'DELETE_AUTHORITY','删除权限'),
(200,'WRITE_ROLE','修改角色'),
(201,'DELETE_ROLE','删除角色'),
(202,'WRITE_USER_ROLE','修改用户角色'),
(300,'WRITE_SCOPE','修改授权作用域'),
(301,'DELETE_SCOPE','删除授权作用域'),
(400,'WRITE_TYPE','修改授权模式'),
(401,'DELETE_TYPE','删除授权模式'),
(500,'CREATE_CLIENT','创建应用'),
(501,'WRITE_CLIENT','修改应用'),
(503,'WRITE_CLIENT_ANY','修改任意应用'),
(504,'DELETE_CLIENT_ANY','删除任意应用'),
(505,'READ_CLIENT_ANY','获取任意应用');

/* 角色 Roles */
INSERT IGNORE INTO `roles` (`rid`,`roleName`,`roleDescription`) VALUES
(0,'User','用户'),
(1,'Developer','开发者'),
(2,'Tester','测试员'),
(3,'Administrator','管理员'),
(4,'Root','超级管理员');

/* 授权作用域 Scopes */
INSERT IGNORE INTO `scopes` (`sid`,`name`,`subtitle`,`description`) VALUES
(0,'read:user','读取用户信息',NULL),
(1,'write:user','修改用户信息',NULL);

/* 授权类型 GrantTypes */
INSERT IGNORE INTO `types` (`tid`,`name`,`description`) VALUES
(0,'authorization_code','授权码模式'),
(1,'refresh_token','令牌刷新'),
(2,'implicit ','简易模式'),
(3,'client_credentials','客户端凭据模式'),
(4,'password','密码模式');

/* 应用 Clients */
INSERT IGNORE INTO `clients` (`cid`,`uid`,`secret`,`name`,`description`,`redirectUri`,`accessTokenValidity`,`refreshTokenValidity`,`additionalInformation`,`status`) VALUES
('default',0,NULL,'default','Default Client',NULL,7200,86400,NULL,0);

/* 资源 Resources */


/* 应用权限 ClientAuthorities */


/* 应用资源 ClientResources */


/* 应用授权作用域 ClientScopes */
INSERT IGNORE INTO `client_scope` SELECT 'default',sid,0 FROM `scopes` WHERE sid BETWEEN 0 AND 999;

/* 应用授权类型 ClientGrantTypes */


/* 角色权限 RoleAuthorities */
INSERT IGNORE INTO `role_authority` VALUES /* 普通用户具有用户信息读写权限 */
(0,0),(0,1);
INSERT IGNORE INTO `role_authority` /* 超级管理员具有所有权限 */
SELECT 4,aid FROM `authorities`;

/* 用户角色 UserRoles */
INSERT IGNORE INTO `user_role` (`uid`,`rid`,`expiredAt`) VALUES (0,4,NULL); /* root用户具有超级管理员权限 */

