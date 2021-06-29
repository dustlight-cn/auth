#
# Maven 构建环境
#
FROM maven:3.5.0-jdk-8-alpine AS builder

COPY . /app
WORKDIR /app

RUN mvn package -Dmaven.test.skip=true

FROM openjdk:8-jre-alpine

COPY ./auth-service.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]