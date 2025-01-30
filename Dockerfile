#Use OpenJDK as base image
FROM openjdk:17-jdk-slim

#Set the working directory inside the container
WORKDIR /app

#Copy the built JAR file from Maven target directory to the container
COPY target/*.jar app.jar

# Expose the application port (this needs to match port specified in yml file)
EXPOSE 8082

#Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

