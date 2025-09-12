# Dockerfile CORRIGIDO
FROM maven:3.9-eclipse-temurin-21-alpine as builder

WORKDIR /app

# Copia arquivos de build primeiro (cache eficiente)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia código e compila
COPY src ./src
RUN mvn package -DskipTests

# Imagem final minimalista
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o JAR da etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Saúde check para Docker
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]