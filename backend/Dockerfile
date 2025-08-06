# Etapa de build
FROM maven:3.9.4-eclipse-temurin-17 as builder

WORKDIR /app
COPY app /app
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ADD https://repo1.maven.org/maven2/io/sentry/sentry-opentelemetry-agent/8.18.0/sentry-opentelemetry-agent-8.18.0.jar /app/sentry-opentelemetry-agent.jar

# Adiciona o endpoint do OTLP que o collector escuta
ENV JAVA_TOOL_OPTIONS="-javaagent:/app/sentry-opentelemetry-agent.jar -Dotel.exporter.otlp.endpoint=${OTEL_EXPORTER_OTLP_ENDPOINT}"

ENTRYPOINT ["java", "-jar", "app.jar"]

