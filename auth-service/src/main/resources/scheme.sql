/**
 * 表结构
 * version: 0
 */

/* 用户表 Users */
CREATE TABLE IF NOT EXISTS`users` (
  `uid` bigint NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(128) NOT NULL,
  `phone` VARCHAR(20) NULL,
  `email` varchar(320) NULL,
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
  UNIQUE KEY `phone_UNIQUE` (`phone`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  FULLTEXT KEY `user_keywords` (`username`,`phone`,`email`,`nickname`) WITH PARSER ngram
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/* 角色表 Roles */
CREATE TABLE IF NOT EXISTS `roles` (
  `rid` bigint NOT NULL,
  `roleName` varchar(64) NOT NULL,
  `roleDescription` varchar(128) DEFAULT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rid`),
  UNIQUE KEY `roleName_UNIQUE` (`roleName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* 授权类型表 GrantTypes */
CREATE TABLE IF NOT EXISTS`types` (
  `tid` bigint NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tid`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/* 权限表 Authorities */
CREATE TABLE IF NOT EXISTS`authorities` (
  `aid` bigint NOT NULL,
  `authorityName` varchar(64) NOT NULL,
  `authorityDescription` varchar(128) DEFAULT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`aid`),
  UNIQUE KEY `authorityName_UNIQUE` (`authorityName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* 授权作用域表 Scopes */
CREATE TABLE IF NOT EXISTS`scopes` (
  `sid` bigint NOT NULL,
  `name` varchar(45) NOT NULL,
  `subtitle` varchar(64) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/* 应用表 Clients */
CREATE TABLE IF NOT EXISTS`clients` (
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
  FULLTEXT KEY `client_keywords` (`name`,`description`,`redirectUri`,`cid`) WITH PARSER ngram,
  CONSTRAINT `client_owner` FOREIGN KEY (`uid`) REFERENCES `users` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/* 资源表 Resources */
CREATE TABLE IF NOT EXISTS`resources` (
  `rid` varchar(64) NOT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rid`),
  CONSTRAINT `resources_rid` FOREIGN KEY (`rid`) REFERENCES `clients` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/* 应用权限表 ClientAuthorities */
CREATE TABLE IF NOT EXISTS`client_authority` (
  `cid` varchar(64) NOT NULL,
  `aid` bigint NOT NULL,
  PRIMARY KEY (`cid`,`aid`),
  KEY `client_authority_aid_idx` (`aid`),
  CONSTRAINT `client_authority_aid` FOREIGN KEY (`aid`) REFERENCES `authorities` (`aid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `client_authority_cid` FOREIGN KEY (`cid`) REFERENCES `clients` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/* 应用资源表 ClientResources */
CREATE TABLE IF NOT EXISTS`client_resource` (
  `cid` varchar(64) NOT NULL,
  `rid` varchar(64) NOT NULL,
  PRIMARY KEY (`cid`,`rid`),
  KEY `client_resource_rid_idx` (`rid`),
  CONSTRAINT `client_resource_cid` FOREIGN KEY (`cid`) REFERENCES `clients` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `client_resource_rid` FOREIGN KEY (`rid`) REFERENCES `resources` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/* 应用授权作用域表 ClientScopes */
CREATE TABLE IF NOT EXISTS`client_scope` (
  `cid` varchar(64) NOT NULL,
  `sid` bigint NOT NULL,
  `autoApprove` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`cid`,`sid`),
  KEY `client_scope_sid_idx` (`sid`) /*!80000 INVISIBLE */,
  KEY `client_scope_cid_idx` (`cid`) /*!80000 INVISIBLE */,
  CONSTRAINT `client_scope_cid` FOREIGN KEY (`cid`) REFERENCES `clients` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `client_scope_sid` FOREIGN KEY (`sid`) REFERENCES `scopes` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/* 应用授权类型表 ClientGrantTypes */
CREATE TABLE IF NOT EXISTS`client_type` (
  `cid` varchar(64) NOT NULL,
  `tid` bigint NOT NULL,
  PRIMARY KEY (`cid`,`tid`),
  KEY `client_type_tid_idx` (`tid`),
  CONSTRAINT `client_type_cid` FOREIGN KEY (`cid`) REFERENCES `clients` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `client_type_tid` FOREIGN KEY (`tid`) REFERENCES `types` (`tid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/* 角色权限表 RoleAuthorities */
CREATE TABLE IF NOT EXISTS`role_authority` (
  `rid` bigint NOT NULL,
  `aid` bigint NOT NULL,
  PRIMARY KEY (`rid`,`aid`),
  KEY `aid_idx` (`aid`),
  KEY `rid_idx` (`rid`),
  CONSTRAINT `role_authority_aid` FOREIGN KEY (`aid`) REFERENCES `authorities` (`aid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `role_authority_rid` FOREIGN KEY (`rid`) REFERENCES `roles` (`rid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=ascii;

/* 用户角色表 UserRoles */
CREATE TABLE IF NOT EXISTS`user_role` (
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
