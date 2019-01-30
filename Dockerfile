FROM java:8
FROM maven:alpine
# image layer
WORKDIR /app
ADD pom.xml /app
# Image layer: with the application
COPY . /app
#Build jar file
RUN mvn clean install

FROM java:8
EXPOSE 5050
WORKDIR /app
COPY target/nideas-api-user-service.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
