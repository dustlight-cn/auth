# 服务配置
通过配置文件修改数据库地址、SMTP 服务或者其他选项。

[查看配置文件模板](#配置模板)

[查看完整配置文件](../auth-service/src/main/resources/application.yaml)

## 配置方式
可以通过多种方式进行配置，例如：
* [环境变量](#环境变量)
* [文件配置](#文件配置)
* [ConfigMap](#ConfigMap) （仅适用于 ```Kubernetes``` 部署）

具体配置方式取决于您的部署方式，可以同时存在多种配置方式。

### 环境变量
> 具体的环境变量配置方式根据操作系统不同有所区别。使用 Docker 或者 Kubernetes 部署服务时可以通过环境变量更改配置。

#### 配置示例
```yaml
smtp:
  host: "smtp.exmail.qq.com" # SMTP 服务器地址
  user: "YOUR_USERNAME"
  pass: "YOUR_PASSWORD"
```
#### 环境变量形式
```smtp.user=xx@xx.com```

```smtp.pass=******```

### 文件配置

在运行目录（通常为 ```可执行 jar 文件``` 目录下）创建 ```application.yaml```，应用启动时将读取此配置文件。

如果您以 Docker 方式部署服务，可以将文件挂载到容器路径 ```/application.yaml``` 。

配置文件模板请参考：
[配置文件模板](#配置模板)
或
[完整配置文件](../auth-service/src/main/resources/application.yaml)

### ConfigMap
在 ```Kubernetes``` 集群中部署服务时，可以创建 ```ConfigMap``` 保存 ```application.yaml``` 文件的内容并以数据卷的形式挂载到 ```Pod``` 中。
具体方式请参考：[Kubernetes 文档](https://kubernetes.io/zh/docs/concepts/configuration/configmap/)

另外，本服务集成了 ```Spring Cloud Kubernetes```，有更多的选项以及更灵活的方式可以进行选择。
详情请参考：[Spring Cloud Kubernetes 文档](https://docs.spring.io/spring-cloud-kubernetes/docs/current/reference/html/#configmap-propertysource)

## 配置模板

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

