FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /build

COPY .mvn/ .mvn/
COPY --chmod=0755 mvnw .
COPY pom.xml .
COPY src/ src/
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /build/target/*.jar /app/api.jar

USER 10001:10001
EXPOSE 10000
CMD ["sh", "-c", "exec java -jar /app/api.jar --server.address=0.0.0.0 --server.port=${PORT:-10000}"]
