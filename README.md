# Dustlight 光尘

基于 SpringBoot & Spring Security 的 OAuth2脚手架项目。

功能包含：用户管理、角色管理、权限管理、OAuth应用管理等。

## 关键词
- SpringBoot, SpringSecurity
- MyBatis, MySQL, Redis
- Docker, MicroService, 微服务
- SMTP, Email, 验证码
- 腾讯云对象存储

## 示例站点
- [https://api.dustlight.cn/u/doc/](https://api.dustlight.cn/u/doc/) 接口文档
- [https://account.dustlight.cn](https://account.dustlight.cn) 用户管理与OAuth2授权管理平台

## 快速部署
### 准备工作
在进行服务部署之前，请检查是否满足以下条件：
* Docker            *服务部署需要在Docker上进行部署。*
* MySQL             *服务使用MySQL数据库进行数据持久化。[数据库初始化SQL](backend/uim/others/database.sql)*
* Redis             *服务使用Redis来实现分布式Session以及Token、验证码的存储。*
* STMP账号            *服务在某些情况可能需要发送电子邮件，例如用户注册时。*
* 腾讯云对象储存(COS)  *服务使用腾讯云COS保存用户头像和应用Logo等静态文件。*

### 获取镜像
```bash
docker pull ccr.ccs.tencentyun.com/dustlight/uim:latest
```

### 创建容器
```bash
docker create --name uim -p 8080:8080 -e MYSQL_HOST={数据库地址} -e MYSQL_PORT=3306 ... ccr.ccs.tencentyun.com/dustlight/uim:latest
```

**环境变量列表：**

| 变量名 | 描述 | 默认值|
| :----: | :----: | :----: |
| MYSQL_HOST | 数据库服务器地址 | mysql |
| MYSQL_PORT | 数据库端口 | 3306 |
| MYSQL_DB | 数据库名 | uim |
| MYSQL_CHARSET | 数据库字符集 | utf8 |
| MYSQL_SSL | 数据库使用SSL | false |
| MYSQL_USER | 数据库用户 | - |
| MYSQL_PASS | 数据库密码 | - |
| REDIS_HOST | Redis服务器地址 | redis |
| REDIS_PORT | Redis端口 | 6379 |
| SMTP_HOST | SMTP服务器地址 | smtp.exmail.qq.com |
| SMTP_USER | SMTP账号 | - |
| SMTP_PASS | SMTP端口 | - |
| TENCENT_COS_SECRET_ID | 腾讯云COS SecretId | - |
| TENCENT_COS_SECRET_KEY | 腾讯云COS SecretKey | - |
| TENCENT_COS_BUCKET | 腾讯云COS 储存桶名称 | - |
| TENCENT_COS_REGION | 腾讯云COS 地区 | - |
| STORAGE_BASE_URL | 静态资源基础URL，若为空默认使用腾讯云URL | - |
### 启动容器
```bash
docker start uim
```

### 查看日志
```bash
docker logs 
```

### 查看API
```http
http://{YOUR_HOST}:8080/u/doc/
```
将"{YOUR_HOST}"替换成docker宿主机地址后，使用浏览器打开地址即可查看API。
## 目录
- [root](.) 根目录
    - [backend](backend) 后端项目
        - [uim](backend/uim) 统一身份认证服务
        - [client-template](backend/client-template) OAuth2客户端模板
        - [uim-client](backend/uim-client) OAuth2客户端模块
    - [common](common) 公共模块
        - [generator](common/generator) 生成器模块
        - [sender](common/sender) 消息发送器
            - [sender-core](common/sender/sender-core) 消息发送器核心模块
            - [email-core](common/sender/email-core) 邮件发送器核心模块
            - [email-simple](common/sender/email-simple) 简单邮件发送器
        - [storage](common/storage) 存储
            - [storage-core](common/storage/storage-core) 存储核心模块
            - [TencentCloudObjectStorage](common/storage/TencentCloudObjectStorage) 腾讯云对象存储模块
        - [validator](common/validator) 验证业务
            - [validator-core](common/validator/validator-core) 验证业务核心模块
    - [frontend](frontend) 前端项目
        - [uim-frontend](frontend/uim-frontend) 统一身份认证服务前端项目
        - [uim-client-frontend](frontend/uim-client-frontend) OAuth2客户端前端示例项目
