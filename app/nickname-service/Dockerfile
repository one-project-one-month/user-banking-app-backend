FROM maven:3.8.7-eclipse-temurin-17 as build

FROM eclipse-temurin:17-jre

COPY ./target/nickname-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 3001 9098
ENTRYPOINT ["java","-jar","app.jar"]
