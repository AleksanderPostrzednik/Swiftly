# Etap 1: budowanie z JDK21 (eclipse-temurin + apt-get install maven)
FROM eclipse-temurin:21-jdk AS build
RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etap 2: runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/Swiftly-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
