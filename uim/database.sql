-- MySQL dump 10.16  Distrib 10.1.44-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: dustlight.cn    Database: oauth
-- ------------------------------------------------------
-- Server version	10.4.13-MariaDB-1:10.4.13+maria~bionic

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
-- Current Database: `oauth`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `oauth` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `oauth`;

--
-- Table structure for table `authority_details`
--

DROP TABLE IF EXISTS `authority_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authority_details` (
  `id` bigint(20) NOT NULL,
  `authority_name` varchar(45) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `ceratedAt` datetime DEFAULT current_timestamp(),
  `updatedAt` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `authority_name_UNIQUE` (`authority_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authority_details`
--

LOCK TABLES `authority_details` WRITE;
/*!40000 ALTER TABLE `authority_details` DISABLE KEYS */;
INSERT INTO `authority_details` VALUES (0,'READ_USERINFO','获取用户信息','2020-06-19 18:57:01','2020-06-19 18:57:01'),(1,'WRITE_USERINFO','修改用户信息','2020-06-19 18:57:01','2020-06-19 18:57:01'),(2,'READ_USERINFNO_ANY','获取任意用户信息','2020-06-19 18:57:01','2020-06-19 18:57:01'),(3,'WRITE_USERINFO_ANY','修改任意用户信息','2020-06-19 18:57:01','2020-06-19 18:57:01'),(4,'CREATE_CLIENT','创建应用','2020-06-19 18:57:02','2020-06-19 18:57:02'),(5,'DELETE_CLIENT','删除应用','2020-06-19 18:57:02','2020-06-19 18:57:02'),(6,'DELETE_CLIENT_ANY','删除任意应用','2020-06-19 18:57:02','2020-06-19 18:57:02'),(7,'UPDATE_CLIENT','修改应用信息','2020-06-19 18:57:02','2020-06-19 18:57:02'),(8,'UPDATE_CLIENT_ANY','修改任意应用信息','2020-06-19 18:57:02','2020-06-19 18:57:02'),(9,'READ_TEPMLATE','获取模板','2020-06-19 18:57:02','2020-06-19 18:57:02'),(10,'WRITE_TEMPLATE','修改模板','2020-06-19 18:57:02','2020-06-19 18:57:02'),(11,'DELETE_TEMPLATE','删除模板','2020-06-19 18:57:02','2020-06-19 18:57:02');
/*!40000 ALTER TABLE `authority_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_scope`
--

DROP TABLE IF EXISTS `client_scope`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_scope` (
  `client_id` varchar(256) NOT NULL,
  `scope_id` bigint(20) NOT NULL,
  PRIMARY KEY (`client_id`,`scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_scope`
--

LOCK TABLES `client_scope` WRITE;
/*!40000 ALTER TABLE `client_scope` DISABLE KEYS */;
INSERT INTO `client_scope` VALUES ('order-client',0),('rO0ABXcJbn+t0wl58AAA',0),('rO0ABXcJbn+t1ee58AAA',0),('rO0ABXcJbn+t1Jh58AAA',0),('rO0ABXcJbn7Q7uQJ8AAA',0),('user-client',0);
/*!40000 ALTER TABLE `client_scope` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauth_client_details`
--

DROP TABLE IF EXISTS `oauth_client_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(256) NOT NULL,
  `uid` bigint(20) NOT NULL DEFAULT 0,
  `client_name` varchar(256) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) NOT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_client_details`
--

LOCK TABLES `oauth_client_details` WRITE;
/*!40000 ALTER TABLE `oauth_client_details` DISABLE KEYS */;
INSERT INTO `oauth_client_details` VALUES ('order-client',0,'','','$2a$10$sNNrAJ/Cdh8YHCK1f8s7KeS0zPfdgFyuxIy7m5CRaaV1w8vuUe2Oe','userinfo','authorization_code,refresh_token,password,implicit,client_credentials','http://localhost:8080/hello','ROLE_BASIC',3600,36000,NULL,'1'),('rO0ABXcJbn+t0wl58AAA',7951073035650945024,'App1','','$2a$10$z8ctejDAi.iHg0CHuMOftun/Ekd4rVZ7cLrDP4JndwM0AsPXjoEGi','userinfo,all','authorization_code,refresh_token,password,implicit','http://localhost:1234/','',NULL,NULL,NULL,'1'),('rO0ABXcJbn+t1ee58AAA',7951073035650945024,'App1','','$2a$10$0nDfIHggkJUa5wXWYwhXUO2xlhbopg/h38Qstadg1AUXxWFHJaDpa','userinfo,all','authorization_code,refresh_token,password,implicit','http://localhost:1234/','',NULL,NULL,NULL,'1'),('rO0ABXcJbn+t1Jh58AAA',7951073035650945024,'App1','','$2a$10$uHTrb6IcZrmJnsmIhZWZY.1NHNRLYtfRsWjYhXkQdCS8oCGA88h/q','userinfo,all','authorization_code,refresh_token,password,implicit','http://localhost:1234/','',NULL,NULL,'{“name\": \"123\"}','1'),('rO0ABXcJbn7Q7uQJ8AAA',7951073035650945024,'App1','','$2a$10$sed.JRDVMSwVHSYgjw4NxuLSiIILAd1U4frUqDzzxjgJ4pGvvZdiy','userinfo,all','authorization_code,refresh_token,password,implicit,client_credentials','http://localhost:1234/','',NULL,NULL,NULL,'1'),('user-client',0,'','','$2a$10$sNNrAJ/Cdh8YHCK1f8s7KeS0zPfdgFyuxIy7m5CRaaV1w8vuUe2Oe','all','authorization_code,refresh_token,password','http://localhost:8080/hello','',3600,36000,NULL,'1');
/*!40000 ALTER TABLE `oauth_client_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_authority`
--

DROP TABLE IF EXISTS `role_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_authority` (
  `rid` bigint(20) NOT NULL,
  `aid` bigint(20) NOT NULL,
  PRIMARY KEY (`rid`,`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_authority`
--

LOCK TABLES `role_authority` WRITE;
/*!40000 ALTER TABLE `role_authority` DISABLE KEYS */;
INSERT INTO `role_authority` VALUES (0,0),(0,1),(0,2),(0,3),(0,4),(0,5),(0,6),(0,7),(0,8),(0,9),(0,10),(0,11);
/*!40000 ALTER TABLE `role_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_details`
--

DROP TABLE IF EXISTS `role_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_details` (
  `id` bigint(20) NOT NULL,
  `role_name` varchar(45) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `createdAt` datetime DEFAULT current_timestamp(),
  `updatedAt` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name_UNIQUE` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_details`
--

LOCK TABLES `role_details` WRITE;
/*!40000 ALTER TABLE `role_details` DISABLE KEYS */;
INSERT INTO `role_details` VALUES (0,'ROLE_ROOT','根角色','2020-06-19 18:44:40','2020-06-19 18:44:40'),(1,'ROLE_ADMIN','管理员','2020-06-19 18:44:40','2020-06-19 18:44:40'),(2,'ROLE_DEV','开发者','2020-06-19 18:44:40','2020-06-19 18:44:40'),(3,'ROLE_USER','普通用户','2020-06-19 18:48:42','2020-06-19 18:48:42');
/*!40000 ALTER TABLE `role_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scope_authority`
--

DROP TABLE IF EXISTS `scope_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scope_authority` (
  `sid` bigint(20) NOT NULL,
  `aid` bigint(20) NOT NULL,
  PRIMARY KEY (`sid`,`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
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
-- Table structure for table `scope_details`
--

DROP TABLE IF EXISTS `scope_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scope_details` (
  `id` bigint(20) NOT NULL,
  `scope_name` varchar(32) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `createdAt` datetime DEFAULT current_timestamp(),
  `updatedAt` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `scope_name_UNIQUE` (`scope_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scope_details`
--

LOCK TABLES `scope_details` WRITE;
/*!40000 ALTER TABLE `scope_details` DISABLE KEYS */;
INSERT INTO `scope_details` VALUES (0,'userinfo','获取用户信息','2020-06-19 19:29:57','2020-06-19 19:29:57');
/*!40000 ALTER TABLE `scope_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sender_templates`
--

DROP TABLE IF EXISTS `sender_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sender_templates` (
  `name` varchar(64) NOT NULL,
  `text` text DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='-';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sender_templates`
--

LOCK TABLES `sender_templates` WRITE;
/*!40000 ALTER TABLE `sender_templates` DISABLE KEYS */;
INSERT INTO `sender_templates` VALUES ('registerVerificationCode','<html>\n<head>\n<title>邮箱验证</title>\n</head>\n<body>\n<h4>邮箱验证</h4>\n<p>您的验证码是： <b th:text=\"${code}\"></b></p>\n</body>\n</html>');
/*!40000 ALTER TABLE `sender_templates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_details`
--

DROP TABLE IF EXISTS `user_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_details` (
  `uid` bigint(20) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(128) NOT NULL,
  `email` varchar(320) NOT NULL,
  `nickname` varchar(45) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `gender` int(2) unsigned zerofill DEFAULT 00,
  `role` bigint(20) DEFAULT 4,
  `enabled` int(1) DEFAULT 1,
  `account_expired` int(1) DEFAULT 0,
  `credentials_expired` int(1) DEFAULT 0,
  `account_locked` int(1) DEFAULT 0,
  `createdAt` datetime DEFAULT current_timestamp(),
  `updatedAt` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid_UNIQUE` (`uid`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `phone_UNIQUE` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_details`
--

LOCK TABLES `user_details` WRITE;
/*!40000 ALTER TABLE `user_details` DISABLE KEYS */;
INSERT INTO `user_details` VALUES (0,'hansin','$2a$10$1QW45Y2n8HB2B57Rqh.TC.qL5YbjOUixejBQKSSUeEr0HYs8lX.Wy','',NULL,NULL,00,0,1,0,0,0,'2020-05-16 17:26:58','2020-06-19 19:13:24'),(7950744839713804288,'hansin1997','$2a$10$pMas8axi9Q626lalsYz6BeflYPyb2s6RI4fHx2TtZJal/uAWTIDDe','hansin@dustlight.cn',NULL,NULL,00,NULL,1,0,0,0,'2020-05-18 15:25:01','2020-06-19 19:12:48'),(7950788585357135872,'111111','$10$1QW45Y2n8HB2B57Rqh.TC.qL5YbjOUixejBQKSSUeEr0HYs8lX.Wy','gg@GG','',NULL,00,NULL,1,0,0,0,'2020-05-18 18:18:51','2020-06-19 19:12:48'),(7951073035650945024,'222222','$2a$10$cFAoUfkHm4FzwQ95jZUJvutJdYlBJFF.JmNR6kMPD7z8ZIb4myjGS','ww','gggg',NULL,00,NULL,1,0,0,0,'2020-05-19 13:09:09','2020-06-19 19:12:48'),(7961394947674992640,'wwwwww','$2a$10$w/9Q6C6TZMGIx2lEX.M2vuePcKr.ViLFEGg7Z6ZGcliS5VgMNCPFa','lbgzs2010@live.cn','',NULL,00,NULL,1,0,0,0,'2020-06-17 00:44:45','2020-06-19 19:12:48');
/*!40000 ALTER TABLE `user_details` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-20  4:56:45
