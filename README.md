# Auth
[![Docker Build Status](https://img.shields.io/github/workflow/status/dustlight-cn/auth/Docker%20Image%20CI)](https://github.com/dustlight-cn/auth/actions/)
[![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/dustlight-cn/auth?include_prereleases)](https://github.com/dustlight-cn/auth/releases)
[![Docker Image Size (latest semver)](https://img.shields.io/docker/image-size/dustlightcn/auth-service?logo=docker)](https://hub.docker.com/repository/docker/dustlightcn/auth-service)
[![Docker Image Version (latest semver)](https://img.shields.io/docker/v/dustlightcn/auth-service?label=image%20version&logo=docker)](https://hub.docker.com/repository/docker/dustlightcn/auth-service)

[简介](#简介) | [展示](#展示) | [部署](#部署) | [构建](#构建) | [鸣谢](#鸣谢)

## 简介

**Auth** 是一个基于 **Spring Boot** 的 **OAuth2.0 用户中心**。 提供 OAuth2.0 授权与管理服务，以及包括用户、应用、角色与权限的管理。 提供 Restful 接口以及 OpenAPI 文档。
提供基于 **Vue** & **Quasar** 的前端页面。 支持微服务部署。

> [在线展示](https://accounts.dustlight.cn/)
> 
> [获取测试账号](#前端展示) （拥有创建应用权限，查看所有用户信息以及查看所有应用信息权限。）

### 后端

* 通过重写 **Spring Security OAuth2** 中的 Endpoint 实现前后端分离架构，使用 **Redis** 存储 Token、授权码、验证码等。
* 基于 **MySQL** & **MyBatis** 实现包括用户管理、应用管理、权限与角色管理等业务。
* 文件储存通过第三方服务实现（同时支持本地储存），登录注册等接口通过谷歌 reCAPTCHA 人机识别进行验证。
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
> 2. 若上述测试账号均不可用，请 [创建 Issue](https://github.com/dustlight-cn/auth/issues/new) 联系我。

### 接口浏览

| Open API | URL |
| --- | --- |
| JSON | [https://api.dustlight.cn/v1/api-doc](https://api.dustlight.cn/v1/api-doc) | 
| YAML | [https://api.dustlight.cn/v1/api-doc.yaml](https://api.dustlight.cn/v1/api-doc.yaml) | 

> 后端接口浏览调试：
> [前往查看（Swagger UI）](https://api.dustlight.cn/v1/swagger-ui)

## 部署

### 本地部署

#### 获取可执行 Jar
您可以从 [Release](https://github.com/dustlight-cn/auth/releases) 
下载编译完成的可执行 Jar 包，
也可以选择进行 [手动构建](#构建后端服务)。

#### 部署后端服务
> 运行之前请安装 [JRE](https://www.oracle.com/java/technologies/javase-jre8-downloads.html)

配置文件：application.yaml

```yaml
mysql:
  host: "instance-2"  # MySQL 服务器地址
  port: 30010         # MySQL 服务端口
  db: auth            # MySQL 数据库
  charset: "utf8"     # 字符编码
  ssl: false          # 连接启用 SSL
  username: "root"    # 数据库用户名
  password: "123456"  # 数据库密码
redis:
  host: "instance-2"  # Redis 服务器地址
  port: 30020         # Redis 服务端口
  password: ""        # Redis 密码

dustlight:
  auth:
    storage:
      storage-type: local # 储存模式，可选本地储存或者云储存。（local | storage）
      prefix: "upload/auth/" # 储存地址前缀：本地储存模式为文件名的前缀，云储存模式则为存储对象 key 的前缀。
      base-url: "./" # 使用本地储存时为文件储存目录，如 './upload/'。当启用云储存时，自定义域名以及协议，如 'https://accounts.dustlight.cn/'。（通常用于 CDN）
      simple-mode: true # 当云储存启用时，是否省略生成的签名。（可以避免 CDN 缓存失效）
      default-expiration: 900000 # 当云储存启用时，生成签名的有效期。（单位为毫秒，默认为15分钟）
    service:
      pattern:
        username: "^[a-zA-Z]([-_a-zA-Z0-9]{5,19})$" # 用户名正则限制
        email: "^\\S+@\\S+$"  # 邮箱地址正则限制
        password: "^.{6,20}$" # 密码正则限制
  # 验证码配置，详情请参考：https://github.com/dustlight-cn/captcha
  captcha:
    default:
      store:
        name: redisCodeStore
    recaptcha:
      default-secret: "6Lcp1xAaAAAAAJh6jmR8oWjEiqwGgbBTS7BnDpbX"
      default-parameter-name: "g-recaptcha-response"
    generator:
      random-string:
        chars: "0123456789"
        length: 6
    verifier:
      string-equals:
        trim: true
  # 对象存储配置，详情请参考：https://github.com/dustlight-cn/storage
  storage:
    tencent:
      cos:
        enabled: false
```

运行服务：

```
java -jar auth-service-X.X.X.jar
```

#### 获取前端 UI
您可以从 [Release](https://github.com/dustlight-cn/auth/releases)
下载构建完成的压缩文件，也可以选择进行 [手动构建](#构建前端%20UI)。

#### 部署前端 UI

解压下载的压缩文件或者构建完成的 ```dist/spa``` 目录下的文件到您的 Web 目录，并为其进行 SPA 配置。

下面为 Nginx 的示例：

```
server {
    listen 80 http2;
    server_name quasar.myapp.com;

    root /home/user/quasar.myapp.com/public;

    add_header X-Frame-Options "SAMEORIGIN";
    add_header X-XSS-Protection "1; mode=block";
    add_header X-Content-Type-Options "nosniff";

    index index.html;

    charset utf-8;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location = /favicon.ico { access_log off; log_not_found off; }
    location = /robots.txt  { access_log off; log_not_found off; }

    access_log off;
    error_log  /var/log/nginx/quasar.myapp.com-error.log error;

    location ~ /\.(?!well-known).* {
        deny all;
    }
}
```

其他 Web 服务器配置请 [参考此处](http://www.quasarchs.com/quasar-cli/developing-spa/deploying/) ，或者搜索对应的关键字 ”单页面应用 部署“。

### Docker 部署

#### 拉取镜像

最新版本：
[![Docker Image Version (latest semver)](https://img.shields.io/docker/v/dustlightcn/auth-service?label=version)](https://hub.docker.com/repository/docker/dustlightcn/auth-service)

拉取镜像：
```docker push dustlightcn/auth-service:版本号```

#### 

### Kubernetes 部署

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

[![JetBrains](https://raw.githubusercontent.com/dustlight-cn/auth/master/docs/img/jetbrains.svg)](https://www.jetbrains.com/?from=Auth)
