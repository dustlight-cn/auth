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
INSERT INTO `authority_details` VALUES (0,'READ_USERINFO','获取用户信息','2020-06-19 18:57:01','2020-06-19 18:57:01'),(1,'WRITE_USERINFO','修改用户信息','2020-06-19 18:57:01','2020-06-19 18:57:01'),(2,'READ_USERINFNO_ANY','获取任意用户信息','2020-06-19 18:57:01','2020-06-19 18:57:01'),(3,'WRITE_USERINFO_ANY','修改任意用户信息','2020-06-19 18:57:01','2020-06-19 18:57:01'),(4,'CREATE_CLIENT','创建应用','2020-06-19 18:57:02','2020-06-19 18:57:02'),(5,'DELETE_CLIENT','删除应用','2020-06-19 18:57:02','2020-06-19 18:57:02'),(6,'DELETE_CLIENT_ANY','删除任意应用','2020-06-19 18:57:02','2020-06-19 18:57:02'),(7,'UPDATE_CLIENT','修改应用信息','2020-06-19 18:57:02','2020-06-19 18:57:02'),(8,'UPDATE_CLIENT_ANY','修改任意应用信息','2020-06-19 18:57:02','2020-06-19 18:57:02'),(9,'READ_TEMPLATE','获取模板','2020-06-19 18:57:02','2020-06-19 18:57:02'),(10,'WRITE_TEMPLATE','修改模板','2020-06-19 18:57:02','2020-06-19 18:57:02'),(11,'DELETE_TEMPLATE','删除模板','2020-06-19 18:57:02','2020-06-19 18:57:02'),(100,'TEST','测试权限','2020-06-20 22:27:19','2020-06-20 22:27:19');
/*!40000 ALTER TABLE `authority_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_authority`
--

DROP TABLE IF EXISTS `client_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_authority` (
  `cid` varchar(256) NOT NULL,
  `aid` bigint(20) NOT NULL,
  PRIMARY KEY (`cid`,`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_authority`
--

LOCK TABLES `client_authority` WRITE;
/*!40000 ALTER TABLE `client_authority` DISABLE KEYS */;
INSERT INTO `client_authority` VALUES ('order-client',1);
/*!40000 ALTER TABLE `client_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_grant_types`
--

DROP TABLE IF EXISTS `client_grant_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_grant_types` (
  `cid` bigint(20) NOT NULL,
  `tid` bigint(20) NOT NULL,
  PRIMARY KEY (`cid`,`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_grant_types`
--

LOCK TABLES `client_grant_types` WRITE;
/*!40000 ALTER TABLE `client_grant_types` DISABLE KEYS */;
INSERT INTO `client_grant_types` VALUES (0,0),(0,4);
/*!40000 ALTER TABLE `client_grant_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_resource`
--

DROP TABLE IF EXISTS `client_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_resource` (
  `cid` varchar(256) NOT NULL,
  `rid` bigint(20) NOT NULL,
  PRIMARY KEY (`cid`,`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
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
  `cid` varchar(256) NOT NULL,
  `sid` bigint(20) NOT NULL,
  `auto_approve` int(1) DEFAULT NULL,
  PRIMARY KEY (`cid`,`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_scope`
--

LOCK TABLES `client_scope` WRITE;
/*!40000 ALTER TABLE `client_scope` DISABLE KEYS */;
INSERT INTO `client_scope` VALUES ('order-client',0,0),('order-client',1,0);
/*!40000 ALTER TABLE `client_scope` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grant_types`
--

DROP TABLE IF EXISTS `grant_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grant_types` (
  `id` bigint(20) NOT NULL,
  `grant_type` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `grant_type_UNIQUE` (`grant_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grant_types`
--

LOCK TABLES `grant_types` WRITE;
/*!40000 ALTER TABLE `grant_types` DISABLE KEYS */;
INSERT INTO `grant_types` VALUES (0,'authorization_code'),(2,'client_credentials'),(3,'implicit '),(1,'password'),(4,'refresh_token');
/*!40000 ALTER TABLE `grant_types` ENABLE KEYS */;
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
  `client_secret` varchar(256) NOT NULL,
  `client_name` varchar(256) NOT NULL,
  `redirect_uri` varchar(1024) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `enabled` int(1) DEFAULT NULL,
  `description` varchar(4096) DEFAULT NULL,
  `createdAt` datetime DEFAULT current_timestamp(),
  `updatedAt` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`client_id`),
  UNIQUE KEY `client_name_UNIQUE` (`client_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_client_details`
--

LOCK TABLES `oauth_client_details` WRITE;
/*!40000 ALTER TABLE `oauth_client_details` DISABLE KEYS */;
INSERT INTO `oauth_client_details` VALUES ('order-client',0,'$2a$10$sNNrAJ/Cdh8YHCK1f8s7KeS0zPfdgFyuxIy7m5CRaaV1w8vuUe2Oe','测试应用','http://localhost:8080/hello',3600,36000,NULL,NULL,'这是一个测试应用！','2020-06-20 09:23:22','2020-06-21 03:51:36');
/*!40000 ALTER TABLE `oauth_client_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource_details`
--

DROP TABLE IF EXISTS `resource_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource_details` (
  `id` bigint(20) NOT NULL,
  `resource_name` varchar(128) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `createdAt` datetime DEFAULT current_timestamp(),
  `updatedAt` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `resource_name_UNIQUE` (`resource_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource_details`
--

LOCK TABLES `resource_details` WRITE;
/*!40000 ALTER TABLE `resource_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `resource_details` ENABLE KEYS */;
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
INSERT INTO `role_authority` VALUES (0,0),(0,1),(0,2),(0,3),(0,4),(0,5),(0,6),(0,7),(0,8),(0,9),(0,10),(0,11),(4,0),(4,1);
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
INSERT INTO `scope_authority` VALUES (0,0),(1,100);
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
INSERT INTO `scope_details` VALUES (0,'userinfo','获取用户信息','2020-06-19 19:29:57','2020-06-19 19:29:57'),(1,'test','测试权限','2020-06-20 22:26:58','2020-06-20 22:27:30');
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
INSERT INTO `sender_templates` VALUES ('registerVerificationCode','\n\n<title>邮箱验证</title>\n\n\n<h4>邮箱x哈哈验证</h4>\n<p>您的验证码是： <b th:text=\"${code}\"></b></p>\n\n'),('邮箱验证','<h2 id=\"Specifications\" style=\"border-bottom-color: currentColor; border-bottom-style: none; border-bottom-width: 0px; border-image-outset: 0; border-image-repeat: stretch; border-image-slice: 100%; border-image-source: none; border-image-width: 1; border-left-color: currentColor; border-left-style: none; border-left-width: 0px; border-right-color: currentColor; border-right-style: none; border-right-width: 0px; border-top-color: rgb(61, 126, 154); border-top-style: solid; border-top-width: 3px; color: rgb(51, 51, 51); font-family: x-locale-heading-primary,zillaslab,Palatino,&amp;quot;Palatino Linotype&amp;quot;,x-locale-heading-secondary,serif; font-size: 2.33rem; font-style: normal; font-variant: normal; font-weight: 700; letter-spacing: normal; line-height: 1.2; margin-bottom: 20px; margin-left: 0px; margin-right: 0px; margin-top: 60px; orphans: 2; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 40px; position: relative; text-align: left; text-decoration: none; text-indent: 0px; text-transform: none; -webkit-text-stroke-width: 0px; white-space: normal; word-spacing: 0px;\">Specifications</h2><h4><p style=\"border: 0px none currentcolor; box-sizing: border-box; color: rgb(51, 51, 51); font-family: Arial, x-locale-body, sans-serif; font-size: 16px; font-variant-numeric: normal; font-variant-east-asian: normal; letter-spacing: normal; margin-bottom: 24px; max-width: 42rem; padding: 0px;\"></p><span style=\"color: rgb(51, 51, 51); font-family: Arial, x-locale-body, sans-serif; font-size: 16px; font-variant-numeric: normal; font-variant-east-asian: normal; letter-spacing: normal;\">\n\n</span><span style=\"color: rgb(51, 51, 51); font-family: Arial, x-locale-body, sans-serif; font-size: 16px; font-variant-numeric: normal; font-variant-east-asian: normal; letter-spacing: normal;\">\n\n</span><table class=\"standard-table\" style=\"border: 2px solid rgb(255, 255, 255); border-collapse: collapse; box-sizing: border-box; color: rgb(51, 51, 51); font-family: Arial, x-locale-body, sans-serif; font-size: 16px; font-variant-numeric: normal; font-variant-east-asian: normal; letter-spacing: normal; margin: 0px 0px 24px; max-width: 100%; padding: 0px; width: 100%;\">\n <thead style=\"border-bottom-color: currentColor; border-bottom-style: none; border-bottom-width: 0px; border-image-outset: 0; border-image-repeat: stretch; border-image-slice: 100%; border-image-source: none; border-image-width: 1; border-left-color: currentColor; border-left-style: none; border-left-width: 0px; border-right-color: currentColor; border-right-style: none; border-right-width: 0px; border-top-color: currentColor; border-top-style: none; border-top-width: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\">\n  <tr style=\"border-bottom-color: currentColor; border-bottom-style: none; border-bottom-width: 0px; border-image-outset: 0; border-image-repeat: stretch; border-image-slice: 100%; border-image-source: none; border-image-width: 1; border-left-color: currentColor; border-left-style: none; border-left-width: 0px; border-right-color: currentColor; border-right-style: none; border-right-width: 0px; border-top-color: currentColor; border-top-style: none; border-top-width: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\">\n   <th scope=\"col\" style=\"background: none 0% 0% / auto repeat scroll padding-box border-box rgba(212, 221, 228, 0.5); border-color: rgb(255, 255, 255) rgb(255, 255, 255) rgb(212, 221, 228); border-style: solid; border-width: 2px; border-image: none 100% / 1 / 0 stretch; font-style: inherit; margin: 0px; padding: 2px 8px 4px; text-align: left;\">Specification</th>\n   <th scope=\"col\" style=\"background: none 0% 0% / auto repeat scroll padding-box border-box rgba(212, 221, 228, 0.5); border-color: rgb(255, 255, 255) rgb(255, 255, 255) rgb(212, 221, 228); border-style: solid; border-width: 2px; border-image: none 100% / 1 / 0 stretch; font-style: inherit; margin: 0px; padding: 2px 8px 4px; text-align: left;\">Status</th>\n   <th scope=\"col\" style=\"background: none 0% 0% / auto repeat scroll padding-box border-box rgba(212, 221, 228, 0.5); border-color: rgb(255, 255, 255) rgb(255, 255, 255) rgb(212, 221, 228); border-style: solid; border-width: 2px; border-image: none 100% / 1 / 0 stretch; font-style: inherit; margin: 0px; padding: 2px 8px 4px; text-align: left;\">Comment</th>\n  </tr>\n </thead>\n <tbody style=\"border-bottom-color: currentColor; border-bottom-style: none; border-bottom-width: 0px; border-image-outset: 0; border-image-repeat: stretch; border-image-slice: 100%; border-image-source: none; border-image-width: 1; border-left-color: currentColor; border-left-style: none; border-left-width: 0px; border-right-color: currentColor; border-right-style: none; border-right-width: 0px; border-top-color: currentColor; border-top-style: none; border-top-width: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\">\n  <tr style=\"border-bottom-color: currentColor; border-bottom-style: none; border-bottom-width: 0px; border-image-outset: 0; border-image-repeat: stretch; border-image-slice: 100%; border-image-source: none; border-image-width: 1; border-left-color: currentColor; border-left-style: none; border-left-width: 0px; border-right-color: currentColor; border-right-style: none; border-right-width: 0px; border-top-color: currentColor; border-top-style: none; border-top-width: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\">\n   <td style=\"background-color: rgba(212, 221, 228, 0.25); border: 2px solid rgb(255, 255, 255); box-shadow: rgba(212, 221, 228, 0.5) 0px -1px 0px 0px inset; margin: 0px; padding: 6px 8px;\"><a class=\"external\" lang=\"en\" href=\"https://html.spec.whatwg.org/multipage/editing.html#attr-contenteditable\" rel=\"noopener\" hreflang=\"en\" style=\"border: 0px none currentcolor; color: rgb(61, 126, 154); margin: 0px; padding: 0px; text-decoration-line: none;\">HTML Living Standard<br><small lang=\"en-US\" style=\"border-bottom-color: currentColor; border-bottom-style: none; border-bottom-width: 0px; border-image-outset: 0; border-image-repeat: stretch; border-image-slice: 100%; border-image-source: none; border-image-width: 1; border-left-color: currentColor; border-left-style: none; border-left-width: 0px; border-right-color: currentColor; border-right-style: none; border-right-width: 0px; border-top-color: currentColor; border-top-style: none; border-top-width: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\">The definition of \'contenteditable\' in that specification.</small></a></td>\n   <td style=\"background-color: rgba(212, 221, 228, 0.25); border: 2px solid rgb(255, 255, 255); box-shadow: rgba(212, 221, 228, 0.5) 0px -1px 0px 0px inset; margin: 0px; padding: 6px 8px;\"><span class=\"spec-Living\" style=\"background-color: rgb(238, 238, 238); border-color: currentcolor; border-radius: 2px; border-style: none none none solid; border-width: 0px 0px 0px 4px; border-image: none 100% / 1 / 0 stretch; color: rgb(0, 83, 159); display: inline-block; font-family: Helvetica, arial, sans-serif; font-size: 14px; line-height: normal; margin: 0px 0px 0px 10px; min-width: 20px; padding: 0.45em 0.35em; vertical-align: baseline;\">Living Standard</span></td>\n   <td style=\"background-color: rgba(212, 221, 228, 0.25); border: 2px solid rgb(255, 255, 255); box-shadow: rgba(212, 221, 228, 0.5) 0px -1px 0px 0px inset; margin: 0px; padding: 6px 8px;\">No change from latest snapshot, <a title=\"The \'HTML 5.2\' specification\" class=\"external\" lang=\"en\" href=\"https://www.w3.org/TR/html52/\" rel=\"noopener\" hreflang=\"en\" style=\"border: 0px none currentcolor; color: rgb(61, 126, 154); margin: 0px; padding: 0px; text-decoration-line: none;\">HTML 5.2</a></td>\n  </tr>\n  <tr style=\"border-bottom-color: currentColor; border-bottom-style: none; border-bottom-width: 0px; border-image-outset: 0; border-image-repeat: stretch; border-image-slice: 100%; border-image-source: none; border-image-width: 1; border-left-color: currentColor; border-left-style: none; border-left-width: 0px; border-right-color: currentColor; border-right-style: none; border-right-width: 0px; border-top-color: currentColor; border-top-style: none; border-top-width: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\">\n   <td style=\"background-color: rgba(212, 221, 228, 0.15); border: 2px solid rgb(255, 255, 255); box-shadow: rgba(212, 221, 228, 0.5) 0px -1px 0px 0px inset; margin: 0px; padding: 6px 8px;\"><a class=\"external\" lang=\"en\" href=\"https://www.w3.org/TR/html52/editing.html#making-document-regions-editable-the-contenteditable-content-attribute\" rel=\"noopener\" hreflang=\"en\" style=\"border: 0px none currentcolor; color: rgb(61, 126, 154); margin: 0px; padding: 0px; text-decoration-line: none;\">HTML 5.2<br><small lang=\"en-US\" style=\"border-bottom-color: currentColor; border-bottom-style: none; border-bottom-width: 0px; border-image-outset: 0; border-image-repeat: stretch; border-image-slice: 100%; border-image-source: none; border-image-width: 1; border-left-color: currentColor; border-left-style: none; border-left-width: 0px; border-right-color: currentColor; border-right-style: none; border-right-width: 0px; border-top-color: currentColor; border-top-style: none; border-top-width: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\">The definition of \'contenteditable\' in that specification.</small></a></td>\n   <td style=\"background-color: rgba(212, 221, 228, 0.15); border: 2px solid rgb(255, 255, 255); box-shadow: rgba(212, 221, 228, 0.5) 0px -1px 0px 0px inset; margin: 0px; padding: 6px 8px;\"><span class=\"spec-REC\" style=\"background-color: rgb(238, 238, 238); border-color: currentcolor; border-radius: 2px; border-style: none none none solid; border-width: 0px 0px 0px 4px; border-image: none 100% / 1 / 0 stretch; color: rgb(48, 156, 64); display: inline-block; font-family: Helvetica, arial, sans-serif; font-size: 14px; line-height: normal; margin: 0px 0px 0px 10px; min-width: 20px; padding: 0.45em 0.35em; vertical-align: baseline;\">Recommendation</span></td>\n   <td style=\"background-color: rgba(212, 221, 228, 0.15); border: 2px solid rgb(255, 255, 255); box-shadow: rgba(212, 221, 228, 0.5) 0px -1px 0px 0px inset; margin: 0px; padding: 6px 8px;\">Snapshot of <a title=\"The \'HTML Living Standard\' specification\" class=\"external\" lang=\"en\" href=\"https://html.spec.whatwg.org/multipage/\" rel=\"noopener\" hreflang=\"en\" style=\"border: 0px none currentcolor; color: rgb(61, 126, 154); margin: 0px; padding: 0px; text-decoration-line: none;\">HTML Living Standard</a>, no change from <a title=\"The \'HTML 5.1\' specification\" class=\"external\" lang=\"en\" href=\"https://www.w3.org/TR/html51/\" rel=\"noopener\" hreflang=\"en\" style=\"border: 0px none currentcolor; color: rgb(61, 126, 154); margin: 0px; padding: 0px; text-decoration-line: none;\">HTML 5.1</a></td>\n  </tr>\n  <tr style=\"border-bottom-color: currentColor; border-bottom-style: none; border-bottom-width: 0px; border-image-outset: 0; border-image-repeat: stretch; border-image-slice: 100%; border-image-source: none; border-image-width: 1; border-left-color: currentColor; border-left-style: none; border-left-width: 0px; border-right-color: currentColor; border-right-style: none; border-right-width: 0px; border-top-color: currentColor; border-top-style: none; border-top-width: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\">\n   <td style=\"background-color: rgba(212, 221, 228, 0.25); border: 2px solid rgb(255, 255, 255); box-shadow: rgba(212, 221, 228, 0.5) 0px -1px 0px 0px inset; margin: 0px; padding: 6px 8px;\"><a class=\"external\" lang=\"en\" href=\"https://www.w3.org/TR/html51/editing.html#making-document-regions-editable-the-contenteditable-content-attribute\" rel=\"noopener\" hreflang=\"en\" style=\"border: 0px none currentcolor; color: rgb(61, 126, 154); margin: 0px; padding: 0px; text-decoration-line: none;\">HTML 5.1<br><small lang=\"en-US\" style=\"border-bottom-color: currentColor; border-bottom-style: none; border-bottom-width: 0px; border-image-outset: 0; border-image-repeat: stretch; border-image-slice: 100%; border-image-source: none; border-image-width: 1; border-left-color: currentColor; border-left-style: none; border-left-width: 0px; border-right-color: currentColor; border-right-style: none; border-right-width: 0px; border-top-color: currentColor; border-top-style: none; border-top-width: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\">The definition of \'contenteditable\' in that specification.</small></a></td>\n   <td style=\"background-color: rgba(212, 221, 228, 0.25); border: 2px solid rgb(255, 255, 255); box-shadow: rgba(212, 221, 228, 0.5) 0px -1px 0px 0px inset; margin: 0px; padding: 6px 8px;\"><span class=\"spec-REC\" style=\"background-color: rgb(238, 238, 238); border-color: currentcolor; border-radius: 2px; border-style: none none none solid; border-width: 0px 0px 0px 4px; border-image: none 100% / 1 / 0 stretch; color: rgb(48, 156, 64); display: inline-block; font-family: Helvetica, arial, sans-serif; font-size: 14px; line-height: normal; margin: 0px 0px 0px 10px; min-width: 20px; padding: 0.45em 0.35em; vertical-align: baseline;\">Recommendation</span></td>\n   <td style=\"background-color: rgba(212, 221, 228, 0.25); border: 2px solid rgb(255, 255, 255); box-shadow: rgba(212, 221, 228, 0.5) 0px -1px 0px 0px inset; margin: 0px; padding: 6px 8px;\">Snapshot of <a title=\"The \'HTML Living Standard\' specification\" class=\"external\" lang=\"en\" href=\"https://html.spec.whatwg.org/multipage/\" rel=\"noopener\" hreflang=\"en\" style=\"border: 0px none currentcolor; color: rgb(61, 126, 154); margin: 0px; padding: 0px; text-decoration-line: none;\">HTML Living Standard</a>, no change from <a title=\"The \'HTML5\' specification\" class=\"external\" lang=\"en\" href=\"https://www.w3.org/TR/html52/\" rel=\"noopener\" hreflang=\"en\" style=\"border: 0px none currentcolor; color: rgb(61, 126, 154); margin: 0px; padding: 0px; text-decoration-line: none;\">HTML5</a></td>\n  </tr>\n  <tr style=\"border-bottom-color: currentColor; border-bottom-style: none; border-bottom-width: 0px; border-image-outset: 0; border-image-repeat: stretch; border-image-slice: 100%; border-image-source: none; border-image-width: 1; border-left-color: currentColor; border-left-style: none; border-left-width: 0px; border-right-color: currentColor; border-right-style: none; border-right-width: 0px; border-top-color: currentColor; border-top-style: none; border-top-width: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\">\n   <td style=\"background-color: rgba(212, 221, 228, 0.15); border: 2px solid rgb(255, 255, 255); box-shadow: rgba(212, 221, 228, 0.5) 0px -1px 0px 0px inset; margin: 0px; padding: 6px 8px;\"><a class=\"external\" lang=\"en\" href=\"https://www.w3.org/TR/html52/editing.html#attr-contenteditable\" rel=\"noopener\" hreflang=\"en\" style=\"border: 0px none currentcolor; color: rgb(61, 126, 154); margin: 0px; padding: 0px; text-decoration-line: none;\">HTML5<br><small lang=\"en-US\" style=\"border-bottom-color: currentColor; border-bottom-style: none; border-bottom-width: 0px; border-image-outset: 0; border-image-repeat: stretch; border-image-slice: 100%; border-image-source: none; border-image-width: 1; border-left-color: currentColor; border-left-style: none; border-left-width: 0px; border-right-color: currentColor; border-right-style: none; border-right-width: 0px; border-top-color: currentColor; border-top-style: none; border-top-width: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\">The definition of \'contenteditable\' in that specification.</small></a></td>\n   <td style=\"background-color: rgba(212, 221, 228, 0.15); border: 2px solid rgb(255, 255, 255); box-shadow: rgba(212, 221, 228, 0.5) 0px -1px 0px 0px inset; margin: 0px; padding: 6px 8px;\"><span class=\"spec-REC\" style=\"background-color: rgb(238, 238, 238); border-color: currentcolor; border-radius: 2px; border-style: none none none solid; border-width: 0px 0px 0px 4px; border-image: none 100% / 1 / 0 stretch; color: rgb(48, 156, 64); display: inline-block; font-family: Helvetica, arial, sans-serif; font-size: 14px; line-height: normal; margin: 0px 0px 0px 10px; min-width: 20px; padding: 0.45em 0.35em; vertical-align: baseline;\">Recommendation</span></td>\n   <td style=\"background-color: rgba(212, 221, 228, 0.15); border: 2px solid rgb(255, 255, 255); box-shadow: rgba(212, 221, 228, 0.5) 0px -1px 0px 0px inset; margin: 0px; padding: 6px 8px;\">Snapshot of <a title=\"The \'HTML Living Standard\' specification\" class=\"external\" lang=\"en\" href=\"https://html.spec.whatwg.org/multipage/\" rel=\"noopener\" hreflang=\"en\" style=\"border: 0px none currentcolor; color: rgb(61, 126, 154); margin: 0px; padding: 0px; text-decoration-line: none;\">HTML Living Standard</a>, initial definition.</td></tr></tbody></table></h4>');
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
INSERT INTO `user_details` VALUES (0,'hansin','$2a$10$1QW45Y2n8HB2B57Rqh.TC.qL5YbjOUixejBQKSSUeEr0HYs8lX.Wy','',NULL,NULL,00,0,1,0,0,0,'2020-05-16 17:26:58','2020-06-19 19:13:24'),(7962943190178328576,'hansin1997','$2a$10$kexcic1HeA3.ZuQcqkJOEOzpdItnTRy4H9nL7HeN0PA17XzNWTnJW','hansin@dustlight.cn','',NULL,00,4,1,0,0,0,'2020-06-21 07:16:55','2020-06-21 07:16:55'),(7962953799328985088,'lbgzs2010','$2a$10$bhqVV6UpPlutKXPm4pidzOtcK41vu.Lgr81XTqaAqFvFTHX0aOVQO','lbgzs2010@live.cn','',NULL,00,4,1,0,0,0,'2020-06-21 07:59:04','2020-06-21 07:59:04');
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

-- Dump completed on 2020-06-22  3:06:47
