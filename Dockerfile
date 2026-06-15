# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy POMs first for dependency caching
COPY pom.xml .
COPY artsie-domain/pom.xml artsie-domain/
COPY artsie-storage/pom.xml artsie-storage/
COPY artsie-api/pom.xml artsie-api/
COPY artsie-admin/pom.xml artsie-admin/
COPY artsie-app/pom.xml artsie-app/

# Download dependencies (cached unless POMs change)
RUN mvn dependency:go-offline -B

# Copy source and build
COPY . .
RUN mvn clean package -DskipTests -B

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Non-root user for security
RUN addgroup -S artsie && adduser -S artsie -G artsie
USER artsie

# Copy the fat JAR from the build stage
COPY --from=build /app/artsie-app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
