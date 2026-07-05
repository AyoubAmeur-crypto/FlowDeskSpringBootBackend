# ============================================================
# FlowDesk Backend — Multi-stage Dockerfile
# ============================================================
# Build stage: Maven + JDK 17
# ------------------------------------------------------------
FROM eclipse-temurin:17-jdk-alpine AS builder
LABEL stage=builder

WORKDIR /app

# Copy Maven POM first to leverage Docker layer caching
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B -q

# Copy source and build the JAR
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests -B -q && \
    mv target/*.jar app.jar

# ============================================================
# Runtime stage: JDK 17 (slim)
# ------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -S flowdesk && adduser -S flowdesk -G flowdesk

# Copy the built JAR from the builder stage
COPY --from=builder /app/app.jar app.jar

USER flowdesk

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=40s --retries=5 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/login 2>/dev/null || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
