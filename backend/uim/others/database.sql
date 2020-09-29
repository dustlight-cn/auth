-- MySQL dump 10.13  Distrib 5.7.30, for Linux (x86_64)
--
-- Host: 192.168.199.218    Database: uim
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `authorities`
--

DROP TABLE IF EXISTS `authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authorities` (
  `aid` bigint NOT NULL,
  `authorityName` varchar(64) NOT NULL,
  `authorityDescription` varchar(128) DEFAULT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`aid`),
  UNIQUE KEY `authorityName_UNIQUE` (`authorityName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authorities`
--

LOCK TABLES `authorities` WRITE;
/*!40000 ALTER TABLE `authorities` DISABLE KEYS */;
INSERT INTO `authorities` VALUES (0,'READ_USER','读取用户信息','2020-09-14 23:59:57','2020-09-17 16:20:43'),(1,'WRITE_USER','修改用户信息','2020-09-14 23:59:57','2020-09-17 16:20:43'),(2,'READ_USER_ANY','读取任意用户信息','2020-09-15 00:15:11','2020-09-17 16:20:43'),(3,'WRITE_USER_ANY','修改任意用户信息','2020-09-15 00:15:11','2020-09-17 16:20:43'),(4,'CREATE_USER','创建用户','2020-09-15 00:15:11','2020-09-15 00:15:11'),(5,'DELETE_USER','删除用户','2020-09-15 00:15:11','2020-09-15 00:15:11'),(6,'GRANT_USER','修改用户权限','2020-09-15 00:15:11','2020-09-15 00:15:11'),(7,'QUERY_USER','查询用户','2020-09-15 00:15:11','2020-09-15 00:15:11');
/*!40000 ALTER TABLE `authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_authority`
--

DROP TABLE IF EXISTS `client_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_authority` (
  `cid` varchar(64) NOT NULL,
  `aid` bigint NOT NULL,
  PRIMARY KEY (`cid`,`aid`),
  KEY `client_authority_aid_idx` (`aid`),
  CONSTRAINT `client_authority_aid` FOREIGN KEY (`aid`) REFERENCES `authorities` (`aid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `client_authority_cid` FOREIGN KEY (`cid`) REFERENCES `clients` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_authority`
--

LOCK TABLES `client_authority` WRITE;
/*!40000 ALTER TABLE `client_authority` DISABLE KEYS */;
/*!40000 ALTER TABLE `client_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_resource`
--

DROP TABLE IF EXISTS `client_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_resource` (
  `cid` varchar(64) NOT NULL,
  `rid` varchar(64) NOT NULL,
  PRIMARY KEY (`cid`,`rid`),
  KEY `client_resource_rid_idx` (`rid`),
  CONSTRAINT `client_resource_cid` FOREIGN KEY (`cid`) REFERENCES `clients` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `client_resource_rid` FOREIGN KEY (`rid`) REFERENCES `resources` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_resource`
--

LOCK TABLES `client_resource` WRITE;
/*!40000 ALTER TABLE `client_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `client_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_scope`
--

DROP TABLE IF EXISTS `client_scope`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_scope` (
  `cid` varchar(64) NOT NULL,
  `sid` bigint NOT NULL,
  `autoApprove` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`cid`,`sid`),
  KEY `client_scope_sid_idx` (`sid`) /*!80000 INVISIBLE */,
  KEY `client_scope_cid_idx` (`cid`) /*!80000 INVISIBLE */,
  CONSTRAINT `client_scope_cid` FOREIGN KEY (`cid`) REFERENCES `clients` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `client_scope_sid` FOREIGN KEY (`sid`) REFERENCES `scopes` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_scope`
--

LOCK TABLES `client_scope` WRITE;
/*!40000 ALTER TABLE `client_scope` DISABLE KEYS */;
INSERT INTO `client_scope` VALUES ('a',0,NULL);
/*!40000 ALTER TABLE `client_scope` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_type`
--

DROP TABLE IF EXISTS `client_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_type` (
  `cid` varchar(64) NOT NULL,
  `tid` bigint NOT NULL,
  PRIMARY KEY (`cid`,`tid`),
  KEY `client_type_tid_idx` (`tid`),
  CONSTRAINT `client_type_cid` FOREIGN KEY (`cid`) REFERENCES `clients` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `client_type_tid` FOREIGN KEY (`tid`) REFERENCES `types` (`tid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_type`
--

LOCK TABLES `client_type` WRITE;
/*!40000 ALTER TABLE `client_type` DISABLE KEYS */;
INSERT INTO `client_type` VALUES ('a',0),('a',1);
/*!40000 ALTER TABLE `client_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients` (
  `cid` varchar(64) NOT NULL,
  `uid` bigint NOT NULL,
  `secret` varchar(128) NOT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `redirectUri` varchar(1024) DEFAULT NULL,
  `accessTokenValidity` int DEFAULT '7200',
  `refreshTokenValidity` int DEFAULT '86400',
  `additionalInformation` varchar(2048) DEFAULT NULL,
  `status` tinyint DEFAULT '0',
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cid`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `client_owner_idx` (`uid`),
  FULLTEXT KEY `client_keywords` (`name`,`description`,`redirectUri`,`cid`) /*!50100 WITH PARSER `ngram` */ ,
  CONSTRAINT `client_owner` FOREIGN KEY (`uid`) REFERENCES `users` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
INSERT INTO `clients` VALUES ('a',0,'a','a','a','http://localhost:8080',7200,86400,NULL,0,'2020-09-22 23:24:23','2020-09-22 23:24:23');
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resources`
--

DROP TABLE IF EXISTS `resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resources` (
  `rid` varchar(64) NOT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rid`),
  CONSTRAINT `resources_rid` FOREIGN KEY (`rid`) REFERENCES `clients` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resources`
--

LOCK TABLES `resources` WRITE;
/*!40000 ALTER TABLE `resources` DISABLE KEYS */;
/*!40000 ALTER TABLE `resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_authority`
--

DROP TABLE IF EXISTS `role_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_authority` (
  `rid` bigint NOT NULL,
  `aid` bigint NOT NULL,
  PRIMARY KEY (`rid`,`aid`),
  KEY `aid_idx` (`aid`),
  KEY `rid_idx` (`rid`),
  CONSTRAINT `role_authority_aid` FOREIGN KEY (`aid`) REFERENCES `authorities` (`aid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `role_authority_rid` FOREIGN KEY (`rid`) REFERENCES `roles` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=ascii;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_authority`
--

LOCK TABLES `role_authority` WRITE;
/*!40000 ALTER TABLE `role_authority` DISABLE KEYS */;
INSERT INTO `role_authority` VALUES (0,0),(4,0),(0,1),(4,1),(4,2),(4,3),(4,4),(4,5),(4,6),(4,7);
/*!40000 ALTER TABLE `role_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `rid` bigint NOT NULL,
  `roleName` varchar(64) NOT NULL,
  `roleDescription` varchar(128) DEFAULT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rid`),
  UNIQUE KEY `roleName_UNIQUE` (`roleName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (0,'User','用户','2020-09-15 00:00:48','2020-09-15 00:00:48'),(1,'Developer','开发者','2020-09-15 00:18:30','2020-09-15 00:18:30'),(2,'Tester','测试员','2020-09-15 00:18:30','2020-09-15 00:18:30'),(3,'Administrator','管理员','2020-09-15 00:18:30','2020-09-15 00:18:30'),(4,'Root','超级管理员','2020-09-15 00:18:30','2020-09-15 00:18:30');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scope_authority`
--

DROP TABLE IF EXISTS `scope_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scope_authority` (
  `sid` bigint NOT NULL,
  `aid` bigint NOT NULL,
  PRIMARY KEY (`sid`,`aid`),
  KEY `scope_authority_aid_idx` (`aid`) /*!80000 INVISIBLE */,
  KEY `scope_authority_sid_idx` (`sid`) /*!80000 INVISIBLE */,
  CONSTRAINT `scope_authority_aid` FOREIGN KEY (`aid`) REFERENCES `authorities` (`aid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `scope_authority_sid` FOREIGN KEY (`sid`) REFERENCES `scopes` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scope_authority`
--

LOCK TABLES `scope_authority` WRITE;
/*!40000 ALTER TABLE `scope_authority` DISABLE KEYS */;
INSERT INTO `scope_authority` VALUES (0,0);
/*!40000 ALTER TABLE `scope_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scopes`
--

DROP TABLE IF EXISTS `scopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scopes` (
  `sid` bigint NOT NULL,
  `name` varchar(45) NOT NULL,
  `subtitle` varchar(64) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scopes`
--

LOCK TABLES `scopes` WRITE;
/*!40000 ALTER TABLE `scopes` DISABLE KEYS */;
INSERT INTO `scopes` VALUES (0,'userinfo','读取用户信息',NULL,'2020-09-28 18:35:37','2020-09-28 18:35:37');
/*!40000 ALTER TABLE `scopes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `templates`
--

DROP TABLE IF EXISTS `templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `templates` (
  `name` varchar(64) NOT NULL,
  `uid` bigint NOT NULL,
  `title` varchar(256) NOT NULL,
  `content` text NOT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`name`),
  KEY `template_owner_idx` (`uid`),
  CONSTRAINT `template_owner` FOREIGN KEY (`uid`) REFERENCES `users` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `templates`
--

LOCK TABLES `templates` WRITE;
/*!40000 ALTER TABLE `templates` DISABLE KEYS */;
INSERT INTO `templates` VALUES ('register',0,'注册验证码','您的验证码是：${code}','2020-09-21 23:38:25','2020-09-22 05:33:03');
/*!40000 ALTER TABLE `templates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `types`
--

DROP TABLE IF EXISTS `types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `types` (
  `tid` bigint NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tid`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `types`
--

LOCK TABLES `types` WRITE;
/*!40000 ALTER TABLE `types` DISABLE KEYS */;
INSERT INTO `types` VALUES (0,'authorization_code','授权码模式','2020-09-22 23:48:18','2020-09-22 23:48:18'),(1,'refresh_token','令牌刷新','2020-09-22 23:48:18','2020-09-22 23:48:18'),(2,'implicit ','简易模式','2020-09-22 23:48:18','2020-09-22 23:48:18'),(3,'client_credentials','客户端凭据模式','2020-09-22 23:48:18','2020-09-22 23:48:18'),(4,'password','密码模式','2020-09-22 23:48:18','2020-09-22 23:48:18'),(12,'qwqwqw','xxxaaass','2020-09-28 20:32:50','2020-09-28 20:36:57');
/*!40000 ALTER TABLE `types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `uid` bigint NOT NULL,
  `rid` bigint NOT NULL,
  `expiredAt` datetime DEFAULT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`,`rid`),
  KEY `rid_idx` (`rid`),
  KEY `uid_idx` (`uid`),
  CONSTRAINT `user_role_rid` FOREIGN KEY (`rid`) REFERENCES `roles` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_role_uid` FOREIGN KEY (`uid`) REFERENCES `users` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=ascii;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (0,0,NULL,'2020-09-16 12:23:32','2020-09-16 12:23:32'),(0,1,NULL,'2020-09-16 12:23:32','2020-09-16 12:23:32'),(0,2,NULL,'2020-09-16 12:23:32','2020-09-16 12:23:32'),(0,3,NULL,'2020-09-16 12:23:32','2020-09-16 12:23:32'),(0,4,NULL,'2020-09-16 12:23:32','2020-09-16 12:23:32'),(503694347777703936,0,'2020-09-16 12:26:42','2020-09-16 12:25:04','2020-09-16 12:25:43'),(503694347777703936,1,'2020-09-16 12:26:42','2020-09-16 12:25:04','2020-09-16 12:25:43'),(503694347777703936,2,'2020-09-16 12:26:42','2020-09-16 12:25:04','2020-09-16 12:25:43'),(504135522511269888,0,NULL,'2020-09-17 16:55:41','2020-09-17 16:55:41'),(505966692622970880,0,NULL,'2020-09-22 18:12:06','2020-09-22 18:12:06'),(505970177499770880,0,NULL,'2020-09-22 18:25:57','2020-09-22 18:25:57');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `uid` bigint NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(128) NOT NULL,
  `email` varchar(320) NOT NULL,
  `nickname` varchar(32) NOT NULL DEFAULT '',
  `gender` int(2) unsigned zerofill DEFAULT '00',
  `accountExpiredAt` datetime DEFAULT NULL,
  `credentialsExpiredAt` datetime DEFAULT NULL,
  `unlockedAt` datetime DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  FULLTEXT KEY `user_keywords` (`username`,`email`,`nickname`) /*!50100 WITH PARSER `ngram` */ 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (0,'root','$2a$10$XBQXZQLQg.s5/8JO/HdSXe7GpAxqgyglzywvZ4nZZPPnhNZhcyhLq','-','ROOT',00,NULL,NULL,NULL,1,'2020-09-15 00:27:10','2020-09-17 03:44:07'),(1,'root1','root1','-1','ROOT1',00,NULL,NULL,NULL,1,'2020-09-15 00:28:10','2020-09-16 16:45:54'),(2,'root2','root2','-2','ROOT2',00,NULL,NULL,NULL,1,'2020-09-15 00:29:10','2020-09-16 16:45:54'),(3,'root3','root3','-3','ROOT3',00,NULL,NULL,NULL,1,'2020-09-15 00:30:10','2020-09-16 16:45:54'),(503694347777703936,'ee-84e0-d20a8cb6b447','$2a$10$ixV4Wx5TZqAAbnyibaMBreNUtJAtwA2Jx1ROwBiL3XRo/ew.vJ7iK','ee-84e0-d20a8cb6b447','ZTmfey',00,'2020-09-16 11:47:36',NULL,NULL,1,'2020-09-16 11:42:37','2020-09-16 11:42:37'),(504135522511269888,'hansin','$2a$10$GP57XrnKif0JriZlHCyu2OwfMvB3/VpOUEZPvvAnleVOPYpvMR79e','hansin@dustlight.cn','hansin',00,NULL,NULL,NULL,1,'2020-09-17 16:55:41','2020-09-22 20:13:50'),(505966692622970880,'845612500','$2a$10$4D/p8ANABGJ5OvLfTYYMZuoqz4Fyu8eJoAoVdYdZtZKXngmnId/zS','845612500@qq.com','845612500',01,NULL,NULL,NULL,1,'2020-09-22 18:12:06','2020-09-22 18:13:26'),(505970177499770880,'lbgzs2010','$2a$10$kySdeEvbrRXdpt07KRkpn.8/jxmLCgTxz91lAAWlvuQDIu4VGSJW.','lbgzs2010@live.cn','lbgzs2010',00,NULL,NULL,NULL,1,'2020-09-22 18:25:57','2020-09-22 18:25:57');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-09-29  9:12:55
