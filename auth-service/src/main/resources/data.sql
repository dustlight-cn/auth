/**
 * 表数据
 * version: 0
 */

/* 用户 Users */
LOCK TABLES `users` WRITE;
INSERT INTO `users` VALUES (0,'test','$2a$10$GP57XrnKif0JriZlHCyu2OwfMvB3/VpOUEZPvvAnleVOPYpvMR79e','','test',00,NULL,NULL,NULL,1,NULL,NULL);
UNLOCK TABLES;

/* 权限 Authorities */
LOCK TABLES `authorities` WRITE;
INSERT INTO `authorities` VALUES (0,'READ_USER','读取用户',NULL,NULL),(1,'WRITE_USER','修改用户',NULL,NULL),(2,'READ_USER_ANY','读取任意用户',NULL,NULL),(3,'WRITE_USER_ANY','修改任意用户',NULL,NULL),(5,'DELETE_USER','删除用户',NULL,NULL),(6,'DELETE_USER_ANY','删除任意用户',NULL,NULL),(100,'WRITE_AUTHORITY','修改权限',NULL,NULL),(101,'DELETE_AUTHORITY','删除权限',NULL,NULL),(200,'WRITE_ROLE','修改角色',NULL,NULL),(201,'DELETE_ROLE','删除角色',NULL,NULL),(202,'WRITE_USER_ROLE','修改用户角色',NULL,NULL),(300,'WRITE_SCOPE','修改授权作用域',NULL,NULL),(301,'DELETE_SCOPE','删除授权作用域',NULL,NULL),(400,'WRITE_TYPE','修改授权模式',NULL,NULL),(401,'DELETE_TYPE','删除授权模式',NULL,NULL),(500,'CREATE_CLIENT','创建应用',NULL,NULL),(501,'WRITE_CLIENT','修改应用',NULL,NULL),(503,'WRITE_CLIENT_ANY','修改任意应用',NULL,NULL),(504,'DELETE_CLIENT_ANY','删除任意应用',NULL,NULL),(505,'READ_CLIENT_ANY','获取任意应用',NULL,NULL);
UNLOCK TABLES;

/* 角色 Roles */
LOCK TABLES `roles` WRITE;
INSERT INTO `roles` VALUES (0,'User','用户',NULL,NULL),(1,'Developer','开发者',NULL,NULL),(2,'Tester','测试员',NULL,NULL),(3,'Administrator','管理员',NULL,NULL),(4,'Root','超级管理员',NULL,NULL);
UNLOCK TABLES;

/* 授权作用域 Scopes */
LOCK TABLES `scopes` WRITE;
INSERT INTO `scopes` VALUES (0,'read:user','读取用户信息',NULL,NULL,NULL),(1,'write:user','修改用户信息',NULL,NULL,NULL);
UNLOCK TABLES;

/* 授权类型 GrantTypes */
LOCK TABLES `types` WRITE;
INSERT INTO `types` VALUES (0,'authorization_code','授权码模式',NULL,NULL),(1,'refresh_token','令牌刷新',NULL,NULL),(2,'implicit ','简易模式',NULL,NULL),(3,'client_credentials','客户端凭据模式',NULL,NULL),(4,'password','密码模式',NULL,NULL),(12,'qwqwqw','xxxaaass',NULL,NULL);
UNLOCK TABLES;

/* 应用 Clients */
LOCK TABLES `clients` WRITE;
INSERT INTO `clients` VALUES ('a',0,'$2a$10$GP57XrnKif0JriZlHCyu2OwfMvB3/VpOUEZPvvAnleVOPYpvMR79e','test','Test client','http://localhost:8080/',7200,86400,NULL,0,NULL,NULL);
UNLOCK TABLES;

/* 资源 Resources */
LOCK TABLES `resources` WRITE;
UNLOCK TABLES;

/* 应用权限 ClientAuthorities */
LOCK TABLES `client_authority` WRITE;
INSERT INTO `client_authority` VALUES ('a',0),('a',1),('a',2),('a',3),('a',5),('a',6),('a',100),('a',101),('a',200),('a',201),('a',202),('a',300),('a',301),('a',400),('a',401),('a',500),('a',501),('a',503),('a',504),('a',505);
UNLOCK TABLES;

/* 应用资源 ClientResources */
LOCK TABLES `client_resource` WRITE;
UNLOCK TABLES;

/* 应用授权作用域 ClientScopes */
LOCK TABLES `client_scope` WRITE;
INSERT INTO `client_scope` VALUES ('a',0,0),('a',1,0);
UNLOCK TABLES;

/* 应用授权类型 ClientGrantTypes */
LOCK TABLES `client_type` WRITE;
INSERT INTO `client_type` VALUES ('a',0),('a',1),('a',2),('a',3),('a',4);
UNLOCK TABLES;

/* 角色权限 RoleAuthorities */
LOCK TABLES `role_authority` WRITE;
INSERT INTO `role_authority` VALUES (0,0),(4,0),(0,1),(4,1),(4,2),(4,3),(0,5),(4,5),(4,6),(4,100),(4,101),(4,200),(4,201),(4,202),(4,300),(4,301),(4,400),(4,401),(1,500),(4,500),(1,501),(4,501),(4,503),(4,504),(4,505);
UNLOCK TABLES;

/* 用户角色 UserRoles */
LOCK TABLES `user_role` WRITE;
INSERT INTO `user_role` VALUES (0,0,NULL,NULL,NULL),(0,1,NULL,NULL,NULL);
UNLOCK TABLES;

