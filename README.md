# Dustlight 光尘

基于SpringBoot的OAuth2.0脚手架工程。

示例站点：
- [account.dustlight.cn](https://account.dustlight.cn) OAuth2用户中心及开放平台

## 目录
- [root](.) 根目录
    - [backend](backend) 后端项目
        - [uim](backend/uim) 统一身份认证服务
        - [client-template](backend/client-template) OAuth2客户端模板
    - [common](common) 公共模块
        - [sender](common/sender) 消息发送器模块
            - [sender-core](common/sender/sender-core) 消息发送器核心模块
            - [email-core](common/sender/email-core) 邮件发送器核心模块
            - [email-simple](common/sender/email-simple) 简单邮件发送器
        - [storage](common/storage) 存储模块
            - [storage-core](common/storage/storage-core) 存储核心模块
            - [TencentCloudObjectStorage](common/storage/TencentCloudObjectStorage) 腾讯云对象存储模块
    - [frontend](frontend) 前端项目
        - [uim-frontend](frontend/uim-frontend) 统一身份认证服务前端项目
