FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .

# Build whole project so parent POM is installed
RUN mvn -B clean install -DskipTests

# Ensure Maven installed the parent properly
RUN ls -R /root/.m2/repository/com/corporatebanking

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
