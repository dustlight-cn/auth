FROM openjdk:8-jre-alpine

COPY ./auth-service.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]