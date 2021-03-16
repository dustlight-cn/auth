# Auth
[![Docker Build Status](https://img.shields.io/github/workflow/status/dustlight-cn/auth/Docker%20Image%20CI)](https://github.com/dustlight-cn/auth/actions/)
[![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/dustlight-cn/auth?include_prereleases)](https://github.com/dustlight-cn/auth/releases)
[![Docker Image Size (latest semver)](https://img.shields.io/docker/image-size/dustlightcn/auth-service?logo=docker)](https://hub.docker.com/repository/docker/dustlightcn/auth-service)
[![Docker Image Version (latest semver)](https://img.shields.io/docker/v/dustlightcn/auth-service?label=image%20version&logo=docker)](https://hub.docker.com/repository/docker/dustlightcn/auth-service)
[![License](https://img.shields.io/github/license/dustlight-cn/auth)](LICENSE)

[简介](#简介) | [展示](#展示) | [部署](#部署) | [构建](#构建) | [鸣谢](#鸣谢)

## 简介

**Auth** 是一个基于 **Spring Boot** 的 **OAuth2.0 用户中心**。 提供 OAuth2.0 授权与管理服务，以及包括用户、应用、角色与权限的管理。 提供 Restful 接口以及 OpenAPI 文档。
提供基于 **Vue** & **Quasar** 的前端页面。 支持微服务部署。

> [在线展示](https://accounts.dustlight.cn/)
> 
> [获取测试账号](#前端展示) （拥有创建应用权限，查看所有用户信息以及查看所有应用信息权限。）
>
> [隐式授权示例 *(implicit)*](https://accounts.dustlight.cn/authorize?client_id=test&response_type=token)

### 支持的授权模式

* 授权码模式 *authorization_code* ✔
* 客户端凭据模式 *client_credentials* ✔
* 隐式授权模式 *implicit* ✔
* 令牌刷新 *refresh_token* ✔
* 密码模式 *password* （默认不启用，如需启用可以创建授权模式。）

### 后端

* 通过重写 **Spring Security OAuth2** 中的 Endpoint 实现前后端分离架构，使用 **Redis** 存储 Token、授权码、验证码等。
* 基于 **MySQL** & **MyBatis** 实现包括用户管理、应用管理、权限与角色管理等业务。
* 文件储存通过第三方服务实现（同时兼容本地储存），登录注册等接口通过谷歌 reCAPTCHA 人机识别进行验证。
* 使用 **Spring Doc** & **Swagger-UI** 生成 **OpenAPI** 文档和 API 调试页面。

#### OAuth2 端点

| 功能 | URL | 方法 |
| --- | --- | --- |
| 颁发 OAuth 令牌 | [/v1/oauth/token](https://api.dustlight.cn/v1/oauth/token) | POST |
| 销毁令牌 | [/v1/token](https://api.dustlight.cn/v1/token) | DELETE |
| 检查令牌 | [/v1/token/validity](https://api.dustlight.cn/v1/token/validity) | GET / POST |
| 授权 | - | - |

> 由于后端服务只提供 Restful 接口，授权页面由前端提供。


### 前端

* 基于 **Vue** & **Quasar** 框架。
* 使用 **OpenAPI Generator** 生成 Typescript Axios SDK。
* 国际化（中英文） & 自适应。

#### OAuth2 页面

| 功能 | URL |  |
| --- | --- | --- |
| 授权 | [/authorize](https://accounts.dustlight.cn/authorize) | [隐式授权示例 *(implicit)*](https://accounts.dustlight.cn/authorize?client_id=test&response_type=token) |


### 关键词

* OAuth2.0 授权中心
* 无状态（Stateless）
* 角色权限（RBAC）

## 展示

### 前端展示

包含登录注册、应用授权、个人信息管理、应用管理以及用户管理和系统设置。

[前往查看用户中心](https://accounts.dustlight.cn)

[隐式授权示例 *(implicit)*](https://accounts.dustlight.cn/authorize?client_id=test&response_type=token)

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

#### 配置文件
application.yaml（与运行目录同级）：

```yaml
mysql:
  host: "localhost"   # MySQL 服务器地址
  port: 3306          # MySQL 服务端口
  db: auth            # MySQL 数据库
  charset: "utf8"     # 字符编码
  ssl: false          # 连接启用 SSL
  username: "root"    # 数据库用户名
  password: "123456"  # 数据库密码
redis:
  host: "localhost"   # Redis 服务器地址
  port: 6379          # Redis 服务端口
  password: ""        # Redis 密码
smtp:
  host: "smtp.exmail.qq.com" # SMTP 服务器地址
  user: "YOUR_USERNAME"
  pass: "YOUR_PASSWORD"

dustlight:
  auth:
    storage:
      storage-type: local # 储存模式，可选本地储存或者云储存。（local | storage）
      prefix: "upload/auth/" # 储存地址前缀：本地储存模式时代表本地储存目录，云储存模式则代表存储对象 key 的前缀。
    #      base-url: "https://accounts.dustlight.cn/" # 当启用云储存时，自定义生成访问链接的域名以及协议。（通常用于 CDN 或者自定义域名）
    #      simple-mode: true # 当云储存启用时，是否忽略对象 URL 的 Query 部分，以便前端能够利用缓存减少请求次数提高访问速度。（仅适用于公有读储存桶）
    #      default-expiration: 900000 # 当云储存启用时，生成签名的有效期。（单位为毫秒，默认为15分钟）
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
      default-secret: "6Lcp1xAaAAAAAJh6jmR8oWjEiqwGgbBTS7BnDpbX" # 谷歌 reCAPTCHA 服务的密钥
      default-parameter-name: "g-recaptcha-response" # 请求参数名（更该此项必须配合前端更改）
    generator:
      random-string:
        chars: "0123456789" # 生成随机字符验证码的字符池
        length: 6 # 随机字符验证码的长度
    verifier:
      string-equals:
        trim: true
  # 对象存储配置，详情请参考：https://github.com/dustlight-cn/storage
  storage:
#    alibaba:
#      oss:
#        access-key-id: YOUR_ACCESS_KEY_ID
#        secret-access-key: YOUR_SECRET_ACCESS_KEY
#        bucket: YOUR_BUCKET_NAME
#        endpoint: YOUR_BUCKET_ENDPOINT
#    tencent:
#      cos:
#        secret-key: YOUR_SECRET_KEY
#        secret-id: YOUR_SECRET_ID
#        bucket: YOUR_BUCKET_NAME
#        region: YOUR_BUCKET_REGION
```


#### 部署后端服务
> 本地部署需要提前请安装 [Java 运行环境 (JRE)](https://www.oracle.com/java/technologies/javase-jre8-downloads.html) 。

运行服务：

```
java -jar auth-service-*.jar
```

#### 获取前端 UI
您可以从 [Release](https://github.com/dustlight-cn/auth/releases)
下载构建完成的压缩文件，也可以选择进行 [手动构建](#构建前端-ui)。

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

> 正式部署时需要更改后端接口的地址，可以在 ```js/app.*.js``` 中搜索 ```http://localhost:8080```，
> 并将其替换。若需要更改更多参数，如用户名正则、谷歌验证码 Key，建议选择手动构建。（修改前端项目的配置文件 ```src/config.ts```）

### Docker 部署

#### 拉取镜像

镜像： ```dustlightcn/auth-service``` [![Docker Image Version (latest semver)](https://img.shields.io/docker/v/dustlightcn/auth-service?label=version)](https://hub.docker.com/repository/docker/dustlightcn/auth-service)

拉取镜像：
```
docker pull dustlightcn/auth-service:版本号（如 1.0.5-alpha）
```
   
#### 启动容器

运行容器：
```
docker run -e mysql.host=MYSQL_HOST -p 8080:8080 --name auth-service dustlightcn/auth-service:版本号（如 1.0.5-alpha）
```

> 通过 Docker 容器进行部署时，可以通过参数来配置环境变量（例如：```-e key=value```）。环境变量可以作为应用配置被读取，如 ```-e mysql.host=MYSQ_HOST```。
> 
> 查看 [示例配置](#配置文件)

### Kubernetes 部署

Kubernetes 部署文档未完善。

## 构建

> 前端构建需要提前安装 [Node.js](https://nodejs.org/)  。
> 
> 后端构建需要提前安装 [Java 开发工具包 (JDK)](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html) 
> 以及 [Maven](https://maven.apache.org/) 。

如果您需要自己构建项目，可以参考以下步骤进行构建。

### 构建后端服务

执行 ```mvn package```

> 配置文件位于：[```auth-service/src/main/resources/application.yaml```](auth-service/src/main/resources/application.yaml)
>
> 生成的文件位于 ```auth-service/target```

### 构建前端 UI

进入目录 ```cd auth-ui```

#### 安装 NPM 依赖

执行 ```npm i```

#### 进行前端构建

执行 ```quasar build```

> 配置文件位于：[```auth-ui/src/config.ts```](auth-ui/src/config.ts)
> 
> 生成的文件位于 ```auth-ui/dist/spa```

### 构建 Docker 镜像

复制构建完成的二进制文件到根目录，将其重命名为 ```auth-service.jar```

执行 ```docker build -t auth-service:TAG .```

## 鸣谢

[![JetBrains](https://raw.githubusercontent.com/dustlight-cn/auth/master/docs/img/jetbrains.svg)](https://www.jetbrains.com/?from=Auth)
