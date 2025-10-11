FROM maven:3.8.7-eclipse-temurin-17 as build
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
COPY src/main/proto ./src/main/proto
# RUN mvn -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app

# ✅ Install netcat for the wait script
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

COPY ./target/nickname-service-0.0.1-SNAPSHOT.jar app.jar

COPY ./scripts/wait-for-dependencies.sh /usr/local/bin/wait-for-dependencies.sh

RUN chmod +x /usr/local/bin/wait-for-dependencies.sh

EXPOSE 3001 9098

ENTRYPOINT ["/usr/local/bin/wait-for-dependencies.sh"]

CMD ["java","-jar","app.jar"]
