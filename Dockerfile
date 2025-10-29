# Estágio 1: Build com JDK 21
FROM maven:3.9.3-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copia o pom.xml e baixa as dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copia o resto do código-fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Runtime com JRE 21 (imagem menor)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia o JAR do estágio de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]