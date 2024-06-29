# Use the latest Gradle image with JDK 17 to build the project
FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app
COPY /build.gradle /settings.gradle ./
COPY /src ./src
RUN gradle build --no-daemon -x test

# Use a Tomcat image with JDK 17 to deploy the application
FROM tomcat:9.0.65-jdk17-corretto
WORKDIR /usr/local/tomcat/webapps/
COPY --from=build /app/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh","run"]