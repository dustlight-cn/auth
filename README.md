# Auth

[简介](#简介) | [展示](#展示) | [部署](#部署) | [构建](#构建) | [鸣谢](鸣谢)

## 简介

**Auth** 是一个基于 **Spring Boot** 的 **OAuth2.0** 的授权与管理服务。 提供 OAuth2.0 授权服务，以及包括用户、应用、角色与权限的管理。 提供 Restful 接口以及 OpenAPI 文档。
提供前端页面。 支持微服务部署。

> [在线展示](#展示)

### 后端

* 通过重写 **Spring Security OAuth2** 中的 Endpoint 实现前后端分离架构，使用 **Redis** 存储 Token、授权码、验证码等。
* 基于 **MySQL** & **MyBatis** 实现包括用户管理、应用管理、权限与角色管理等业务。
* 文件储存通过第三方服务实现，登录注册等接口通过谷歌 reCAPTCHA 人机识别进行验证。
* 使用 **Spring Doc** & **Swagger-UI** 生成 **OpenAPI** 文档和 API 调试页面。

### 前端

* 基于 **Vue** & **Quasar** 框架。
* 使用 **OpenAPI Generator** 生成 Typescript Axios SDK。
* 国际化（中英文） & 自适应。

### 关键词

* OAuth2.0 授权中心
* 无状态（Stateless）
* 角色权限（RBAC）

## 展示

### 前端展示

包含登录注册、应用授权、个人信息管理、应用管理以及用户管理和系统设置。
[前往查看](https://accounts.dustlight.cn)

您可以使用下列的测试账号登录，它们拥有测试权限以及开发者权限。

| 用户名 | 密码 |
| --- | --- |
| test_user | 123456 |
| aaaaaa | 123456 |
| bbbbbb | 123456 |

> 1. 出于功能展示的目的，测试账号拥有查看所有用户信息的权限。如果您打算进行注册账号，您的电子邮箱地址可能会被其他人看到。
> 2. 若上述测试账号均不可用，请 [创建 Issue](https://github.com/dustlight-cn/auth/issues/new) 联系我们。

### 接口浏览

| Open API | URL |
| --- | --- |
| JSON | [https://api.dustlight.cn/v1/api-doc](https://api.dustlight.cn/v1/api-doc) | 
| YAML | [https://api.dustlight.cn/v1/api-doc.yaml](https://api.dustlight.cn/v1/api-doc.yaml) | 

> 后端接口浏览调试：
> [前往查看（Swagger UI）](https://api.dustlight.cn/v1/swagger-ui)

## 部署

### Docker 镜像

```dustlightcn/auth-service```
![Docker Image Size (latest semver)](https://img.shields.io/docker/image-size/dustlightcn/auth-service?logo=docker)
![Docker Image Version (latest semver)](https://img.shields.io/docker/v/dustlightcn/auth-service?label=version)

### Kubernetes配置

待完善

### ...

## 构建

如果您需要自己构建项目，可以参考以下步骤进行构建。

### 构建后端服务

执行 ```mvn package```

### 构建前端 UI

进入目录 ```cd auth-ui```

执行 ```quasar build```

### 构建 Docker 镜像

复制构建完成的二进制文件到根目录，将其重命名为 ```auth-service.jar```

执行 ```docker build -t auth-service:0.0.1 .```

## 鸣谢

[![JetBrains](https://github.com/dustlight-cn/auth/blob/master/auth-ui/docs/img/jetbrains.png)](https://www.jetbrains.com/?from=Auth)
