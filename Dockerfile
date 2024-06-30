# Etapa de construcción
FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app
COPY Server/gradle /app/gradle
COPY Server/gradlew /app/gradlew
COPY Server/build.gradle /app/build.gradle
COPY Server/settings.gradle /app/settings.gradle 
COPY Server/src /app/src
RUN chmod +x ./gradlew
RUN ./gradlew clean build --no-daemon -x test

# Etapa de ejecución
FROM gradle:7.5.1-jdk17
WORKDIR /app
COPY --from=build /app /app
CMD ["./gradlew", "bootRun"]