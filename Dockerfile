# syntax=docker/dockerfile:1.7
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /workspace

COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
RUN ./mvnw -q -Dmaven.test.skip=true dependency:go-offline

COPY src src
RUN ./mvnw -q -Dmaven.test.skip=true package

FROM eclipse-temurin:21-jre-jammy AS runtime

WORKDIR /app

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
