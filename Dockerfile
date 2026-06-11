FROM eclipse-temurin:25-jdk-jammy AS build

WORKDIR /app

COPY gradlew gradlew.bat settings.gradle* build.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN chmod +x gradlew && ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:25-jre-jammy

WORKDIR /app

RUN useradd --system --create-home spring
USER spring

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
