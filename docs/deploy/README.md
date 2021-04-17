# 部署

* [服务部署](#服务部署)
    * [本地部署](#本地部署)
    * [Docker 部署](#Docker-部署)
    * [Kubernetes 部署](#Kubernetes-部署)
* [前端部署](#前端部署)
    * [Nginx 部署](#Nginx-部署)
    * [腾讯云 COS 部署](#腾讯云-COS-部署)
        * [CDN 配置](#CDN-配置)

## 服务部署
### 本地部署
> 本地部署需要提前请安装 [Java 运行环境 (JRE)](https://www.oracle.com/java/technologies/javase-jre8-downloads.html) 。

#### 获取可执行 Jar
您可以从 [Release](https://github.com/dustlight-cn/auth/releases)
下载编译完成的可执行 Jar 包，
也可以选择进行 [手动构建](../Build.md)。

#### 配置文件
您需要配置数据库、Redis 以及 SMTP 账号才能成功运行服务，请参考：[服务配置](../ServiceConfig.md)

#### 部署后端服务
运行服务：
```
java -jar auth-service-*.jar
```

### Docker 部署

#### 拉取镜像

镜像： ```dustlightcn/auth-service``` [![Docker Image Version (latest semver)](https://img.shields.io/docker/v/dustlightcn/auth-service?label=version)](https://hub.docker.com/repository/docker/dustlightcn/auth-service) [![Docker Image Size (latest semver)](https://img.shields.io/docker/image-size/dustlightcn/auth-service?logo=docker)](https://hub.docker.com/repository/docker/dustlightcn/auth-service)


拉取镜像：
```
docker pull dustlightcn/auth-service:版本号（如 1.0.6-alpha-3）
```

#### 启动容器
> 通过 Docker 容器进行部署时，可以通过参数来配置环境变量（例如：```-e key=value```）。环境变量可以作为应用配置被读取，如 ```-e mysql.host=MYSQ_HOST```。
> 
> 也可以将 ```application.yaml``` 配置文件挂载到容器路径 ```/application.yaml``` 完成配置。
>
> 具体配置方式请参考：[服务配置](../ServiceConfig.md)

运行容器：
```
docker run -e mysql.host=MYSQL_HOST -p 8080:8080 --name auth-service dustlightcn/auth-service:版本号（如 1.0.5-alpha）
```

### Kubernetes 部署
#### 一键部署
> 使用 ```kubectl``` 一键部署服务，集群中需要至少两个持久卷（```PersistentVolume```）用于 MySQL 数据库 与 Redis 的持久化。

```bash
kubectl apply -f https://raw.githubusercontent.com/dustlight-cn/auth/master/docs/deploy/k8s/deploy.yaml
```

详情请参考：[Kubernetes 部署](k8s)

## 前端部署
> 您可以从 [Release](https://github.com/dustlight-cn/auth/releases)
下载构建完成的压缩文件，也可以选择进行 [手动构建](../Build.md)。

### Nginx 部署

解压下载的压缩文件或者构建完成的 ```dist/spa``` 目录下的文件到您的 Web 目录，并为其进行 ```SPA``` 配置。

下面为 Nginx 的配置示例：

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

> 正式部署时需要更改后端接口的地址，可以在 ```js/app.*.js``` 中搜索 ```host:"."```，
> 并将其替换为后端域名，如 ```host:"https://api.dustlight.cn"```。若需要更改更多参数，如用户名正则、谷歌验证码 Key，建议选择手动构建。（修改前端项目的配置文件 ```src/config.ts```）

### 腾讯云 COS 部署
待完善
#### CDN 配置
待完善