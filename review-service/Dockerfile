FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .

RUN mvn install -N -DskipTests

COPY security-common/pom.xml security-common/pom.xml
COPY security-common/src security-common/src

RUN mvn -q -f security-common/pom.xml clean install -DskipTests

COPY review-service/pom.xml review-service/pom.xml
COPY review-service/src review-service/src

RUN mvn -q -f review-service/pom.xml clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /workspace/review-service/target/review-service-1.0.0.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]