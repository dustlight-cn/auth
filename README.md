# Auth

[简介](#简介) | [示例](#示例) | [构建](#构建) | [部署](#部署)

## 简介
**Auth** 是一个基于 **Spring Boot** 的 **OAuth2.0** 的授权与管理服务。

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

## 示例
* [UI (前端)](https://accounts.dustlight.cn)
* [API浏览 (Swagger UI)](https://api.dustlight.cn/v0/swagger-ui)

## 构建
待完善

## 部署
### 镜像 
```dustlightcn/auth-service```

![Docker Image Size (latest semver)](https://img.shields.io/docker/image-size/dustlightcn/auth-service?logo=docker)
![Docker Image Version (latest semver)](https://img.shields.io/docker/v/dustlightcn/auth-service?label=version)

### Kubernetes配置
待完善
### ...

