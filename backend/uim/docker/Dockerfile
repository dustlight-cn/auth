FROM openjdk:8-jre-alpine
VOLUME /tmp

# 安装字体，用于验证码生成
RUN set -xe \
&& apk --no-cache add ttf-dejavu fontconfig
COPY ALGER.TTF /usr/share/fonts/ALGER.TTF

COPY service.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]