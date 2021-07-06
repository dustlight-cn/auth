#
# Maven 构建环境
#
#FROM maven:3.5.0-jdk-8-alpine AS builder
#
#COPY . /app
#WORKDIR /app
#
## Maven 打包
#RUN mvn package -Dmaven.test.skip=true
#
## 获取项目版本号并复制 auth-service
#RUN version=`mvn help:evaluate -Dexpression=project.version -q -DforceStdout` && \
#  cp auth-service/target/auth-service-$version.jar auth-service.jar

#
# JRE
#
FROM openjdk:11-jre-alpine

#COPY --from=builder /app/auth-service.jar app.jar
COPY auth-service.jar app.jar

# 暴露端口
EXPOSE 8080

ENTRYPOINT [ "java" , "-jar" , "-XX:+UseContainerSupport" , "/app.jar" ]