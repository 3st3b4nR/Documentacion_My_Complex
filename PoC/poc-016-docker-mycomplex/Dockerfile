# =========================
# Etapa 1: construcción
# =========================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src

RUN mvn -q clean package -DskipTests


# =========================
# Etapa 2: ejecución
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

RUN groupadd --system mycomplex \
    && useradd --system --gid mycomplex --create-home mycomplex

COPY --from=build /workspace/target/*.jar /app/app.jar

USER mycomplex

EXPOSE 8080

ENV SERVER_PORT=8080
ENV APP_ENVIRONMENT=container

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
