# 构建

> 前端构建需要提前安装 [Node.js](https://nodejs.org/)  。
>
> 后端构建需要提前安装 [Java 开发工具包 (JDK)](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
> 以及 [Maven](https://maven.apache.org/) 。
> 
> Docker 镜像构建需要提前安装环境，参考 [Docker 官网](https://www.docker.com/)

如果您需要自己构建项目，可以参考以下步骤进行构建。

* [构建后端服务（Jar）](#构建后端服务)
* [构建 Docker 镜像](#构建-Docker-镜像)
* [构建前端 UI](#构建前端UI)

## 构建后端服务

执行 ```mvn package```

> 配置文件位于：[```auth-service/src/main/resources/application.yaml```](../auth-service/src/main/resources/application.yaml)
>
> 生成的文件位于 ```auth-service/target```

## 构建 Docker 镜像

复制构建完成的二进制文件到根目录，将其重命名为 ```auth-service.jar```

执行 ```docker build -t auth-service:TAG .```


## 构建前端UI

进入目录 ```cd auth-ui```

#### 安装 NPM 依赖

执行 ```npm i```

#### 进行前端构建
> 执行构建之前，请先进行配置，参考：[前端配置](UiConfig.md)

执行 ```quasar build```

成功构建之后，生成的文件位于 ```auth-ui/dist/spa``` 。